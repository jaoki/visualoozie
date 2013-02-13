<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Friend List Panels</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">


	<script type="text/javascript" src="js/joint.all.min.js" ></script>

	<!--
	<script type="text/javascript" src="js/raphael.js" ></script>
	<script type="text/javascript" src="js/joint.js" ></script>
	<script type="text/javascript" src="js/joint.dia.js" ></script>
	<script type="text/javascript" src="js/joint.dia.fsa.js" ></script>
	-->

	<script type="text/javascript" src="js/jquery-1.9.1.min.js" ></script>


	<link rel="stylesheet" type="text/css" href="<s:url value="/style/list_organizer.css"/>" />
		

	<script type="text/javascript" src="<s:url value="/js/list_organizer.js"/>" > </script>
	<script type="text/javascript" src="<s:url value="/js/WindowNotification.js"/>" > </script>

    <script type="text/javascript" >


var submitButtonClicked = function(){
    var formData = new FormData($("#fileform")[0]);
    $.ajax({
		url: 'api/upload_xml'
		, type: 'POST'
        , data: formData
		, complete: function(jqXHR, textStatus){
			var res = $.parseJSON(jqXHR.responseText);
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
	Joint.paper("myfsa1", PAPER_WITH, 1000);
	var fsa = Joint.dia.fsa;

	weightedNodes["start"] = { to : [ res.start.to] };

	for(var i = 0; i < res.decisionOrForkOrJoin.length; i++){
//		var circle = fsa.State.create({ position: {x: 200, y: yPos}, label: res.decisionOrForkOrJoin[i].name });
//		if(res.decisionOrForkOrJoin[i].java !=null){
//			var java = res.decisionOrForkOrJoin[i].java;
//		}

		// kill does not have ok and error
		if(res.decisionOrForkOrJoin[i].ok === undefined && res.decisionOrForkOrJoin[i].error === undefined){
			weightedNodes[res.decisionOrForkOrJoin[i].name] = { to : [] };
		}else{
			weightedNodes[res.decisionOrForkOrJoin[i].name] = { to : [res.decisionOrForkOrJoin[i].ok.to, res.decisionOrForkOrJoin[i].error.to] };
		}

	}


	weightedNodes[res.end.name] = { to : [] };

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

	var allNodeArray = [];
	var yPos = 40;
	for(var i = 0; i < sortedNodeNames.length; i++){
		// how many same sort order there are?
		var sameSortCount = 1
		for(; i + sameSortCount < sortedNodeNames.length ;sameSortCount++){
			if(sortedNodeNames[i].sortOrder != sortedNodeNames[i + sameSortCount].sortOrder){
				break;
			}
		}

		for(var j = 0; j < sameSortCount; j++){
			var circle = fsa.State.create({
						position: {x: (PAPER_WITH/(sameSortCount + 1) * (j+1)), y: yPos}
						, label: sortedNodeNames[i + j].name
					});
			allNodeArray.push(circle);
			weightedNodes[sortedNodeNames[i + j].name].circle = circle;

			if(j > 0){
				i++;
			}
		}

		yPos += 80;

	}

	// Add lines
	for(var key in weightedNodes){	
		var node = weightedNodes[key];

		for(var i = 0; i < node.to.length; i++){
			node.circle.joint(weightedNodes[node.to[i]].circle, fsa.arrow).registerForever(allNodeArray);
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

$(function() {

	$("#submitButton").click(submitButtonClicked);

}); // End of JQuery Initialization


    </script>

</head>

<body>


	<!--------------- Top Menu ------------------------>
	<div class="blueBar">

	</div>

	<div>
		<form id="fileform" name="input" action="api/upload_xml" enctype="multipart/form-data" method="post">
			<input name="xmlfile" type="file"/>
			<input id="submitButton" type="button" value="Submit"/>
		</form>
		
	</div>

	<div id="myfsa1"></div>



</body>
</html>
