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
			notification = new WindowNotification("body");

			$( "#help-message" ).dialog({
				autoOpen: false,
				modal: true,
				closeOnEscape: true,
				width: 400,
				buttons: {
					Ok: function() {
						$( this ).dialog( "close" );
					}
				}
			});

			userConfig = new UserConfiguration();
			userConfig.load();

			allFriends = <s:property value="friends" escape="false"/>;

			friendLists = <s:property value="friendLists" escape="false"/>;

			listMembers = new Array();
			<s:iterator value="friendListMembersJS" id="friendListMembersJS" status="st" >
				<s:property escape="false"/>;
			</s:iterator>


			$( "#labelName" ).html("<a href='https://www.facebook.com/" + me.id + "' target='_blank'>" + me.name + "</a>");
			$( "#profilePhoto" ).html("<a href='https://www.facebook.com/" + me.id + "' target='_blank'><img style='width: 25px; height 25px;' src='https://graph.facebook.com/" + me.id + "/picture'/></a>");

			$("#readme").accordion({
				active: -1 
				, collapsible: true 
				, change: function(event, ui){
					if($(this).find("div")[0].className.indexOf("ui-accordion-content-active") == -1)
						$(this).width("80px");
					else
						$(this).width("300px");
				}
			});

			$( "#addListButton" ).button({
					icons: {
						primary: "ui-icon-circle-plus"
					},
					text: false
			}).height("16px").width("16px");

			drawPanels();

			$("input:text").focus(function() { $(this).select(); } );


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

	<div id="fb-root"></div>
	<script>(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) return;
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=136900619743227";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));</script>


	<!--------------- Top Menu ------------------------>
	<div class="blueBar">
		<span class="appTitle1"><a href="https://www.facebook.com/" target="_blank">facebook</a></span>
		<span class="appTitle2">Friend List Organizer</span>
		<span class="alphaversion">Ver. 0.4.01.01</span>
		<span class="alphaversion">Alpha Version</span>
		<div class="fb-like" data-href="https://www.facebook.com/friend.list.organizer" data-send="true" data-layout="button_count" data-width="130" data-show-faces="false"></div>

		<!--------------- Profile Icon etc. ------------------------>
		<div style="float: right; position: relative; right: 30px; top: 10px;">
			<div style="margin-right: 5px; float: left; " id='profilePhoto'></div>
			<div class="profileName" id='labelName'></div>

			<button style=" float: left; margin-right: 5px; top: 1px;" type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only' role='button' aria-disabled='false' title='Hint' onClick='hint();'>
				<span class='ui-button-icon-primary ui-icon ui-icon-help'></span>
				<span class='ui-button-text'>Hint</span>
			</button>

			<!--------------- Read Me ------------------------>
			<div id="readme" style="width: 80px; float: left;">
				<h3><a href="#">Read Me</a></h3>
				<div>
					<a href="https://www.facebook.com/friend.list.organizer" target="_blank">Support</a><br/>
					<a href="https://www.facebook.com/friend.list.organizer" target="_blank">Feedback</a><br/>
					<span style="font-weight: bold;">The supported browsers are Firefox, Chrome and IE.</span>
					<p>
						What's next!?<br/>
						<ul>
							<li>Removing friends from lists</li>
						</ul> 
					</p>
				</div>
			</div>

		</div>

		<!--------------- Menu ------------------------>
		<div id="createList">
			New List Name <input id="newListTextbox" type="text" />
			<button id="addListButton" type='button' title='Create New List' onClick='addList();'></button>
			<input id="friendSearchText" size="29" class="searchbox_nocriteria" type="text" onChange="friendSearchText_change(event);" onKeyUp="friendSearchText_keyup(event);" value="Type your friend name to search"/>
			<input id="toggleNameCheckbox" type="checkbox" title="Show friend's names" checked="true" onClick="toggleFriendNames();"/>

			<button id="resetMainPanelSize" type="button" title="Set main panel size to the window size" onClick="userConfig.resetMainPanelSize();">Set Panels to Screen</button>
		</div>

	</div>

	<!--------------- Main Panel ------------------------>
	<input name="fbAccessToken" id="fbAccessToken" value="<s:property value="fbAccessToken" escape="false"/>" type="hidden"/>

	<div id="main_area" class="main_area">

		<div id="main_panel" class="main_panel">
			<div id="main_panel_content" class="main_panel_content"></div>
		</div>

		<div id="ads_area" class="ads_area">
			<iframe src="http://rcm.amazon.com/e/cm?t=frielistorga-20&o=1&p=29&l=ur1&category=electronics&f=ifr" width="120" height="600" scrolling="no" border="0" marginwidth="0" style="border:none;" frameborder="0"></iframe>

			<script type="text/javascript"><!--
			google_ad_client = "ca-pub-0293494487488769";
			/* list_organizer2 */
			google_ad_slot = "8635144172";
			google_ad_width = 120;
			google_ad_height = 90;
			//-->
			</script>
			<script type="text/javascript"
			src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
			</script>

			<script type="text/javascript"><!--
			google_ad_client = "ca-pub-0293494487488769";
			/* text3 */
			google_ad_slot = "5934796548";
			google_ad_width = 120;
			google_ad_height = 240;
			//-->
			</script>
			<script type="text/javascript"
			src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
			</script>

		</div>
	</div>

	<!--------------- Help ------------------------>
	<div id="help-message" title="Friend List Organizer Help">
		<p>
			<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
			You can drag and drop your friends to a list.
		</p>
		<div style='margin: 6px;float: left; height: 200px; width:100px;'>
			<div class='panelContentDiv panel_div_css0' style='height: 200px; width:100px; overflow: visible;'>
				Friend List
				<img id="hint_image" class='friend_photo' style='position: relative; height: 50px; width: 50px;' src='https://graph.facebook.com/zuck/picture' />
			</div>
		</div>
		<div style='margin: 6px;float: left; height: 200px; width:100px;'>
			<div class='panelContentDiv panel_div_css1' style='height: 200px; width:100px; '>
				Other Friend List
			</div>
		</div>
	</div>


</body>
</html>
