package net.junaoki.fflo.persistent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

public class MongoDBAccess {
	public static final String DB_NAME = "list_organizer";
	public static final String COLLECTION_NAME = "user_configuration";
	
	public static final String KEY_USER_ID = "userId";
	public static final String KEY_LAST_UPDATE = "lastUpdate";
//	public static final String KEY_MENU = "menu";
//	public static final String KEY_MENU_FRIEND_NAME_SHOWN = "friendNameShown";

    private static Mongo mongo;
    private DB db;

    static{
	    MongoURI uri = new MongoURI("mongodb://localhost");
		try {
			mongo = new Mongo(uri);
		} catch (MongoException e) {
			mongo = null;
			e.printStackTrace();
		} catch (UnknownHostException e) {
			mongo = null;
			e.printStackTrace();
		}
    }

    public MongoDBAccess() {
	    db = mongo.getDB(DB_NAME);
    }

    private BasicDBObject parse(Object data){
    	
		BasicDBObject dbobj = new BasicDBObject();
    	
    	for(Method method : data.getClass().getDeclaredMethods()){
    		if(method.getName().startsWith("get")){
    			String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4); // to get getMenu to menu
    			Object value;
    			try {
					value = method.invoke(data, (Object[])null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return null;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return null;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					return null;
				}
				
				if(value instanceof String){
					dbobj.put(key, (String)value);
				}else if(value instanceof Integer){
					dbobj.put(key, (Integer)value);
				}else if(value instanceof Boolean){
					dbobj.put(key, (Boolean)value);
				}else if(value == null){
						dbobj.put(key, null);
				}else if(value instanceof List<?>){
					ArrayList parsedObjects = new ArrayList();
					for(Object nonListValue : (List<?>)value){
						BasicDBObject parsedDBObj = parse(nonListValue);
						parsedObjects.add(parsedDBObj);
					}
					dbobj.put(key, parsedObjects);
				}else{
					BasicDBObject child = parse(value);
					dbobj.put(key, child);
				}
    		}
    	}

    	return dbobj;

    }

    public void saveUserConfiguration(UserConfiguration userConf) throws FFLOMongoDbException{
		DBCollection configCollection = db.getCollection(COLLECTION_NAME);

		BasicDBObject dbobj = parse(userConf);
		dbobj.put(KEY_LAST_UPDATE, new Date());

		DBObject recordKey = new BasicDBObject();
		recordKey.put(KEY_USER_ID, userConf.getUserId());
		try{
			configCollection.update(recordKey, dbobj, true, false);
		}catch(MongoException e){
			throw new FFLOMongoDbException();
		}
	
    }

    private Object parse(Class<?> clazz, DBObject dbobj){

		Object instance;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}

		Iterator<String> keys = dbobj.keySet().iterator();
		while(keys.hasNext()){
			String dbobjKey = keys.next();
			Object dbobjValue = dbobj.get(dbobjKey);
			Object value;
			Class<?> parameterType;
			if(dbobjValue instanceof ObjectId){
				// do nothing
				continue;
			}else if(dbobjValue instanceof BasicDBList){
				parameterType = List.class;
				ArrayList list = new ArrayList();
				BasicDBList dbList = (BasicDBList)dbobjValue;
				for(int i = 0; i < dbList.size(); i++){
					DBObject dbObject = (DBObject)dbList.get(i);
					Class<?> dbObjectParamType;
					try {
						dbObjectParamType = Class.forName("net.junaoki.fflo.persistent." + dbobjKey.substring(0, 1).toUpperCase() + dbobjKey.substring(1, dbobjKey.length() - 1)); // panels -> net.junaoki.fflo.persistent.Panel
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return null;
					}
					list.add(parse(dbObjectParamType, dbObject));
				
				}
				value = list;
			}else if(dbobjValue instanceof DBObject){
				try {
					parameterType = Class.forName("net.junaoki.fflo.persistent." + dbobjKey.substring(0, 1).toUpperCase() + dbobjKey.substring(1)); // menu -> net.junaoki.fflo.persistent.Menu
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
					return null;
				}

				value = parse(parameterType, (BasicDBObject)dbobjValue);

			}else if(dbobjValue == null){
				value = null;
				parameterType = null;
			}else{ // String, Integer etc.
				value = dbobjValue;
				if(dbobjValue instanceof Integer)
					parameterType = Integer.TYPE;
				else if(dbobjValue instanceof Boolean)
					parameterType = Boolean.TYPE;
				else if(dbobjValue instanceof Double)
					parameterType = Double.TYPE;
				else
					parameterType = dbobjValue.getClass();
			}

			Method method;
			try {
				String methodName = "set" + dbobjKey.substring(0, 1).toUpperCase() + dbobjKey.substring(1); // menu -> setMenu
				method = clazz.getDeclaredMethod(methodName, parameterType);
			} catch (SecurityException e1) {
				e1.printStackTrace();
				return null;
			} catch (NoSuchMethodException e1) {
				method = null;
			}

			if(method != null){
				try {
				method.invoke(instance, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
	    			

		}
		
		return instance;
    	
    }

    public UserConfiguration getUserConfiguration(String userId) throws FFLOMongoDbException{
		DBCollection configCollection = db.getCollection("user_configuration");
		DBObject recordKey = new BasicDBObject();
		recordKey.put(KEY_USER_ID, userId);
		DBObject dbobj;
		try{
			dbobj = configCollection.findOne(recordKey);
		}catch(MongoException e){
			throw new FFLOMongoDbException();
	    }

		UserConfiguration userConfig = (UserConfiguration)parse(UserConfiguration.class, dbobj);
		
		return userConfig;
    	
    }
}
