/**
 * Copyright (c) 2013, Yahoo! Inc.  All rights reserved.
 * Copyrights licensed under the New BSD License. See the accompanying LICENSE file for terms
 */

$(function() {

	var onApiUploadXmlCompleted = function(jqXHR, textStatus){
		var res = $.parseJSON(jqXHR.responseText);
		drawXmlEditor(res.xml, res.lineNumber, res.columnNumber);
		$("#span_identifiedNamespace").html(res.identifiedNamespace);

		if(!res.succeeded){
			$("#errorMessage").html(res.errorMessage);
			return;
		}
		$("#waiting_image").hide();
		drawDiagram(res);

	};

	function submitPreparation(){
		$("#xml_editor_div").html("");
		$("#workflow_diagram").html("");
		$("#xml_editor_pre").html("");
		$("#errorMessage").html("");
		$("#waiting_image").show();
		collapseMenu();
	}

	var fileSubmitButtonClicked = function(){
		if($("#xmlfile").val() == ""){
			$("#errorMessage").html("Select a workflow.xml to upload");
			return;
		}
		submitPreparation();

		var formData = new FormData($("#fileform")[0]);
		$.ajax({
			url: vo.contextRoot + "api/upload_xml"
			, type: 'POST'
			, dataType: 'json'
			, data: formData
			, cache: false
			, contentType: false
			, processData: false
			, complete: onApiUploadXmlCompleted
		});
	};

	var textSubmitButtonClicked = function(){
		submitPreparation();

//		var formData = new FormData($("#fileform")[0]);
		$.ajax({
			url: vo.contextRoot + "api/upload_xml"
			, type: 'POST'
			, dataType: 'json'
			, data: {xmltext: $("#xml_textarea").val()}
			, cache: false
			, contentType: "application/x-www-form-urlencoded"
//			, contentType: false
//			, processData: false
			, complete : onApiUploadXmlCompleted

		});
	};

	var onWindowResize = function(){
		var editorHeight = window.innerHeight - $("#workflow_diagram").offset().top;
		$("#xml_editor_div").height(editorHeight);
		$("#workflow_diagram").height(editorHeight);
	};

	var weightedNodes;

	function drawDiagram(res){
		weightedNodes = {};
		var editorWidth = $(document).width() / 2;
		onWindowResize();
		var canvas1 = new wfjs.Canvas("workflow_diagram", editorWidth, 2000);
		var unsortedNodes = res.nodes;

		// Set weightedNodes wihtout sortOrder
		for(var nodeIndex = 0; nodeIndex < unsortedNodes.length; nodeIndex++){
			var node = unsortedNodes[nodeIndex];
			weightedNodes[node.name] = { to : node.to , type: node.type };
		}

		addSortOrderToWeightedNodes("start", 0);

		// Invert the index TODO pretty bad logic.
		var sortedNodeNames = [];
		for(var key in weightedNodes){
			 
			var notAdded = true;
			for(var i = 0; i < sortedNodeNames.length; i++){
				if(weightedNodes[key].sortOrder < sortedNodeNames[i].sortOrder){
					sortedNodeNames.splice(i, 0, {name: key, sortOrder: weightedNodes[key].sortOrder});
					notAdded = false;
					break;
				}
			}

			if(notAdded){
				sortedNodeNames.push({name: key, sortOrder: weightedNodes[key].sortOrder});
			}
		}

		var yPos = 40;
		// Generates svg nodes
		for(var i = 0; i < sortedNodeNames.length; ){
			// how many same sort order there are?
			var sameSortCount = 1
			for(; i + sameSortCount < sortedNodeNames.length ;sameSortCount++){
				if(sortedNodeNames[i].sortOrder != sortedNodeNames[i + sameSortCount].sortOrder){
					break;
				}
			}


			for(var j = 0; j < sameSortCount; j++){
				var xPos = (editorWidth/(sameSortCount + 1) * (j+1)) + Math.floor((Math.random()*30)+1); // Add some random number to fluctuate x-position

				var node;
				if(weightedNodes[sortedNodeNames[i + j].name].type == "KILL"){
					node = new wfjs.CircleNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name, { stroke: "red", fill: "red" });
				}else if(weightedNodes[sortedNodeNames[i + j].name].type == "DECISION"){
					node = new wfjs.DiamondNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name);
				}else if(weightedNodes[sortedNodeNames[i + j].name].type == "START" || weightedNodes[sortedNodeNames[i + j].name].type == "END"){
					node = new wfjs.CircleNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name, { stroke: "grey", fill: "grey" });
				}else{
					node = new wfjs.CircleNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name, { stroke: "blue", fill: "yellow" });
				}


				// TODO fix this (CircleNode has show() but DiamondNode does not)
				if(node.show !== undefined)
					node.show();



				weightedNodes[sortedNodeNames[i + j].name].node = node;

			}

			i = i + sameSortCount;

			yPos += 85;

		}


// TODO resize svg


		// Add svg lines
		for(var key in weightedNodes){	
			var node = weightedNodes[key];
			for(var i = 0; i < node.to.length; i++){
				node.node.connectTo(weightedNodes[node.to[i]].node);
			}
		}

	}

	function addSortOrderToWeightedNodes(currentKey, parentSortOrder){
		if(weightedNodes[currentKey].sortOrder === undefined){
			weightedNodes[currentKey].sortOrder = (parentSortOrder + 1);
		}
		for(var i = 0; i < weightedNodes[currentKey].to.length; i++){
			addSortOrderToWeightedNodes(weightedNodes[currentKey].to[i], parentSortOrder + 1);
		}
	}

	function drawXmlEditor(xmlArray, lineNumber, columnNumber){
		// Tokenize
		var tokens = [];
		for(var rawLineIndex = 0; rawLineIndex <  xmlArray.length; rawLineIndex++){
			var rawLine = xmlArray[rawLineIndex];
			for(var i = 0; i < rawLine.length; i++){
				var achar = rawLine.charAt(i);
				if(achar == "<"){
					tokens.push({lineNum : rawLineIndex + 1, type: "tagstart", value : "&lt;"});
				}else if(achar == ">"){
					tokens.push({lineNum : rawLineIndex + 1, type: "tagend", value : ">"});
				}else{
					var start = i;
					for(; i < rawLine.length - 1; i++){
						if(rawLine.charAt(i + 1) == "<" || rawLine.charAt(i + 1) == ">")
							break;
					}
					var chunk = rawLine.substring(start, i + 1).replace(/ /g, "&nbsp;").replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
					tokens.push({lineNum : rawLineIndex + 1, type: "elementcontent", value : chunk});
				}
			}
		}

		// Make editor table
		var editor = "<table>";
		editor += "<tr>";
		editor += "<td colspan='2'>Uploaded XML</td>";

		var currentLineNum = 0;
		var tokensIndex = 0;
		while(tokensIndex < tokens.length){
			currentLineNum = tokens[tokensIndex].lineNum;
			editor += "</tr>";
			editor += "<tr>";
			// Line Number
			if(currentLineNum == lineNumber){
				editor += "<td class='line_num_column error'><span>" + (currentLineNum) + "</span></td>";
				editor += "<td class='error'>";
			}else{
				editor += "<td class='line_num_column'><span>" + (currentLineNum) + "</span></td>";
				editor += "<td>";
			}

			// Line Content
			while(tokensIndex < tokens.length && currentLineNum == tokens[tokensIndex].lineNum){
				editor += "<span class='" + tokens[tokensIndex].type + "'>" + tokens[tokensIndex].value + "</span>";
				tokensIndex++;
			}
			editor += "</td>";

		}
		editor += "</tr>";

		editor += "</table>";
		$("#xml_editor_div").html(editor);
	}

$("#xml_textarea").val(
"<!-- This is a VisualOozie example. Click Validate -->\n"
+ "<workflow-app xmlns='uri:oozie:workflow:0.2' name='demo-wf'>\n"
+ "\n"
+ "    <start to='cleanup-node'/>\n"
+ "\n"
+ "    <action name='cleanup-node'>\n"
+ "        <fs>\n"
+ "            <delete path='${nameNode}/user/${wf:user()}/${examplesRoot}/output-data/demo'/>\n"
+ "        </fs>\n"
+ "        <ok to='fork-node'/>\n"
+ "        <error to='fail'/>\n"
+ "    </action>\n"
+ "\n"
+ "    <fork name='fork-node'>\n"
+ "        <path start='join-node'/>\n"
+ "        <path start='streaming-node'/>\n"
+ "    </fork>\n"
+ "\n"
+ "    <action name='streaming-node'>\n"
+ "        <map-reduce>\n"
+ "            <job-tracker>${jobTracker}</job-tracker>\n"
+ "            <name-node>${nameNode}</name-node>\n"
+ "            <prepare>\n"
+ "                <delete path='${nameNode}/user/${wf:user()}/${examplesRoot}/output-data/demo/streaming-node'/>\n"
+ "            </prepare>\n"
+ "            <streaming>\n"
+ "                <mapper>/bin/cat</mapper>\n"
+ "                <reducer>/usr/bin/wc</reducer>\n"
+ "            </streaming>\n"
+ "            <configuration>\n"
+ "                <property>\n"
+ "                    <name>mapred.job.queue.name</name>\n"
+ "                    <value>${queueName}</value>\n"
+ "                </property>\n"
+ "\n"
+ "                <property>\n"
+ "                    <name>mapred.input.dir</name>\n"
+ "                    <value>/user/${wf:user()}/${examplesRoot}/input-data/text</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.output.dir</name>\n"
+ "                    <value>/user/${wf:user()}/${examplesRoot}/output-data/demo/streaming-node</value>\n"
+ "                </property>\n"
+ "            </configuration>\n"
+ "        </map-reduce>\n"
+ "        <ok to='join-node'/>\n"
+ "        <error to='fail'/>\n"
+ "    </action>\n"
+ "\n"
+ "    <join name='join-node' to='mr-node'/>\n"
+ "    \n"
+ "    \n"
+ "    <action name='mr-node'>\n"
+ "        <map-reduce>\n"
+ "            <job-tracker>${jobTracker}</job-tracker>\n"
+ "            <name-node>${nameNode}</name-node>\n"
+ "            <prepare>\n"
+ "                <delete path='${nameNode}/user/${wf:user()}/${examplesRoot}/output-data/demo/mr-node'/>\n"
+ "            </prepare>\n"
+ "            <configuration>\n"
+ "                <property>\n"
+ "                    <name>mapred.job.queue.name</name>\n"
+ "                    <value>${queueName}</value>\n"
+ "                </property>\n"
+ "\n"
+ "                <property>\n"
+ "                    <name>mapred.mapper.class</name>\n"
+ "                    <value>org.apache.oozie.example.DemoMapper</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.mapoutput.key.class</name>\n"
+ "                    <value>org.apache.hadoop.io.Text</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.mapoutput.value.class</name>\n"
+ "                    <value>org.apache.hadoop.io.IntWritable</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.reducer.class</name>\n"
+ "                    <value>org.apache.oozie.example.DemoReducer</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.map.tasks</name>\n"
+ "                    <value>1</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.input.dir</name>\n"
+ "                    <value>/user/${wf:user()}/${examplesRoot}/output-data/demo/pig-node,/user/${wf:user()}/${examplesRoot}/output-data/demo/streaming-node</value>\n"
+ "                </property>\n"
+ "                <property>\n"
+ "                    <name>mapred.output.dir</name>\n"
+ "                    <value>/user/${wf:user()}/${examplesRoot}/output-data/demo/mr-node</value>\n"
+ "                </property>\n"
+ "            </configuration>\n"
+ "        </map-reduce>\n"
+ "        <ok to='decision-node'/>\n"
+ "        <error to='fail'/>\n"
+ "    </action>\n"
+ "\n"
+ "    <decision name='decision-node'>\n"
+ "        <switch>\n"
+ "            <case to='hdfs-node'>${fs:exists(concat(concat(concat(concat(concat(nameNode, \'/user/\'), wf:user()), \'/\'), examplesRoot), \'/output-data/demo/mr-node\')) == 'true'}</case>\n"
+ "            <default to='end'/>\n"
+ "        </switch>\n"
+ "    </decision>\n"
+ "\n"
+ "    <action name='hdfs-node'>\n"
+ "        <fs>\n"
+ "            <move source='${nameNode}/user/${wf:user()}/${examplesRoot}/output-data/demo/mr-node'\n"
+ "                  target='/user/${wf:user()}/${examplesRoot}/output-data/demo/final-data'/>\n"
+ "        </fs>\n"
+ "        <ok to='end'/>\n"
+ "        <error to='fail'/>\n"
+ "    </action>\n"
+ "\n"
+ "    <kill name='fail'>\n"
+ "        <message>Demo workflow failed, error message[${wf:errorMessage(wf:lastErrorNode())}]</message>\n"
+ "    </kill>\n"
+ "\n"
+ "    <end name='end'/>\n"
+ "\n"
+ "</workflow-app>");
	
	$("#fileSubmitButton").click(fileSubmitButtonClicked);
	$("#textSubmitButton").click(textSubmitButtonClicked);
	$("#startOverbutton").click(function(){
		location.reload();
	});
	$(window).resize(onWindowResize);

	var menuExpanded = true;
	function collapseMenu(){
		$("#menu_div").animate({
			height: "3em",
		}, 200 );
		$("#expand_img").removeClass("flip");
		menuExpanded = false;
	}

	$("#expand_img").click(function(){
		if(menuExpanded){
			collapseMenu();
		}else{
			$("#menu_div").css("height", "");
			$("#expand_img").addClass("flip");
			menuExpanded = true;
		}
	});

}); // End of JQuery Initialization



