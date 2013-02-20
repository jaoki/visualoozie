var wfjs = (function () {
var wfjs1 = {};

var SVGNS = "http://www.w3.org/2000/svg";

var TEXT_DEFAULT_OPTIONS = {
	fill : "black",
	"font-size" : "1em",
};

var draggingTarget = null;

var _onMouseDown = function(e){
	draggingTarget = e.target;
	e.preventDefault();
};


wfjs1.Canvas = (function () {
    function Canvas(containerId) {

		var _this = this;
		var _onMouseUp = function(e){
			if(draggingTarget != null){
				var targetId = draggingTarget.getAttribute("id");
				var node = _this.nodes.getById(targetId);
				node.move(e.offsetX, e.offsetY);
				draggingTarget = null;
			}
		};

        this.containerId = containerId;
		this.container = document.getElementById(containerId);

		var svgElement = document.createElementNS(SVGNS, "svg");
		svgElement.setAttribute("id", "wfjs_svg");
		svgElement.setAttribute("version", "1.1");
		svgElement.addEventListener("mouseup", _onMouseUp, false);
		this.container.appendChild(svgElement);
		this.svgElement = svgElement;

		this.nodes = [];
		this.nodes.getById = function(id){
			for(var i = 0; i < this.length; i++){
				if(this[i].id == id){
					return this[i];
				}
			}
			return null;
		}
    }

    return Canvas;
})();

wfjs1.Node = (function () {
	var index = 0; 

    function Node(canvas, x, y, label, circle_options, text_options) {
		this.id = "wfjs_node_" + index;
		index++;
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.label = label;
        this.circle_options = circle_options;
        this.text_options = text_options;
    };

    Node.prototype.show = function () {
//		var textId = "wfjs_circle_" + this.circles.length + "_text";
		var textId = "wfjs_circle__text";

		this.circleElement = document.createElementNS(SVGNS, "circle");
		this.circleElement.setAttribute("id", this.id);

		for(var attr in this.circle_options){
			this.circleElement.setAttribute(attr, this.circle_options[attr]);
		}
		this.circleElement.setAttribute("cx", this.x);
		this.circleElement.setAttribute("cy", this.y);
		this.circleElement.addEventListener("mousedown", _onMouseDown, false);
		this.canvas.svgElement.appendChild(this.circleElement);
        
		var textElement = document.createElementNS(SVGNS, "text");

		textElement.setAttribute("id", textId);
		textElement.textContent = this.label;

		for(var attr in TEXT_DEFAULT_OPTIONS){
			textElement.setAttribute(attr, TEXT_DEFAULT_OPTIONS[attr]);
		}

		if(this.text_options !=null && this.text_options !== undefined){
			for(var attr in this.text_options){
				textElement.setAttribute(attr, this.text_options[attr]);
			}
		}

		this.canvas.svgElement.appendChild(textElement);
		var rect = textElement.getBBox();
		textElement.setAttribute("x", this.x - (rect.width/2));
		textElement.setAttribute("y", this.y);

		this.labelTextElement = textElement;

		this.canvas.nodes.push(this);

    }; // End of show()

    Node.prototype.move = function(x, y) {
		this.circleElement.setAttribute("cx", x);
		this.circleElement.setAttribute("cy", y);
		var rect = this.labelTextElement.getBBox();
		this.labelTextElement.setAttribute("x", x - (rect.width/2));
		this.labelTextElement.setAttribute("y", y);

    }; // End of move()

    return Node;
})();

return wfjs1;
})();


