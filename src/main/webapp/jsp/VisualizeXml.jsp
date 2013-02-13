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

var traverse_counter = 0;
var weightedNodes = {};

function drawDiagram(res){
	Joint.paper("myfsa1", 500, 2000);
	var fsa = Joint.dia.fsa;

	var yPos = 50;

	var keyLength = 0;
	var allNodes = [];
	var nodes2 = {};
	var cStart = fsa.State.create({ 
		position: {x: 200, y: yPos}
		, label: "Start" 
		, radius: 15
		, attrs: {
			stroke: "blue",
			fill: "yellow",
			id: "aaa"
		  }
	});
	allNodes.push(cStart);
	nodes2["start"] = { node: cStart, to : [ res.start.to] };
	weightedNodes["start"] = { to : [ res.start.to] };
	keyLength++;


	for(var i = 0; i < res.decisionOrForkOrJoin.length; i++){
		yPos += 100;
		var circle = fsa.State.create({ position: {x: 200, y: yPos}, label: res.decisionOrForkOrJoin[i].name });
//		if(res.decisionOrForkOrJoin[i].java !=null){
//			var java = res.decisionOrForkOrJoin[i].java;
//		}

		allNodes.push(circle);

		// kill does not have ok and error
		if(res.decisionOrForkOrJoin[i].ok === undefined && res.decisionOrForkOrJoin[i].error === undefined){
			nodes2[res.decisionOrForkOrJoin[i].name] = {node: circle, to : []};
			weightedNodes[res.decisionOrForkOrJoin[i].name] = { to : [] };
		}else{
			nodes2[res.decisionOrForkOrJoin[i].name] = {node: circle, to : [ res.decisionOrForkOrJoin[i].ok.to, res.decisionOrForkOrJoin[i].error.to]};
			weightedNodes[res.decisionOrForkOrJoin[i].name] = { to : [res.decisionOrForkOrJoin[i].ok.to, res.decisionOrForkOrJoin[i].error.to] };
		}
		keyLength++;

	}

	yPos += 100;

	var cEnd = fsa.State.create({ position: {x: 200, y: yPos}, label: "End" });
	allNodes.push(cEnd);
	nodes2[res.end.name] = {node: cEnd, to:[] };
	weightedNodes[res.end.name] = { to : [] };
	keyLength++;

	// var sortOrder = 1;
	var currentKey = "start"; ;
	// weightedNodes[currentKey].sortOrder = sortOrder;
	addSortOrder(currentKey, 0);

	// Invert the index TODO pretty bad logic.
	var sortedNodeNames = [];
	for(var key in weightedNodes){
		 
		var notAdded = true;
		for(var i = 0; i < sortedNodeNames.length; i++){
			if(weightedNodes[key].sortOrder < sortedNodeNames[i].sortOrder){
				sortedNodeNames.splice(i, 0, {name: key, sortedOrder: weightedNodes[key].sortOrder});
				notAdded = false;
				break;
			}
		}

		if(notAdded){
			sortedNodeNames.push({name: key, sortOrder: weightedNodes[key].sortOrder});
		}
	}


//	for(var prop in nodes2){	
//		var current = nodes2[prop];
//
//		for(var i = 0; i < current.to.length; i++){
//			current.node.joint(nodes2[current.to[i]].node, fsa.arrow).registerForever(allNodes);
//		}
//
//	}

}

function addSortOrder(currentKey, parentSortOrder){
	if(weightedNodes[currentKey].sortOrder === undefined){
		weightedNodes[currentKey].sortOrder = (parentSortOrder + 1);
	}
	for(var i = 0; i < weightedNodes[currentKey].to.length; i++){
		addSortOrder(weightedNodes[currentKey].to[i], parentSortOrder + 1);
	}
}

// return if any to is found
/*
function knot(current){
	traverse_counter++;
	if(current.to.length == 0 || traverse_counter > 100){
		return false;
	}

	for(var i = 0; i < current.to.length; i++){
		current.node.joint(nodes2[current.to[i]].node, fsa.arrow).registerForever(nodes);
		knot(current);
	}
	return true;
}
*/

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
