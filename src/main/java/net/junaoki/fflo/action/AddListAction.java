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
public class AddListAction extends ActionSupport{

	private static final long serialVersionUID = 2257840693482792162L;
	private String listName;
	private String newlyCreatedIlistId;

	@Override
	@Action(value="AddList"
		, results={
			@Result( 
				name=SUCCESS
				, type="json" 
				// , params = {"root", "newlyCreatedIlistId"}
			)
			// , @Result( name=LOGIN, type="redirectAction", location="ShowStartPage" )
	})
	public String execute() throws JsonMappingException, JsonParseException, IOException{
		final SessionObject session = SessionObject.getSessionObject();
		final FacebookAPI api = new FacebookAPI(session.getFacebookAccessToken());
		try{
			final String fbAPIResponse = api.addList(session.getUserId(), listName);
			final ObjectMapper mapper = new ObjectMapper();
			final Map<String, Object> data = mapper.readValue(fbAPIResponse , Map.class);
			newlyCreatedIlistId = (String)data.get("id");
		}catch(FacebookOAuthException e){
			return LOGIN;
		}

        return SUCCESS;
	}

	public void setListName(String listName) { this.listName = listName; }
	public String getListName() { return listName; }

	public void setNewlyCreatedIlistId(String newlyCreatedIlistId) { this.newlyCreatedIlistId = newlyCreatedIlistId; }
	public String getNewlyCreatedIlistId() { return newlyCreatedIlistId; }


}
