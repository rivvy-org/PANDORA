package ctandean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;






//Try to list all organization inside a github repository
	
//https://askubuntu.com/questions/976145/shell-script-to-clone-every-repo-of-a-github-organization
//https://stackoverflow.com/questions/2594880/using-curl-with-a-username-and-password
//https://techrevel.blog/2018/01/19/curl-execution-from-java-program/
	
//https://api.github.com/orgs/[organization]/repos?access_token=[access_token]
//tutorial on curl with github
//https://gist.github.com/caspyin/2288960
//https://stackoverflow.com/questions/32080488/how-to-get-specific-repository-info-from-github-url-in-php
//https://stackoverflow.com/questions/2586975/how-to-use-curl-in-java
//https://stackoverflow.com/questions/24053634/using-curl-command-in-java

//https://developer.github.com/enterprise/2.14/v3/
//https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
//https://stackoverflow.com/questions/18440098/org-json-simple-jsonarray-cannot-be-cast-to-org-json-simple-jsonobject/26677603
//https://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial

//using json testing
public class listOrgRepo2 
{
	private static String token;
	private static String curlLoc =  "F:\\curl-7.61.1-win64-mingw\\bin\\";
	private static String org = "https://api.github.com/orgs/rivvy-org/repos?access_token=";
	private static String org2 = "https://api.github.com/rivvy-org?access_token=";
	private static String org3 = "https://api.github.com/users/rivvy-org";
	private static ArrayList<String> cloneUrl = new ArrayList<String>();
	
	
	public static void main(String[] args) { 

    token = args[0];
    ObjectMapper  objectMapper = new ObjectMapper();
   


    
	try { 
	    
	    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(org+token).openStream()));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);	
		}
		String data = builder.toString();
	    

		
		// parsing from git? https://stackoverflow.com/questions/23479332/import-json-url-to-java-and-parse-it-using-jackson-library
		//https://github.com/coolaj86/json-examples/tree/master/java/jackson
		// https://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial
		// https://stackoverflow.com/questions/5018340/error-converting-json-string-to-map-in-java-using-jackson

		//Map<String,String> myMap = new HashMap<String, String>();
		List myMap = objectMapper.readValue(data, List.class);
		
		//System.out.println("Map is: "+myMap);
		
		JsonNode rootNode = objectMapper.readTree(data);
		JsonNode cloneUrl = rootNode.path("clone_url");
		System.out.println("id = "+cloneUrl.asInt());
		
		Iterator<JsonNode> elements = cloneUrl.elements();
		while(elements.hasNext()){
			JsonNode cu1 = elements.next();
			System.out.println("Phone No = "+cu1.asLong());
		}
		
		

	} catch (IOException e) {
		System.out.print("error");
		e.printStackTrace();
	}
}
	
		
	
}//end class