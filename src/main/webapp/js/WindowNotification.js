
function WindowNotification(target){
	this.topPosition = 0;
	this.target = target;
	this.conf = null;
}

WindowNotification.NOTIFICATION_HEIGHT = 30;
WindowNotification.timeOut = null;
WindowNotification.showingStatus = null; // "showing" or "staying" or "closing" or "closed"

WindowNotification.prototype.showNotification =	function(msg, parameters){
	var defaultConf =
		{
			backgroundcolor: "lightblue" ,
			icon: "ui-icon-circle-check" ,
			display_time: 3000
		};

	this.conf = $.extend(
		defaultConf,
		parameters);
	
	if(WindowNotification.showingStatus == "showing"
			|| WindowNotification.showingStatus == "staying"
			|| WindowNotification.showingStatus == "closing"){
		clearTimeout(WindowNotification.timeOut);
		WindowNotification.showingStatus = "closed";
		$("#notificationDiv").remove();
	}
	

	$(this.target).append(
			"<div id='notificationDiv'" +
			"style='background-color: " + this.conf.backgroundcolor + "; " +
			"border-style: solid; " +
			"border-bottom-width: 1px; " +
			"border-right-width: 1px; " +
			"border-left-width: 1px; " +
			"border-top-width: 0px; " +
			"border-bottom-left-radius: 5px;" +
			"border-bottom-right-radius: 5px;" +
			"box-shadow: 0 0 5px 5px white;" +
			"position: fixed; " +
			"top: -" +
			WindowNotification.NOTIFICATION_HEIGHT + "px; " +
			"left: 50%;" +
			"z-index: 10;" +
			"padding-bottom: 5px;" +
			"padding-right: 15px;" +
			"padding-left: 15px;" +
			"margin: 0px;" +
			"'>" +
			"<span style='display:inline-block;' class='ui-icon " +
			this.conf.icon +
			"' title='ui-icon-lightbulb'>&nbsp;</span>" +
			" " +
			msg + "</div>");

	var width = $("#notificationDiv").width();
	$("#notificationDiv").css("margin-left", "-" + width/2 + "px");

	WindowNotification.showingStatus = "showing";
	this.timer();
	
};

WindowNotification.prototype.timer = function(){
	var _this = this;

	if(WindowNotification.showingStatus == "showing"){
		this.topPosition++;
		$("#notificationDiv").css("top", (this.topPosition - WindowNotification.NOTIFICATION_HEIGHT) + "px");
		if(this.topPosition > WindowNotification.NOTIFICATION_HEIGHT){
			WindowNotification.showingStatus = "staying";
		}
		WindowNotification.timeOut = setTimeout(function(){ _this.timer(); }, 10);
	}else if(WindowNotification.showingStatus == "staying"){
		WindowNotification.showingStatus = "closing";
		WindowNotification.timeOut = setTimeout(function(){ _this.timer(); }, this.conf.display_time);
	}else if(WindowNotification.showingStatus == "closing"){
		this.topPosition--;
		$("#notificationDiv").css("top", (this.topPosition - WindowNotification.NOTIFICATION_HEIGHT) + "px");
		if(this.topPosition < 0){
			WindowNotification.showingStatus = "closed";
		}
		WindowNotification.timeOut = setTimeout(function(){ _this.timer(); }, 10);
	}else{ // closed
		$("#notificationDiv").remove();
		clearTimeout(WindowNotification.timeOut);
	}

};


