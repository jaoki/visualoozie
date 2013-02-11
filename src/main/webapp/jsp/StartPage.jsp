<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Facebook Friend List Organizer Start Page</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/redmond/jquery-ui.css" />
	<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/redmond/jquery.ui.button.css" />
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>

	<link rel="stylesheet" type="text/css" href="<s:url value="/style/list_organizer.css"/>" />

	<script type="text/javascript" src="<s:url value="/js/list_organizer.js"/>" > </script>

    <script type="text/javascript" >
		// jQuery initialization
		var accessToken;
		$(function() {

			// Facebook access_token check
			if (window.location.hash.length == 0) {
				facebookAuth();
				return;
			}

			accessToken = window.location.hash.substring(1);
			$("#fbAccessToken").val(accessToken);
			$("#startForm").submit();

		});

    </script>
	
	<script language="javascript" type='text/javascript'> 

		function facebookAuth() {
			var appID = '270491746325022'; // local test
			// var appID = '136900619743227'; // junaoki.net
			var api = 'https://www.facebook.com/dialog/oauth?';
			var queryParams = [
				'client_id=' + appID
				, 'redirect_uri=' + window.location.href.replace(/#.*/, "")
				, 'response_type=token'
				, 'scope=read_friendlists,manage_friendlists'
			];
			var query = queryParams.join('&');
			var url = api + query;
			window.top.location.href = url;
			return;
		}



	</script> 

</head>

<body>

	Welcome to Facebook Friend List Organizer
	<form id="startForm" action="ShowFriendPanels" method="POST">
		<input name="fbAccessToken" id="fbAccessToken" type="hidden"/>
		<button id="button_start" type="submit" disabled="true">One Moment...</button>
	</form>

</body>
</html>
