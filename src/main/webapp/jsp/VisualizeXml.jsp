<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>VisualOozie</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<link rel="stylesheet" type="text/css" href='<s:url value="/style/visualoozie.css"/>' />
<!--
	<link rel="stylesheet" type="text/css" href="<s:url value="/style/shCore.css"/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value="/style/shThemeDefault.css"/>" />
-->

	<script type="text/javascript" src='<s:url value="/js/joint.all.min.js"/>' ></script>

	<!--
	<script type="text/javascript" src="/js/raphael.js" ></script>
	<script type="text/javascript" src="/js/joint.js" ></script>
	<script type="text/javascript" src="/js/joint.dia.js" ></script>
	<script type="text/javascript" src="/js/joint.dia.fsa.js" ></script>
	-->

	<script type="text/javascript" src='<s:url value="/js/jquery-1.9.1.min.js"/>' ></script>
	<script type="text/javascript" src='<s:url value="/js/visualoozie.js"/>' ></script>
	<script type="text/javascript">
		vo.contextRoot = '<s:url value="/"/>';

	</script>


	<script type="text/javascript" src='<s:url value="/js/visualoozie_VisualizeXml.js"/>' ></script>


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
		<div id="xml_editor_div" class="col1" style="border: 1px solid;">
			<code>
			</code>
		</div>
	</div>

</body>
</html>
