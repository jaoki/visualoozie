package net.junaoki.fflo.action;

import java.io.IOException;

import net.junaoki.facebook.FacebookAPI;
import net.junaoki.facebook.FacebookOAuthException;
import net.junaoki.fflo.SessionObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
public class DeleteListAction extends ActionSupport{

	private static final long serialVersionUID = 2257840693482792162L;
	private String listId;

	@Override
	@Action(value="DeleteList"
		, results={
			@Result( 
				name=SUCCESS
				, type="json" 
				// , params = {"root", "newlyCreatedIlistId"}
			)
			// , @Result( name=LOGIN, type="redirectAction", location="ShowStartPage" )
	})
	public String execute() throws JsonMappingException, JsonParseException, IOException{
		SessionObject session = SessionObject.getSessionObject();
		FacebookAPI api = new FacebookAPI(session.getFacebookAccessToken());
		try{
			api.deleteList(listId);
//			ObjectMapper mapper = new ObjectMapper();
//			Map<String, Object> data = mapper.readValue(fbAPIResponse , Map.class);
//			newlyCreatedIlistId = (String)data.get("id");
		}catch(FacebookOAuthException e){
			return LOGIN;
		}

        return SUCCESS;
	}


	public void setListId(String listId) { this.listId = listId; }
	public String getListId() { return listId; }


}
