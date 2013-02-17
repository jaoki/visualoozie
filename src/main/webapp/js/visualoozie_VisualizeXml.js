$(function() {
	vo.tokenizer("abc");

	var submitButtonClicked = function(){
		$("#workflow_diagram").html("");
		$("#xml_editor_pre").html("");
		$("#errorMessage").html("");

		var formData = new FormData($("#fileform")[0]);
		$.ajax({
//			url: '<s:url value="/api/upload_xml"/>'
			url: vo.contextRoot + "api/upload_xml"
			, type: 'POST'
			, data: formData
			, complete: function(jqXHR, textStatus){
				var res = $.parseJSON(jqXHR.responseText);
//				$("#xml_editor_pre").html(res.escapedXml);
				drawXmlEditor(res.escapedXml, res.lineNumber, res.columnNumber);
//				SyntaxHighlighter.autoloader( 'xml js/shBrushXml.js'); // This needs to be loaded at this point.
//				SyntaxHighlighter.all();

				if(!res.succeeded){
					$("#errorMessage").html(res.errorMessage);
					return;
				}
				drawDiagram(res);

			}
			, cache: false
			, contentType: false
			, processData: false
		});
	};

	var weightedNodes = {};

	var PAPER_WITH = 500;

	function drawDiagram(res){
		Joint.paper("workflow_diagram", PAPER_WITH, 500);
		var fsa = Joint.dia.fsa;
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

		var allCircles = [];
		var yPos = 40;
		// Generates svg circles
		for(var i = 0; i < sortedNodeNames.length; i++){
			// how many same sort order there are?
			var sameSortCount = 1
			for(; i + sameSortCount < sortedNodeNames.length ;sameSortCount++){
				if(sortedNodeNames[i].sortOrder != sortedNodeNames[i + sameSortCount].sortOrder){
					break;
				}
			}

			for(var j = 0; j < sameSortCount; j++){
				var attrs;
				if(weightedNodes[sortedNodeNames[i + j].name].type == "KILL"){
					attrs= { fill : "red" };
				}else if(weightedNodes[sortedNodeNames[i + j].name].type == "START" || weightedNodes[sortedNodeNames[i + j].name].type == "END"){
					attrs= { fill : "grey" };
				}else{
					attrs= {};
				}
				var circle = fsa.State.create({
							position: {x: (PAPER_WITH/(sameSortCount + 1) * (j+1)), y: yPos}
							, label: sortedNodeNames[i + j].name
							, attrs : attrs
						});
				allCircles.push(circle);
				weightedNodes[sortedNodeNames[i + j].name].circle = circle;

				if(j > 0){
					i++;
				}
			}

			yPos += 80;

		}

		// Add svg lines
		for(var key in weightedNodes){	
			var node = weightedNodes[key];
			for(var i = 0; i < node.to.length; i++){
				node.circle.joint(weightedNodes[node.to[i]].circle, fsa.arrow).registerForever(allCircles);
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
		var editor = "<table>";
		editor += "<tr>";
		editor += "<td>#</td>";
		editor += "<td>Content</td>";
		editor += "</tr>";
		for(var i = 0; i <  xmlArray.length; i++){
			var line = xmlArray[i].replace(/ /g, "&nbsp;").replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
			editor += "<tr>";
			if(i == lineNumber - 1){
				editor += "<td><span class='red num'>" + (i + 1) + "</span></td>";
				editor += "<td><span class='red'>" + line + "</span></td>";
			}else{
				editor += "<td><span class='num'>" + (i + 1) + "</span></td>";
				editor += "<td><span>" + line + "</span></td>";
			}
			editor += "</tr>";
		}
		editor += "</table>";
		$("#xml_editor_div code").html(editor);
	}

	$("#submitButton").click(submitButtonClicked);

}); // End of JQuery Initialization



