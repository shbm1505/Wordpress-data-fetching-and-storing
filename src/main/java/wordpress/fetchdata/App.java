package wordpress.fetchdata;


/**
 * Hello world!
 *
 */
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;









//import org.json.*;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.SimpleQueryParser.Settings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class App {
 
	public static void main(String[] args) throws ParseException, org.json.simple.parser.ParseException {
	//System.out.println("2000 Hahahah");
		//	MongoClient mongoClient = new MongoClient();
		// or
	//	MongoClient mongoClient = new MongoClient( "localhost" );
		// or
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
	//	MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
		//                                      new ServerAddress("localhost", 27018),
		 //                                     new ServerAddress("localhost", 27019)));

		DB db = mongoClient.getDB( "mydb" );
		Set<String> collectionNames = db.getCollectionNames();
        for (final String s : collectionNames) {
            System.out.println(s);
        }
        DBCollection coll = db.getCollection("testCollection");

        // drop all the data in it
        coll.drop();

		String s=callURL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in");
		JSONParser parser = new JSONParser();
        Object obj = parser.parse(s);
        JSONObject jsonObject = (JSONObject) obj;
        Long id=(Long) jsonObject.get("ID");
        System.out.println(id);
        String url=(String) jsonObject.get("URL");
        System.out.println(url);
        Boolean jetpack=(Boolean) jsonObject.get("jetpack");
        Long subscribers_count=(Long) jsonObject.get("subscribers_count");
        Boolean is_private=(Boolean) jsonObject.get("is_private");
        Boolean is_following=(Boolean) jsonObject.get("is_following");
        
        JSONObject icon = (JSONObject) jsonObject.get("icon"); 
        String img=(String) icon.get("img");
        String ico=(String) icon.get("ico");
        JSONObject meta = (JSONObject) jsonObject.get("meta"); 
        JSONObject links = (JSONObject) meta.get("links"); 
        String self=(String) jsonObject.get("self");
        String help=(String) jsonObject.get("help");
        String posts=(String) jsonObject.get("posts");
        String comments=(String) jsonObject.get("comments");
        String xmlrpc=(String) jsonObject.get("xmlrpc");
        
        BasicDBObject doc = new BasicDBObject("ID", id)
        .append("URL", "url")
        .append("subscribers_count", subscribers_count)
        .append("is_private", is_private)
        .append("is_following", is_following)
        .append("icon", new BasicDBObject("img", img).append("ico", ico))
        .append("meta", new BasicDBObject("links", new BasicDBObject("self",self).append("help", "help")).append("posts", posts).append("comments", "comments").append("xmlrpc", xmlrpc));
        coll.insert(doc);
        
        DBObject myDoc = coll.findOne();
        System.out.println(myDoc);
        
		 
		String s2 =call2URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/posts/");
		org.elasticsearch.common.settings.Settings settings = ImmutableSettings
				.settingsBuilder()
				.put("cluster.name","elasticsearch1")
				.build();
		
		
		
	//@SuppressWarnings("resource")
	TransportClient transportClient = new TransportClient(settings);
		
	//	Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		
		
	 transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost",9300));
		
		
		 BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
	//	 Map<String,Object> source = new HashMap<String,Object>();
		
		Map<String, Object> json = new HashMap<String, Object>();
	//	json.put("user","kimchy");
	//	json.put("postDate",new Date());
	//	json.put("message","trying out Elasticsearch");
	 
		
		JSONParser parser2 = new JSONParser();
        Object obj2;
		
			obj2 = parser2.parse(s2);
		
		
		
        JSONObject jsonObject2 = (JSONObject) obj2;
		
        JSONArray postss=(JSONArray) jsonObject2.get("posts");
        JSONObject mjo = new JSONObject();
        for (int i = 0; i < postss.size(); i++) {
            mjo = (JSONObject) postss.get(i);
            
            Long idd = (Long) mjo.get("ID");
            String iddd=String.valueOf(idd);
        //    json.put("idd",idd);
      
         String date = (String) mjo.get("date");
         json.put("date",date);
         String modified = (String) mjo.get("modified");
     	json.put("modified",modified);
         String title = (String) mjo.get("title");
     	json.put("title",title);
         String urll=(String) mjo.get("URL");
     	json.put("urll",urll);
       	 String content=(String) mjo.get("content");
     	json.put("content",content);
       	 String excerpt=(String) mjo.get("excerpt");
     	json.put("excerpt",excerpt);
       	 String slug=(String) mjo.get("slug");
     	json.put("slug",slug);
       	 String status=(String) mjo.get("status");
     	json.put("status",status);
       	 String type=(String) mjo.get("type");
     	json.put("type",type);
       	 Boolean likes_enabled=(Boolean) mjo.get("likes_enabled");
     	json.put("likes_enabled",likes_enabled);
       	 Boolean sharing_enabled=(Boolean) mjo.get("sharing_enabled");
       	json.put("sharing_enabled",sharing_enabled);
       	 Long like_count=(Long) mjo.get("like_count");
       	json.put("like_count",like_count);
       	 Long i_like=(Long) mjo.get("i_like");
       	json.put("i_like",i_like);
       
        
		 	
       	
       	 Long is_reblogged=(Long) mjo.get("is_reblogged");
       	json.put("is_reblogged",is_reblogged);
       	 Long is_followingg=(Long) mjo.get("is_following");
       	json.put("is_followingg",is_followingg);
         JSONObject author = (JSONObject) mjo.get("author");
         String login=(String) author.get("login");
         json.put("login",login);
         String name=(String) author.get("name");
         json.put("name",name);
         String urlll=(String) author.get("login");
         json.put("urlll",urlll);
     //    System.out.println(login);
         
         JSONObject discussion = (JSONObject) mjo.get("discussion");
         Boolean comments_open=(Boolean) discussion.get("comments_open");
         json.put("comments_open",comments_open);
         String comment_status=(String) discussion.get("comment_status");
         json.put("comment_status",comment_status);
         Boolean ping_open=(Boolean) discussion.get("ping_open");
         json.put("ping_open",ping_open);
         String ping_status=(String) discussion.get("ping_status");
         json.put("ping_status",ping_status);
         Long comment_count=(Long) discussion.get("comment_count");
         json.put("comment_count",comment_count);
         
     //    JSONObject post_thumbnail = (JSONObject) mjo.get("post_thumbnail");
      //   String urllll=(String) post_thumbnail.get("URL");
       //  json.put("urllll",urllll);
    	 JSONObject tags = (JSONObject) mjo.get("tags");
    	 Set keys = tags.keySet();
    	 //int j=1;
    	    Iterator a = keys.iterator();
    	    while(a.hasNext()) {
    	    	String key = (String)a.next();
    	        //System.out.println(key);
    	    	json.put(key,key);
    	    	//i++;
    	
    	    }
    	
    	    JSONObject categories = (JSONObject) mjo.get("tags");
       	    Set keys2 = categories.keySet();
       	    Iterator a2 = keys2.iterator();
       	    while(a.hasNext()) {
       	    	String key2 = (String)a2.next();
       	        //System.out.println(key2);
       	    	json.put(key2,key2);
       	
       	    }
       	    
       	 JSONObject attachments = (JSONObject) mjo.get("attachments");
    	    Set keys3 = attachments.keySet();
    	    Iterator a3 = keys3.iterator();
    	        while(a3.hasNext()) {
    	    	String key3 = (String)a3.next();
    	    	json.put(key3,key3);
    	    	 
    	    }
    	        bulkRequest.add(
    					transportClient
    					.prepareIndex("hello", "heya",iddd)
    				    .setSource(json));
    	        if(bulkRequest.request().requests().size() == 0){
    	    		
    	    		System.out.println("\n\n No request Added!");
    	    	
    	    	} else{
    	    		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
    	    		
    	    		if (bulkResponse.hasFailures()) {
    	    		    System.out.println("ElasticSearch Failures: \n"+bulkResponse.buildFailureMessage());
    	    		}
    	    		
    	    	}    
    	   

    	    

         //System.out.println(login);
         
         
         
}             
     
     //   Integer ss = (int) (long) id2;
       // System.out.println(ss);
        /*for(int i=0;i<postss.length();i++)
        {
        	//JSONObject postss = (JSONObject) jsonObject2;
        	 JSONObject innerObj = postss.getJSONObject(i);
        	 String date=(String) postss.get("date");
        	 String modified=(String) postss.get("modified");
        	 String title=(String) postss.get("title");
        	 String urll=(String) postss.get("URL");
        	 String content=(String) postss.get("content");
        	 String excerpt=(String) postss.get("excerpt");
        	 String slug=(String) postss.get("slug");
        	 String status=(String) postss.get("status");
        	 String type=(String) postss.get("type");
        	 Boolean likes_enabled=(Boolean) postss.get("likes_enabled");
        	 Boolean sharing_enabled=(Boolean) postss.get("sharing_enabled");
        	 Long like_count=(Long) postss.get("like_count");
        	 Long i_like=(Long) postss.get("i_like");
        	 Long is_reblogged=(Long) postss.get("is_reblogged");
        	 Long is_followingg=(Long) postss.get("is_following");*/
        //	 String slug=(String) postss.get("slug");
        //	 String slug=(String) postss.get("slug");
        //	 String slug=(String) postss.get("slug");
        	// String slug=(String) postss.get("slug");
        	 
        	// JSONObject author = (JSONObject) postss;JSONObject innerObject = outerObject.getJSONObject("highlighting");

    /*    	 JSONObject author = (JSONObject) postss.get("posts"); 
        	 String login=(String) author.get("login");
        	 String name=(String) author.get("name");
        	 String urlll=(String) author.get("URL");
        	 
        	 JSONObject tags = (JSONObject) postss.get("tags"); 
        	 for(Object key : tags.keySet()) {         
        		  //  JSONObject o =(JSONObject)tags.jsonObject.get(key.toString());
        		   // String[] elementNames = JSONObject.getNames(o);
        		 for(Object keys : tags.keySet()) {         
        			    JSONObject o =(JSONObject)tags.get(keys.toString());
        			    System.out.println("json object----------"+o);*/
        			 /*   String[] elementNames = JSONObject.getNames(o);
        			    System.out.printf("%s >> %d ELEMENT(S) IN CURRENT OBJECT:\n", key, elementNames.length);
        			    for (String elementName : elementNames) {
        			        System.out.println(elementName);
        			        String value = o.get(elementName).toString();
        			        System.out.printf("name=%s, value=%s\n", elementName, value);
        			    }
        			    System.out.println(); 
        			   */    
        			}
        
        
		
	//	TransportClient transportClient = new TransportClient();
		
		//Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		
		
//	 transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost",9300));		
		
	//	 BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        	
	/*	System.out.println(call3URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/posts/78"));
		System.out.println(call4URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/posts/78/likes/"));
		System.out.println(call5URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/comments/"));
		System.out.println(call6URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/comments/68"));
		System.out.println(call7URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/comments/68/likes/"));
		System.out.println(call8URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/categories/slug:ecommerce"));
		System.out.println(call9URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/categories"));
		System.out.println(call10URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/tags"));
		System.out.println(call11URL("https://public-api.wordpress.com/rest/v1.1/sites/blog.prophesee.in/tags/slug:aam-aadmi-party"));
*/	
		
		

 
	public static String callURL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call2URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
/*	public static String call3URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call4URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call5URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call6URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call7URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call8URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call9URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call10URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	public static String call11URL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}*/
	
}
