<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Friend List Panels</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/redmond/jquery-ui.css" />
	<!--
	<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/redmond/jquery.ui.button.css" />
	-->
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>

	<link rel="stylesheet" type="text/css" href="<s:url value="/style/list_organizer.css"/>" />
		

	<script type="text/javascript" src="<s:url value="/js/list_organizer.js"/>" > </script>
	<script type="text/javascript" src="<s:url value="/js/WindowNotification.js"/>" > </script>

    <script type="text/javascript" >
		var me = <s:property value="me" escape="false"/>;
		var allFriends;
		var friendLists;
		var listMembers;
		var notification;
		var userConfig;


		// jQuery Initialization
		$(function() {


		}); // End of JQuery Initialization

		var FRIEND_SEARCH_TEXT_MESSAGE = "Type your friend name to search";

		function friendSearchText_change(e){
			if($("#friendSearchText").val() == ""){
				$("#friendSearchText")
						.val(FRIEND_SEARCH_TEXT_MESSAGE)
						.addClass("searchbox_nocriteria")
						.removeClass("searchbox_notfound")
						.removeClass("searchbox");
			}
		}

		function friendSearchText_keyup(e){

			if($("#friendSearchText").val() == FRIEND_SEARCH_TEXT_MESSAGE)
				return;

			if(// e.keyCode == 8  // backspace
					e.keyCode == 9  // tab
					|| (112 <= e.keyCode && e.keyCode <= 123)  // Functional keys
					|| e.keyCode == 192  // Alt Tab
					|| e.keyCode == 17  // Shift
					|| e.keyCode == 18  // alt
					|| e.keyCode == 13  // enter
					|| (37 <= e.keyCode && e.keyCode <= 40)  // Arrow key
					|| (e.keyCode == 65 && e.ctrlKey == true) // Ctrl+A
					){
				return;
			}

			var searchValue = $("#friendSearchText").val().toLowerCase();

			if(searchValue.length == 0){
				for(var i = 0; i < allFriends.data.length; i++){
					$("#friend_" + allFriends.data[i].id).show();
				}
				$("#friendSearchText")
						.addClass("searchbox")
						.removeClass("searchbox_notfound")
						.removeClass("searchbox_nocriteria");
				return;
			}

			var found = false;
			for(var i = 0; i < allFriends.data.length; i++){

				if(allFriends.data[i].name.toLowerCase().indexOf(searchValue) == -1){ // Does not match
					$("#friend_" + allFriends.data[i].id).hide();
				}else{ // Matches
					found = true;
					$("#friend_" + allFriends.data[i].id).show();
				}
			}

			if(found){
				$("#friendSearchText")
						.addClass("searchbox")
						.removeClass("searchbox_notfound")
						.removeClass("searchbox_nocriteria");
			}else{
				$("#friendSearchText")
						.addClass("searchbox_notfound")
						.removeClass("searchbox")
						.removeClass("searchbox_nocriteria");
			}
		}

    </script>

</head>

<body>


	<!--------------- Top Menu ------------------------>
	<div class="blueBar">

	</div>

	<div>
		 <input type="file"/>
	</div>

	<!--------------- Main Panel ------------------------>
	<input name="fbAccessToken" id="fbAccessToken" value="<s:property value="fbAccessToken" escape="false"/>" type="hidden"/>

	<div id="main_area" class="main_area">

		<div id="main_panel" class="main_panel">
			<div id="main_panel_content" class="main_panel_content"></div>
		</div>

	</div>

</body>
</html>
