package ctandean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Purpose of this class is to retrieve a hashmap of Organizational-Repository subject of data based of a Github ReST endpoint
 */
public class GitGetOrgRepos {
	
	//set token as a parameter, generate one from Github please
	private static String token;
	//organization should be known, you can pass this as a String parameter
	private static String org = "https://api.github.com/orgs/rivvy-org/repos?access_token=";
	
	public static void main(String[] args) { 

	    token = args[0];
	    //ObjectMapper  objectMapper = new ObjectMapper();
	    //objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	    //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
	    
	    HashMap orgHash = getOrgList(org, token);
	    Iterator it = orgHash.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String repoName = (String) pair.getKey();
	        Organization orgObj = ((Organization)pair.getValue());
	        System.out.println("Organization: " + orgObj.owner.getLogin()+" @ "+orgObj.owner.getHtmlUrl()
	        					+ " Repository: " + orgObj.getName() + " id/node_id: "+orgObj.getId()+"/"+orgObj.getNodeId() + " clone_url: "+orgObj.getCloneUrl());
	        it.remove(); // avoids a ConcurrentModificationException
	    }

	}
	
	/*
	 * Method to hit up a ReST endpoint of a Github site and return back a json data for repositories in a organization
	 * Can be expanded.
	 * Parameters: 
	 *    String org - a String representing a Github repository endpoint url 
	 *    	EX: https://api.github.com/orgs/(organization name)/repos?access_token=
	 * Returns:
	 * 		Hashmap with String key representing Repository name and Reposiutory objects parsed with POJO class Organization
	 */
	public static HashMap<String,Organization> getOrgList(String org, String token) {
		HashMap<String,Organization> orgHash = new HashMap<String,Organization>();
		try { 
		    URL obj = new URL(org+token);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		    con.setRequestMethod("GET");
		    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);	
			}
			String data = builder.toString();
			
			ObjectMapper  objectMapper = new ObjectMapper();
			List<Organization> list =  objectMapper.readValue(data, new TypeReference<List<Organization>>(){});
			
			for (int i=0;i<list.size();i++) {
				//System.out.println("Org id " + list.get(i).owner.getId() + " Repo id " + list.get(i).getId()); 
				orgHash.put(list.get(i).getName(), list.get(i));
			}

		} catch (IOException e) {
			System.out.print("error");
			e.printStackTrace();
		}

		return orgHash;
	}
	
	
}