<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>VisualOozie</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">


	<script type="text/javascript" src="js/joint.all.min.js" ></script>

	<!--
	<script type="text/javascript" src="js/raphael.js" ></script>
	<script type="text/javascript" src="js/joint.js" ></script>
	<script type="text/javascript" src="js/joint.dia.js" ></script>
	<script type="text/javascript" src="js/joint.dia.fsa.js" ></script>
	-->

	<script type="text/javascript" src="js/jquery-1.9.1.min.js" ></script>


	<link rel="stylesheet" type="text/css" href="<s:url value="/style/visualoozie.css"/>" />
		

	<script type="text/javascript" src="<s:url value="/js/list_organizer.js"/>" > </script>
	<script type="text/javascript" src="<s:url value="/js/WindowNotification.js"/>" > </script>

    <script type="text/javascript" >

$(function() {

	var submitButtonClicked = function(){
		$("#workflow_diagram").html("");
		$("#xml_editor_div").html("");
		$("#errorMessage").html("");

		var formData = new FormData($("#fileform")[0]);
		$.ajax({
			url: 'api/upload_xml'
			, type: 'POST'
			, data: formData
			, complete: function(jqXHR, textStatus){
				var res = $.parseJSON(jqXHR.responseText);
				drawXmlEditor(res.escapedXml);

				if(!res.succeeded){
					$("#errorMessage").html(res.errorMessage);
					return;
				}
				drawDiagram(res);

			}
			, cache: false
			, contentType: false
			, processData: false
		});
	};

	var weightedNodes = {};

	var PAPER_WITH = 500;

	function drawDiagram(res){
		Joint.paper("workflow_diagram", PAPER_WITH, 1000);
		var fsa = Joint.dia.fsa;
		var unsortedNodes = res.nodes;

		// Set weightedNodes wihtout sortOrder
		for(var nodeIndex = 0; nodeIndex < unsortedNodes.length; nodeIndex++){
			var node = unsortedNodes[nodeIndex];
			weightedNodes[node.name] = { to : node.to , type: node.type };
		}

		addSortOrderToWeightedNodes("start", 0);

		// Invert the index TODO pretty bad logic.
		var sortedNodeNames = [];
		for(var key in weightedNodes){
			 
			var notAdded = true;
			for(var i = 0; i < sortedNodeNames.length; i++){
				if(weightedNodes[key].sortOrder < sortedNodeNames[i].sortOrder){
					sortedNodeNames.splice(i, 0, {name: key, sortOrder: weightedNodes[key].sortOrder});
					notAdded = false;
					break;
				}
			}

			if(notAdded){
				sortedNodeNames.push({name: key, sortOrder: weightedNodes[key].sortOrder});
			}
		}

		var allCircles = [];
		var yPos = 40;
		// Generates svg circles
		for(var i = 0; i < sortedNodeNames.length; i++){
			// how many same sort order there are?
			var sameSortCount = 1
			for(; i + sameSortCount < sortedNodeNames.length ;sameSortCount++){
				if(sortedNodeNames[i].sortOrder != sortedNodeNames[i + sameSortCount].sortOrder){
					break;
				}
			}

			for(var j = 0; j < sameSortCount; j++){
				var attrs;
				if(weightedNodes[sortedNodeNames[i + j].name].type == "KILL"){
					attrs= { fill : "red" };
				}else if(weightedNodes[sortedNodeNames[i + j].name].type == "START" || weightedNodes[sortedNodeNames[i + j].name].type == "END"){
					attrs= { fill : "grey" };
				}else{
					attrs= {};
				}
				var circle = fsa.State.create({
							position: {x: (PAPER_WITH/(sameSortCount + 1) * (j+1)), y: yPos}
							, label: sortedNodeNames[i + j].name
							, attrs : attrs
						});
				allCircles.push(circle);
				weightedNodes[sortedNodeNames[i + j].name].circle = circle;

				if(j > 0){
					i++;
				}
			}

			yPos += 80;

		}

		// Add svg lines
		for(var key in weightedNodes){	
			var node = weightedNodes[key];
			for(var i = 0; i < node.to.length; i++){
				node.circle.joint(weightedNodes[node.to[i]].circle, fsa.arrow).registerForever(allCircles);
			}
		}

	}

	function addSortOrderToWeightedNodes(currentKey, parentSortOrder){
		if(weightedNodes[currentKey].sortOrder === undefined){
			weightedNodes[currentKey].sortOrder = (parentSortOrder + 1);
		}
		for(var i = 0; i < weightedNodes[currentKey].to.length; i++){
			addSortOrderToWeightedNodes(weightedNodes[currentKey].to[i], parentSortOrder + 1);
		}
	}

	function drawXmlEditor(xmlArray){
		var editor = "<table>";
		editor += "<tr>";
		editor += "<td>#</td>";
		editor += "<td>Content</td>";
		editor += "</tr>";
		for(var i = 0; i <  xmlArray.length; i++){
			var line = xmlArray[i];
			editor += "<tr>";
			editor += "<td>" + i + "</td>";
			editor += "<td>" + line + "</td>";
			editor += "</tr>";
		}
		editor += "</table>";
		$("#xml_editor_div").html(editor);
	}

	$("#submitButton").click(submitButtonClicked);

}); // End of JQuery Initialization


    </script>

</head>

<body>


	<!--------------- Top Menu ------------------------>
	<div class="blueBar">
		<span class="appTitle1">VisualOozie</span>
	</div>

	<div>
		<form id="fileform" name="input" action="api/upload_xml" enctype="multipart/form-data" method="post">
			<input name="xmlfile" type="file"/>
			<input id="submitButton" type="button" value="Submit File"/>
		</form>
	</div>
	<div>
		<span id="errorMessage" class="red"></span>
	</div>

	<div class="colstart">
		<div id="workflow_diagram" class="col1"></div>
		<div id="xml_editor_div" class="col1"></div>
	</div>

</body>
</html>
