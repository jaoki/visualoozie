<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>VisualOozie</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<link rel="stylesheet" type="text/css" href='<s:url value="/style/visualoozie.css"/>' />

	<script type="text/javascript" src='<s:url value="/js/jquery-1.9.1.min.js"/>' ></script>
	<script type="text/javascript" src='<s:url value="/js/visualoozie.js"/>' ></script>
	<script type="text/javascript" src='<s:url value="/js/wfjs/wfjs.js"/>' ></script>
	<script type="text/javascript">
		var fromStruts = '<s:url value="/"/>';
		vo.contextRoot = fromStruts.substring(0, fromStruts.lastIndexOf("/") + 1);

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
			<span class="alphaversion"><s:property value="Version" /> Alpha Version</span>
		</div>
		<div style="width: 2em; float:right;">&nbsp;</div>
		<div style="float:right;">
			<span class="appTitle1">&nbsp;</span>
			<span class="appTitle2">
				<a class="appTitle2" href="https://groups.google.com/forum/#!forum/visualoozie-user" target="_blank">Forum</a>
			</span>
		</div>
	</div>

	<div style="float: left;">
		<form id="fileform" name="input" action="api/upload_xml" enctype="multipart/form-data" method="post">
			<input name="xmlfile" type="file"/>
			<input id="fileSubmitButton" type="button" value="Validate Workflow XML File"/>
			<a href="https://github.com/yahoo/oozie/wiki/Oozie-WF-use-cases" target="_blank" title="Hadoop Oozie workflow.xml example">Example1</a>
			<a href="http://svn.apache.org/viewvc/oozie/trunk/examples/src/main/apps/demo/workflow.xml?view=markup" target="_blank" title="Hadoop Oozie workflow.xml example">Example2</a>
		</form>
	</div>
	<div style="float: left;">
		<input id="startOverbutton" type="button" value="Start Over"/>
	</div>

	<div class="colstart"> </div>

	<hr/>
	<div><span style="font-weight: bold;">Identified Namespace: </span> <span id="span_identifiedNamespace"></span> </div>

	<div>
		<span id="errorMessage" class="red"></span>
	</div>

	<div><img id="waiting_image" src="<s:url value="/img/wait.gif"/>" style="display: none;" /></div>

	<div class="colstart">
		<div id="xml_editor_div" class="col1" style="width: 50%; overflow: auto;">
			<textarea id="xml_textarea" style="width: 100%; height: 300px;"></textarea>
			<input id="textSubmitButton" type="button" value="Validate Workflow XML"/>
		</div>
		<div id="workflow_diagram" class="col1" style="width: 50%; overflow: auto;"></div>
	</div>

</body>
</html>
