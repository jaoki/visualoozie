
var jqDocument = $(document);
var jqWindow = $(window);

// --------------- Menu ---------------------------------------------------------------------------------
function Menu(){
}

Menu.HEIGHT = 46;

function UserConfiguration(){
	this.data = new Object();
	this.data.menu = new Object();
	this.data.menu.friendNameShown = true;

	this.data.mainPanel = new Object();
	this.data.mainPanel.height = 0;
	this.data.mainPanel.width = 0;
	this.data.panels = new Array();

}

UserConfiguration.prototype.save = function(){
	var postData = "userConfiguration.menu.friendNameShown=" + this.data.menu.friendNameShown;
	postData += "&userConfiguration.mainPanel.height=" + this.data.mainPanel.height;
	postData += "&userConfiguration.mainPanel.width=" + this.data.mainPanel.width;
	//postData += "&userConfiguration.panels[0].width=1";
	//postData += "&userConfiguration.panels[1].width=2";

	$.ajax({
		type: 'POST'
		, url: "SaveUserConfiguration"
		, data: postData
		, complete: function(jqXHR, textStatus) {
			var response = jQuery.parseJSON(jqXHR.responseText)
			if(response.result.code == "Success"){
				console.log("");
			}else if(response.result.code == "DbError"){
			}else if(response.result.code == "SessionExpired"){
				notification.showNotification("Reload the browser! (Http Session is expired)", { backgroundcolor: "LightGoldenRodYellow "});
			}
			

		}
	});

}

UserConfiguration.prototype.load = function(){
	var _this = this;
	$.ajax({
		type: 'GET'
		, url: "LoadUserConfiguration"
		, async : false
		, complete: function(jqXHR, textStatus) {
			var response = jQuery.parseJSON(jqXHR.responseText)
			if(response.result.code == "Success"){
				_this.data = response.userConfiguration;
				// some check
				// TODO when widht or height is too small, or not set
				// TODO (let's have minimum resizable check)
	
				if(_this.data.mainPanel == null){
					_this.data.mainPanel = { 
						width : jqWindow.width() - AdsArea.WIDTH
						, height : jqWindow.height() - MainPanel.SCROLLBAR_WIDTH - Menu.HEIGHT
					};
				}

				if(_this.data.mainPanel.height == null || _this.data.mainPanel.height < MainPanel.MINIMUM_HEIGHT){
					_this.data.mainPanel.height = MainPanel.MINIMUM_HEIGHT;
				}

				if(_this.data.mainPanel.width == null || _this.data.mainPanel.width < MainPanel.MINIMUM_WIDTH){
					_this.data.mainPanel.width = MainPanel.MINIMUM_WIDTH;
				}

				_this.activateConfiguration();

			}else if(response.result.code == "DbError"){
			}else if(response.result.code == "SessionExpired"){
				notification.showNotification("Reload the browser! (Http Session is expired)", { backgroundcolor: "LightGoldenRodYellow "});
			}
			
		}
	});

}

UserConfiguration.prototype.activateConfiguration = function(){
	if(this.data.menu.friendNameShown){
		$("#toggleNameCheckbox").prop("checked", true);
		$(".friend_photo_name_div").show();
	}else{
		$("#toggleNameCheckbox").prop("checked", false);
		$(".friend_photo_name_div").hide();
	}
}

UserConfiguration.prototype.getPanel = function(listId){
	var index;
	for(index = 0; index < this.data.panels.length; index++){
		if(this.data.panels[index].listId == listId){
			break;
		}
	}
	return this.data.panels[index];

}

UserConfiguration.prototype.resetMainPanelSize = function(){
	this.data.mainPanel.width = jqWindow.width() - AdsArea.WIDTH;
	this.data.mainPanel.height = jqWindow.height() - MainPanel.SCROLLBAR_WIDTH - Menu.HEIGHT;
	$("#main_panel")
			.width(this.data.mainPanel.width)
			.height(this.data.mainPanel.height);

	userConfig.save();

}



// --------------- Ads Area ---------------------------------------------------------------------------------
function AdsArea(){
}

AdsArea.WIDTH = 120;


// ---------------------------------------------- MainPanel ----------------------------------------------
function MainPanel(){
	this.allFriendsPanel = null;
	this.panels = new Array();
}

MainPanel.BACKGROUND_CSS_COLOR_COUNT = 9;
MainPanel.SCROLLBAR_WIDTH = 20;
MainPanel.MINIMUM_HEIGHT = 200;
MainPanel.MINIMUM_WIDTH = 200;

MainPanel.prototype.addAllFriendsPanel = function(allFriendsPanel){
	this.allFriendsPanel = allFriendsPanel;
};

MainPanel.prototype.addPanel = function(panel){
	this.panels.push(panel);
};

MainPanel.prototype.insert = function(index, panel){
	this.panels.splice(index, 0, panel);
};

MainPanel.prototype.getPanel = function(listId){
	var index;
	for(index = 0; index < this.panels.length; index++){
		if(this.panels[index].list.id == listId){
			break;
		}
	}
	return this.panels[index];
}

MainPanel.prototype.remove = function(listId){
	var index;
	for(index = 0; index < this.panels.length; index++){
		if(this.panels[index].list.id == listId){
			break;
		}
	}
	this.panels.splice(index, 1);
};

MainPanel.setDraggable = function(){
	$(".friend_photo_draggable_div").draggable({
		revert: 'invalid'
		, scroll: false
		, containment: "#main_panel"
		, helper: 'clone'
	}).css("z-index", 1)
	.hover(
		function(){
			$(this).addClass("friend_photo_draggable_div_mouse_over");
		}
		,function(){
			$(this).removeClass("friend_photo_draggable_div_mouse_over");
		}
	);

}

MainPanel.prototype.refresh = function(){
	var colorNumber = 0;
	var panelDivHtml = "";
	panelDivHtml += this.allFriendsPanel.getPanelDiv(colorNumber);
	colorNumber++;
	for(var panelIndex = 0; panelIndex < this.panels.length; panelIndex++){
		panelDivHtml += this.panels[panelIndex].getPanelDiv(colorNumber);
		if(colorNumber < MainPanel.BACKGROUND_CSS_COLOR_COUNT - 1)
			colorNumber++;
		else
			colorNumber = 0;
	}

	$("#main_panel_content").html(panelDivHtml);

	$("#main_panel")
			.width(userConfig.data.mainPanel.width)
			.height(userConfig.data.mainPanel.height);

	userConfig.activateConfiguration();

	$("#main_panel").resizable({
		minHeight: MainPanel.MINIMUM_HEIGHT,
		minWidth: MainPanel.MINIMUM_WIDTH,
		stop: function(event, ui){
			var _mainPanel = $(this);
			userConfig.data.mainPanel.height = _mainPanel.height();
			userConfig.data.mainPanel.width = _mainPanel.width();
			userConfig.save();
		}
	});


	$( ".panel_div").resizable({
		grid: Panel.IMG_DIV_SIZE
		, resize: function(event, ui){
			// the wrapper's size
			var height = $(this).height();
//			var width = $(this).width();

//			// Change the list title div
//			$(this).children(".panel_title_div").css("width", width + "px");
//			// Change the content div
			$(this).find(".panel_content_div").height((height - Panel.PANEL_TITLE_HEIGHT) + "px");
//			$(this).children(".panel_content_div").css("width", (width - (Panel.PANEL_CONTENT_PADDING * 2)) + "px");
		}
	});

	MainPanel.setDraggable();

	$( ".panelContentDivDroppable" ).droppable({ 
		accept: '.friend_photo_draggable_div',
		activeClass: "dragging",
		drop: function(event, ui) {
			// if the target is dropped in the same panel
			if($(this).attr("id") == $(ui.draggable).parent().attr("id"))
				return;

			var friendName = $(ui.draggable).find("img").attr("title");
			var friendId = $(ui.draggable).attr("friendId");
			var panelName = $(this).attr("listName");
			var listId = $(this).attr("listId");

			notification.showNotification("Adding " + friendName + " to " + panelName + " list... ", { backgroundcolor: "LightGoldenRodYellow "});

			// Add a friend to a list
			$.ajax({
				type: 'GET'
				// url: "https://graph.facebook.com/" + listId + "/members/" + friendId + "?" + $("#fbAccessToken").val(),
				, url: "AddFriendToList?listId=" + listId + "&friendId=" + friendId
				, success: function(data) {
					mainPanel.getPanel(listId).addMember({"id" : friendId, "name" : friendName});

					notification.showNotification(friendName + " has been added to " + panelName + " list", { backgroundcolor: "LawnGreen", icon: "ui-icon-circle-check"});
				}
				, error: function(data) {
					notification.showNotification("An error has been occured. " + friendName + " was not added to " + panelName + " list. Refesh the browser and try it again.", { backgroundcolor: "red", icon: "ui-icon-alert"});
				}
			});

		}
	});

	$(".panelDeleteIconButton" ).button({
		icons: {
			primary: "ui-icon-circle-close"
		}
		, text: false
	}).height("16px").width("16px");


};

// ---------------------------------------------- Panel ---------------------------------------------- 
// width can be "auto" or 800(px);
function Panel(list, members, width){
	this.list = list;
	this.members = members;
	this.width = width;

	this.PANEL_CONTENT_DEFAULT_HEIGHT = Panel.IMG_DIV_SIZE * 3;
	this.PANEL_DEFAULT_HEIGHT = this.PANEL_CONTENT_DEFAULT_HEIGHT + Panel.PANEL_TITLE_HEIGHT;

}
	
Panel.PANEL_CONTENT_PADDING = 3;
Panel.PANEL_TITLE_HEIGHT = 27;
Panel.IMG_SIZE = 50;
Panel.IMG_DIV_SIZE = Panel.IMG_SIZE + 8; // (boreder size + padding + margin) * 2

Panel.prototype.addMember = function(member){
	this.members.data.push(member);

	// Increment member count
	var memberCountSpan = $("#memberCount_" + this.list.id);
	var number = parseInt(memberCountSpan.text());
	memberCountSpan.text(number + 1);

	$("#panel_content_" + this.list.id).append(Panel.getPhotoDiv(member));
	MainPanel.setDraggable();
	userConfig.activateConfiguration();

}

Panel.getPhotoDiv = function(member){
	return "<div class='friend_photo_draggable_div' id='friend_" + member.id + "' friendId='" + member.id + "'>"
		+ "<div class='friend_photo_div'>"
			+ "<a href='https://www.facebook.com/" + member.id + "' target='_blank'>"
			+ "<img class='friend_photo' "
					+ "title=\"" + member.name + "\" "
					+ "src='https://graph.facebook.com/" + member.id + "/picture' />"
			+ "</a>"
			+ "<div class='friend_photo_name_div'>"
			+ member.name 
			+ "</div>"
		+ "</div>"
	+ "</div>"
	;
}

Panel.prototype.getPanelDiv = function(colorThemeNumber){
	var panelWidth;
	if(this.width == "auto"){
		var memberCount = this.members.data.length;
		if(memberCount <= 6){
			panelWidth = (Panel.IMG_DIV_SIZE * 2) + (Panel.PANEL_CONTENT_PADDING * 2) + MainPanel.SCROLLBAR_WIDTH;
		}else if(memberCount <= 18){
			panelWidth = (Panel.IMG_DIV_SIZE * Math.ceil(memberCount/3)) + (Panel.PANEL_CONTENT_PADDING * 2) + MainPanel.SCROLLBAR_WIDTH;
		}else{
			panelWidth = (Panel.IMG_DIV_SIZE * 6) + (Panel.PANEL_CONTENT_PADDING * 2) + MainPanel.SCROLLBAR_WIDTH;
		}
	}else{
		panelWidth = this.width;
	}
//	var panelContentWidth = panelWidth - (Panel.PANEL_CONTENT_PADDING * 2);

	var panelDivHtml = "<div class='panel_div panel_div_css" + colorThemeNumber + "'" 
			+ " style='height: " + this.PANEL_DEFAULT_HEIGHT + "px; width:" + panelWidth + "px;' " 
			+ " id='panel_div_" + this.list.id + "'"
			+ ">";

	panelDivHtml += "<table class='layout'>";
	panelDivHtml += "<tr class='panel_title_css" + colorThemeNumber + "'>";
	panelDivHtml += "<td class='layout'>";

	panelDivHtml += "<div class='panel_title_div'>";

	if(this.width == "auto") // NOT "auto" means so far only All Friends...
		panelDivHtml += "<a title='See Posts in " + this.list.name + " list on Facebook'href='https://www.facebook.com/home.php?sk=fl_" + this.list.id + "' target='_blank'>" + this.list.name + "</a>";
	else
		panelDivHtml += "<a title='See Posts of All Friends on Facebook' href='https://www.facebook.com' target='_blank'>All Friends</a>";

	panelDivHtml += "(<span id='memberCount_" + this.list.id + "' class='memberCount'>" + this.members.data.length + "</span>)"
			+ "</div>"; // end of panel_title_div


	panelDivHtml += "</td>";
	panelDivHtml += "<td class='layout'>";


	// Delete Panel Icon
	panelDivHtml += "<div class='panelDeleteIconDiv'>";
	if(this.list.list_type == "user_created"){
		panelDivHtml += "<button class='panelDeleteIconButton' onClick='deleteList(\"" + this.list.id + "\", \"" + this.list.name + "\")' type='button'>Delete " + this.list.name + " list</button>";
	}else{
		panelDivHtml += "&nbsp;";
	}
	panelDivHtml += "</div>"; // end of panelDeleteIconDiv

	panelDivHtml += "</td>";
	panelDivHtml += "</tr>";
	panelDivHtml += "<tr>";
	panelDivHtml += "<td  class='layout' colspan=2>";

	// this div is needed for jquery resizable + overflow: auto situation
	panelDivHtml += "<div class='panel_content_div ";

	// add an extra class for droppable
	if(this.width == "auto"){ // NOT "auto" means so far only All Friends...
		panelDivHtml += "panelContentDivDroppable ";
	}
	panelDivHtml += "' id='panel_content_" + this.list.id + "' " 
					+ " listName='" + this.list.name + "' "
					+ " listId='" + this.list.id + "' "
					+ "style='" 
					+ "height: " + this.PANEL_CONTENT_DEFAULT_HEIGHT + "px; "
					+ "'>";

	for(var memberIndex = 0; memberIndex < this.members.data.length; memberIndex++){
		var friend = this.members.data[memberIndex];
		panelDivHtml += Panel.getPhotoDiv(friend);
	}

	panelDivHtml +="</div>"; // end of panel_content_div

	panelDivHtml += "</td>";
	panelDivHtml += "</tr>";
	panelDivHtml += "</table>";

	panelDivHtml +="</div>"; // end of panel_div
	return panelDivHtml;
};

// --------------------------------------------------------------------------------------------
var mainPanel;
function drawPanels(){

	mainPanel = new MainPanel();
	// All Friends
	var listAll = new Object();
	listAll.name = "All Friends";
	listAll.id = "All";
	var allFriendsPanel = new Panel(listAll
			, allFriends
			, ($(document).width() - (Panel.PANEL_CONTENT_PADDING * 2) - MainPanel.SCROLLBAR_WIDTH - AdsArea.WIDTH)
			);
	mainPanel.addAllFriendsPanel(allFriendsPanel);

	// Each Friend List
	for(var listIndex = 0; listIndex < friendLists.data.length; listIndex++){
		// image loop
		var listId = friendLists.data[listIndex].id;
		var panel = new Panel(friendLists.data[listIndex], listMembers[listId], "auto");
		mainPanel.addPanel(panel);
	}

	mainPanel.refresh();

}

// --------------------------------------------------------------------------------------------
function hint(){
	$( "#help-message" ).dialog( "open" );
	$("#hint_image")
			.css("left", "0px")
			.animate({"left": "+=120px"}, 1500);
}

// --------------------------------------------------------------------------------------------
function addList(){
	var newListName = $("#newListTextbox").val();
	if(newListName == ""){
		notification.showNotification("Input a list name", { backgroundcolor: "red", icon: "ui-icon-alert"});
		return;
	}

	notification.showNotification("Creating " + newListName + " list... ", { backgroundcolor: "LightGoldenRodYellow "});
	$("#addListButton").attr("disabled", "disabled").css("background", "grey").css("cursor", "wait");
	
	$.ajax({
		type: 'POST'
		, url: "AddList"
		, data: "listName=" + newListName
		, complete: function(jqXHR, textStatus){
			$("#addListButton").prop("disabled", false).css("background", "").css("cursor", "pointer");
			var createdId = jQuery.parseJSON(jqXHR.responseText).newlyCreatedIlistId;
			if(createdId === "undefined")
				notification.showNotification("Failed. " + newListName + " was not created. Go to the support page.", { backgroundcolor: "red", icon: "ui-icon-alert"});
			else
				notification.showNotification(newListName + " list has been created", { backgroundcolor: "LawnGreen", icon: "ui-icon-circle-check"});

			var panel = new Panel(
					{ "id": createdId
							, "name":  newListName
							, "list_type": "user_created" 
					}
					, { "data": [ ] }
					, "auto"
			);
			mainPanel.insert(0, panel);
			mainPanel.refresh();
			$("#newListTextbox").val("");
			
		}
	});
}

// --------------------------------------------------------------------------------------------
function deleteList(listId, listName){
	var r = confirm("Are you sure you want to delete list " + listName + "?");
	if (r == false){
		return;
	}

	notification.showNotification("Deleing " + listName + " list... ", { backgroundcolor: "LightGoldenRodYellow "});

	$.ajax({
		type: 'GET'
		, url: "DeleteList?listId=" + listId
		, complete: function(jqXHR, textStatus){
			if(jqXHR.responseText == "false")
				notification.showNotification("Failed to delete " + listName + ". Go to the support page.", { backgroundcolor: "red", icon: "ui-icon-alert"});
			else
				notification.showNotification(listName + " list has been deleted", { backgroundcolor: "LawnGreen", icon: "ui-icon-circle-check"});

			mainPanel.remove(listId);
			mainPanel.refresh();
		}
	});
}

// --------------------------------------------------------------------------------------------

function toggleFriendNames(){
	if($("#toggleNameCheckbox").prop("checked")){
		$(".friend_photo_name_div").show();
		userConfig.data.menu.friendNameShown = true;
	}else{
		$(".friend_photo_name_div").hide();
		userConfig.data.menu.friendNameShown = false;
	}

	userConfig.save();

}

