<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>VisualOozie</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<link rel="stylesheet" type="text/css" href='<s:url value="/style/visualoozie.css"/>' />

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

<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-38570621-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>

</head>

<body>


	<!--------------- Top Menu ------------------------>
	<div class="blueBar">
		<div style="width: 2em; float:left;">&nbsp;</div>
		<div style="float:left;">
			<span class="appTitle1">VisualOozie</span>
			<span class="appTitle2">to visualize Hadoop Oozie workflow</span>
			<span class="alphaversion">Alpha Version</span>
		</div>
		<div style="width: 2em; float:right;">&nbsp;</div>
		<div style="float:right;">
			<span class="appTitle1">&nbsp;</span>
			<span class="appTitle2">
				<a class="appTitle2" href="https://groups.google.com/forum/#!forum/visualoozie-user" target="_blank">Forum</a>
			</span>
		</div>
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
