package net.junaoki.fflo.action;

import java.net.UnknownHostException;

import net.junaoki.fflo.SessionObject;
import net.junaoki.fflo.action.AjaxResponseResult.ResponseResultEnum;

import net.junaoki.fflo.persistent.FFLOMongoDbException;
import net.junaoki.fflo.persistent.MongoDBAccess;
import net.junaoki.fflo.persistent.UserConfiguration;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
public class UserConfigurationActions extends ActionSupport{
	
	private static final long serialVersionUID = 1L;

	private UserConfiguration userConfiguration;
	private AjaxResponseResult result = new AjaxResponseResult();

	@Action(value = "SaveUserConfiguration"
		, results={
			@Result( 
				name = SUCCESS
				, type = "json" 
			)
			, @Result( 
				name = ERROR
				, type = "json" 
			)
	})
	public String save() {

		final SessionObject session = SessionObject.getSessionObject();

		if(session.getUserId() == null){
			result.setCode(ResponseResultEnum.SessionExpired);
			return ERROR;
		}
		userConfiguration.setUserId(session.getUserId());
		
		MongoDBAccess db = new MongoDBAccess();
		try {
			db.saveUserConfiguration(userConfiguration);
		} catch (FFLOMongoDbException e) {
			result.setCode(ResponseResultEnum.DbError);
			return ERROR;
		}
		

		result.setCode(ResponseResultEnum.Success);
        return SUCCESS;
	}

	@Action(value = "LoadUserConfiguration"
		, results={
			@Result( 
				name = SUCCESS
				, type = "json" 
//				, params = {"root", "userConfiguration"}
			)
			, @Result( 
				name = ERROR
				, type = "json" 
			)
	})
	public String get() {
		final SessionObject session = SessionObject.getSessionObject();
		if(session.getUserId() == null){
			result.setCode(ResponseResultEnum.SessionExpired);
			return ERROR;
		}

		MongoDBAccess db = new MongoDBAccess();
		try {
			userConfiguration = db.getUserConfiguration(session.getUserId());
		} catch (FFLOMongoDbException e) {
			result.setCode(ResponseResultEnum.DbError);
			return ERROR;
		}

		result.setCode(ResponseResultEnum.Success);
        return SUCCESS;
	}

	public void setUserConfiguration(UserConfiguration userConfiguration) { this.userConfiguration = userConfiguration; }
	public UserConfiguration getUserConfiguration() { return userConfiguration; }

	public void setResult(AjaxResponseResult result) { this.result = result; }
	public AjaxResponseResult getResult() { return result; }


}
