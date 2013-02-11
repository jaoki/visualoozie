package net.junaoki.fflo.action;

import java.io.IOException;
import java.util.Map;

import net.junaoki.facebook.FacebookAPI;
import net.junaoki.facebook.FacebookOAuthException;
import net.junaoki.fflo.SessionObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
public class AddFriendToListAction extends ActionSupport{
	private static final long serialVersionUID = 1284807500996202882L;
	
	private String friendId;
	private String listId;

	@Override
	@Action(value="AddFriendToList"
		, results={
			@Result( 
				name=SUCCESS
				, type="json" 
				// , params = {"root", "newlyCreatedIlistId"}
			)
			// , @Result( name=LOGIN, type="redirectAction", location="ShowStartPage" )
	})
	public String execute() {
		final SessionObject session = SessionObject.getSessionObject();
		final FacebookAPI api = new FacebookAPI(session.getFacebookAccessToken());
		try{
			api.addFriendToList(friendId, listId);
		}catch(FacebookOAuthException e){
			return LOGIN;
		}

        return SUCCESS;
	}

	public void setListId(String listId) { this.listId = listId; }
	public String getListId() { return listId; }

	public void setFriendId(String friendId) { this.friendId = friendId; }
	public String getFriendId() { return friendId; }


}
