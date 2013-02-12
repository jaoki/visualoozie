<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Friend List Panels</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/redmond/jquery-ui.css" />

	<script type="text/javascript" src="js/joint.all.min.js" ></script>

	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>

	<link rel="stylesheet" type="text/css" href="<s:url value="/style/list_organizer.css"/>" />
		

	<script type="text/javascript" src="<s:url value="/js/list_organizer.js"/>" > </script>
	<script type="text/javascript" src="<s:url value="/js/WindowNotification.js"/>" > </script>

    <script type="text/javascript" >
		var allFriends;
		var friendLists;
		var listMembers;
		var notification;
		var userConfig;


$(function() {
	Joint.paper("myfsa", 1000, 200);
	var fsa = Joint.dia.fsa;
	var s0 = fsa.StartState.create({ position: {x: 50, y: 50} });
	var se = fsa.EndState.create({ position: {x: 450, y: 150} });
	var s1 = fsa.State.create({ position: {x: 120, y: 120}, label: "state 1" });
	var s2 = fsa.State.create({ position: {x: 300, y: 50}, label: "state 2" });

	var all = [s0, s1, s2, se];
	s0.joint(s1, fsa.arrow).registerForever(all);
	s1.joint(s2, fsa.arrow).registerForever(all);
	s2.joint(se, fsa.arrow).registerForever(all);



}); // End of JQuery Initialization


    </script>

</head>

<body>


	<!--------------- Top Menu ------------------------>
	<div class="blueBar">

	</div>

	<div>
		<form name="input" action="api/upload_xml" enctype="multipart/form-data" method="post">
			<input name="xmlfile" type="file"/>
			<input type="submit" value="Submit"/>
		</form>
		
	</div>

	<div id="myfsa"></div>
    
	


</body>
</html>
