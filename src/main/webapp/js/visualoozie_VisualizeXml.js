$(function() {

	var submitButtonClicked = function(){
		$("#xml_editor_div").html("");
		$("#workflow_diagram").html("");
		$("#xml_editor_pre").html("");
		$("#errorMessage").html("");
		$("#waiting_image").show();

		var formData = new FormData($("#fileform")[0]);
		$.ajax({
//			url: vo.contextRoot + "api/upload_xml"
			url: "/api/upload_xml"
			, type: 'POST'
			, data: formData
			, cache: false
			, contentType: false
			, processData: false
			, complete: function(jqXHR, textStatus){
				var res = $.parseJSON(jqXHR.responseText);
				drawXmlEditor(res.xml, res.lineNumber, res.columnNumber);
				$("#span_identifiedNamespace").html(res.identifiedNamespace);

				if(!res.succeeded){
					$("#errorMessage").html(res.errorMessage);
					return;
				}
				$("#waiting_image").hide();
				drawDiagram(res);

			}
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
		for(var i = 0; i < sortedNodeNames.length; i++){
			// how many same sort order there are?
			var sameSortCount = 1
			for(; i + sameSortCount < sortedNodeNames.length ;sameSortCount++){
				if(sortedNodeNames[i].sortOrder != sortedNodeNames[i + sameSortCount].sortOrder){
					break;
				}
			}


			for(var j = 0; j < sameSortCount; j++){
				var node;
				// Add some random number to fluctuate x-position
				var xPos = (editorWidth/(sameSortCount + 1) * (j+1)) + Math.floor((Math.random()*30)+1);
				if(weightedNodes[sortedNodeNames[i + j].name].type == "KILL"){
					node = new wfjs.CircleNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name, { stroke: "red", fill: "red" });
				}else if(weightedNodes[sortedNodeNames[i + j].name].type == "DECISION"){
					node = new wfjs.DiamondNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name);
				}else if(weightedNodes[sortedNodeNames[i + j].name].type == "START" || weightedNodes[sortedNodeNames[i + j].name].type == "END"){
					node = new wfjs.CircleNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name, { stroke: "grey", fill: "grey" });
				}else{
					node = new wfjs.CircleNode(canvas1, xPos, yPos, sortedNodeNames[i + j].name, { stroke: "blue", fill: "yellow" });
				}


				// TODO fix this
				if(node.show !== undefined)
					node.show();



				weightedNodes[sortedNodeNames[i + j].name].node = node;

				if(j > 0){
					i++;
				}
			}

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

	$("#submitButton").click(submitButtonClicked);
	$("#startOverbutton").click(function(){
		location.reload();
	});
	$(window).resize(onWindowResize);

}); // End of JQuery Initialization



