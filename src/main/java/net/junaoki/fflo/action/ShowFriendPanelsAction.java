package net.junaoki.fflo.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.junaoki.facebook.FacebookAPI;
import net.junaoki.facebook.FacebookOAuthException;
import net.junaoki.fflo.SessionObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.opensymphony.xwork2.ActionSupport;

public class ShowFriendPanelsAction extends ActionSupport{

	private static final long serialVersionUID = -1151449876801253161L;
	private String fbAccessToken;
	private String me;
	private String friends;
	private String friendLists;
	private List<String> friendListMembersJS;

	@Override
	@Action(value="ShowFriendPanels"
		, results={
			@Result( name=LOGIN, type="redirectAction", location="ShowStartPage" )
			, @Result( name=SUCCESS, location="/jsp/FriendPanels.jsp" )
	})
	public String execute() throws JsonMappingException, JsonParseException, IOException{
		final SessionObject session = SessionObject.getSessionObject();
		session.setFacebookAccessToken(fbAccessToken);

		final FacebookAPI api = new FacebookAPI(fbAccessToken);
		try{
			me = api.getMe();
			final ObjectMapper mapper = new ObjectMapper();
			final Map<String, Object> meMap = mapper.readValue(me, Map.class);
			session.setUserId((String)meMap.get("id"));
			
			friends = api.getFriends();
			friendLists = api.getFriendLists();
	
			final Map<String, Object> data = mapper.readValue(friendLists, Map.class);
			final ArrayList<Object> dataElements = (ArrayList)data.get("data");
	
			friendListMembersJS = new ArrayList<String>();
			for(int i = 0; i < dataElements.size(); i++){
				final Map<String, Object> list = ((Map<String, Object>)dataElements.get(i));
				friendListMembersJS.add("listMembers['" + list.get("id") + "'] = " 
						+ api.getListMembers((String)list.get("id")));
			}
//			System.out.println();
			
		}catch(FacebookOAuthException e){
			return LOGIN;
		}

        return SUCCESS;
	}

	public void setFbAccessToken(String fbAccessToken) { this.fbAccessToken = fbAccessToken; }
	public String getFbAccessToken() { return fbAccessToken; }

	public void setMe(String me) { this.me = me; }
	public String getMe() { return me; }

	public void setFriends(String friends) { this.friends = friends; }
	public String getFriends() { return friends; }

	public void setFriendLists(String friendLists) { this.friendLists = friendLists; }
	public String getFriendLists() { return friendLists; }

	public List<String> getFriendListMembersJS() { return friendListMembersJS; }
	public void setFriendListMembersJS(List<String> friendListMembersJS) { this.friendListMembersJS = friendListMembersJS; }

}
