<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Friend List Panels</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<!--
	<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/redmond/jquery-ui.css" />
	-->

	<script type="text/javascript" src="js/joint.all.min.js" ></script>
	<script type="text/javascript" src="js/jquery-1.9.1.min.js" ></script>

	<!--
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
	-->

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

function drawDiagram(res){
	Joint.paper("myfsa1", 500, 2000);
	var fsa = Joint.dia.fsa;

	var yPos = 50;

	var nodes = [];
	var nodes2 = {};
	var cStart = fsa.State.create({ position: {x: 200, y: yPos}, label: "Start" });
	nodes.push(cStart);
	nodes2["start"] = { node: cStart, to : [ res.start.to] };

	for(var i = 0; i < res.decisionOrForkOrJoin.length; i++){
		yPos += 100;
		var circle;
			circle = fsa.State.create({ position: {x: 200, y: yPos}, label: res.decisionOrForkOrJoin[i].name });
//		if(res.decisionOrForkOrJoin[i].java !=null){
//			var java = res.decisionOrForkOrJoin[i].java;
//		}

		nodes.push(circle);

		// kill does not have ok and error
		if(res.decisionOrForkOrJoin[i].ok === undefined && res.decisionOrForkOrJoin[i].error === undefined){
			nodes2[res.decisionOrForkOrJoin[i].name] = {node: circle, to : []};
		}else{
			nodes2[res.decisionOrForkOrJoin[i].name] = {node: circle, to : [ res.decisionOrForkOrJoin[i].ok.to, res.decisionOrForkOrJoin[i].error.to]};
		}

	}

	yPos += 100;

	var cEnd = fsa.State.create({ position: {x: 200, y: yPos}, label: "End" });
	nodes.push(cEnd);
	nodes2[res.end.name] = {node: cEnd, to:[] };

//	var current = nodes2["start"];
//	while(true){	
//		traverse_counter++;
//
//		if(current.to.length == 0 || traverse_counter > 100){
//			break;
//		}
//
//		for(var i = 0; i < current.to.length; i++){
//			current.node.joint(nodes2[current.to[i]].node, fsa.arrow).registerForever(nodes);
//		}
//
//		// TODO fix this
//		current = nodes2[current.to[0]];
//
//	}

//	var current = nodes2["start"];
	for(var prop in nodes2){	
		var current = nodes2[prop];

		for(var i = 0; i < current.to.length; i++){
			current.node.joint(nodes2[current.to[i]].node, fsa.arrow).registerForever(nodes);
		}

	}




//	for(var i = 0; i < nodes.length - 1; i++){
//		nodes[i].joint(nodes[i + 1], fsa.arrow).registerForever(nodes);
//	}

}

// return if any to is found
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
