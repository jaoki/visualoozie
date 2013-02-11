package net.junaoki.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class FacebookAPI {
	final private HttpClient client;
	final private String fbAccessToken;
	// private enum Method{GET, POST, DELETE};
	
	public FacebookAPI(String fbAccessToken){
		client = new DefaultHttpClient();
		this.fbAccessToken = fbAccessToken;
	}
	
	public String getMe() throws FacebookOAuthException{
		return callAPI(new HttpGet("https://graph.facebook.com/me?" + fbAccessToken));
	}

	public String getFriends() throws FacebookOAuthException{
		return callAPI(new HttpGet("https://graph.facebook.com/me/friends?" + fbAccessToken));
	}

	public String getFriendLists() throws FacebookOAuthException{
		return callAPI(new HttpGet("https://graph.facebook.com/me/friendlists?" + fbAccessToken));
	}

	public String getListMembers(String listId) throws FacebookOAuthException{
		return callAPI(new HttpGet("https://graph.facebook.com/" + listId + "/members?" + fbAccessToken));
	}

	public String addList(String userid, String listName) throws FacebookOAuthException{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name", listName));
        HttpPost post = new HttpPost("https://graph.facebook.com/" + userid + "/friendlists/?" + fbAccessToken);
        try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {}
		return callAPI(post);
	}

	public String deleteList(String listId) throws FacebookOAuthException{
        HttpGet request = new HttpGet("https://graph.facebook.com/" + listId + "?method=DELETE&" + fbAccessToken);
		return callAPI(request);
	}

	public String addFriendToList(String friendId, String listId) throws FacebookOAuthException{
        HttpPost request = new HttpPost("https://graph.facebook.com/" + listId + "/members/" + friendId + "?" + fbAccessToken);
		return callAPI(request);
	}

	private String callAPI(HttpUriRequest httpRequest) throws FacebookOAuthException{

		StringBuffer responseContent = new StringBuffer();

		InputStream instream = null;
	    try {
			HttpResponse response = client.execute(httpRequest);
			HttpEntity entity = response.getEntity();
	
			if (entity != null) {
					instream = entity.getContent();
			        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			        // do something useful with the response
			        String line;
			        while ( (line = reader.readLine() ) != null ) {
			            responseContent.append(line);
			        }                        
			        
			        reader.close();
	
			    // When HttpClient instance is no longer needed, shut down the connection manager to ensure immediate deallocation of all system resources
			    // client.getConnectionManager().shutdown();
			}
		
		} catch (IOException ex) {
			// In case of an IOException the connection will be released back to the connection manager automatically
//		} catch (RuntimeException ex) {
//	        // In case of an unexpected exception you may want to abort the HTTP request in order to shut down the underlying connection and release it back to the connection manager.
//	        httpget.abort();
	    } finally {
			// Closing the input stream will trigger connection release
	    	if(instream != null){
				try { instream.close(); } catch (IOException e) {}
	    	}
	    }
		
		if(responseContent.toString().contains("\"type\":\"OAuthException\"")){
			throw new FacebookOAuthException();
		}

		return responseContent.toString();
		
	}

}
