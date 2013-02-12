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
			res.start;
		}
        , cache: false
        , contentType: false
        , processData: false
    });
};

$(function() {

	$("#submitButton").click(submitButtonClicked);
	Joint.paper("myfsa", 1000, 200);
	var fsa = Joint.dia.fsa;
//	var s0 = fsa.StartState.create({ position: {x: 50, y: 50} });
	var se = fsa.EndState.create({ position: {x: 450, y: 150} });
	var s1 = fsa.State.create({ position: {x: 120, y: 120}, label: "state 1" });
	var s2 = fsa.State.create({ position: {x: 300, y: 50}, label: "state 2" });

//	var all = [s0, s1, s2, se];
	var all = [s1, s2, se];
//	s0.joint(s1, fsa.arrow).registerForever(all);
	s1.joint(s2, fsa.arrow).registerForever(all);
	s2.joint(se, fsa.arrow).registerForever(all);

	var json = {"any":null,"credentials":null,"decisionOrForkOrJoin":[{"anyUnderChoice":null,"anyUnderSequence":null,"cred":null,"error":{"to":"fail"},"fs":null,"java":{"archive":[],"arg":["Hello","Oozie!"],"captureOutput":null,"configuration":{"property":[{"description":null,"name":"mapred.job.queue.name","value":"${queueName}"}]},"file":[],"javaOpt":[],"javaOpts":null,"jobTracker":"${jobTracker}","jobXml":[],"mainClass":"org.apache.oozie.example.DemoJavaMain","nameNode":"${nameNode}","prepare":null},"mapReduce":null,"name":"java-node","ok":{"to":"end"},"pig":null,"retryInterval":null,"retryMax":null,"subWorkflow":null},{"message":"Java failed, error","name":"fail"}],"end":{"name":"end"},"global":null,"name":"java-main-wf","parameters":null,"start":{"to":"java-node"}};

	Joint.paper("myfsa1", 1000, 200);
	var fsa = Joint.dia.fsa;

	var cStart = fsa.State.create({ position: {x: 120, y: 120}, label: "Start" });
	var cEnd = fsa.State.create({ position: {x: 500, y: 120}, label: "End" });
	var circles = [];
	var xPos = 300;

	var all = [cStart, cEnd];
	for(var i = 0; i < json.decisionOrForkOrJoin.length; i++){
		var circle;
			circle = fsa.State.create({ position: {x: xPos, y: 50}, label: json.decisionOrForkOrJoin[i].name });
//		if(json.decisionOrForkOrJoin[i].java !=null){
//			var java = json.decisionOrForkOrJoin[i].java;
//		}

		all.push(circle);
		circles.push(circle);
		xPos += 50;
	}
//	var s2 = fsa.State.create({ position: {x: 300, y: 50}, label: "state 2" });

	
	for(var i = 0; i < circles.length; i++){
		if(i == 0){
			cStart.joint(circles[0], fsa.arrow).registerForever(all);
		}

		if(i == circles.length - 1){
			circles[i].joint(cEnd, fsa.arrow).registerForever(all);
		}
	}


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

	<div id="myfsa"></div>
	<div id="myfsa1"></div>
    
	


</body>
</html>
