package net.junaoki.fflo;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;

public class SessionObject {

	public static final String SESSION_OBJECT_KEY = "SESSION_OBJECT_KEY";

	private String fbAccessToken = null;
//	private String lang = null;
	private String userId = null;
	
	private SessionObject(){}
	
	public synchronized static SessionObject getSessionObject(){
		Map<String, Object> session = ActionContext.getContext().getSession();
		SessionObject sessionObject;
		if(session.get(SessionObject.SESSION_OBJECT_KEY) == null){
			sessionObject = new SessionObject();
		}else{
			sessionObject = (SessionObject)session.get(SESSION_OBJECT_KEY);
		}

		return sessionObject;

	}

	public void save() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put(SESSION_OBJECT_KEY, this);
	}

	public void setFacebookAccessToken(String fbAccessToken) {
		this.fbAccessToken = fbAccessToken; 
		save();
	}
	public String getFacebookAccessToken() { return fbAccessToken; }

	public void setUserId(String userId) {
		this.userId = userId;
		save();
	}
	public String getUserId() { return userId; }

//	public void setLang(String lang) {
//		this.lang = lang; 
//		save();
//	}
//	public String getLang() { return lang; }

}
