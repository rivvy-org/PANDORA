package ctandean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

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

public class listOrgRepo1 
{
	private static String token;
	private static String curlLoc =  "F:\\curl-7.61.1-win64-mingw\\bin\\";
	private static String org = "https://api.github.com/orgs/rivvy-org/repos?access_token=";
	private static String org2 = "https://api.github.com/rivvy-org?access_token=";
	private static String org3 = "https://api.github.com/users/rivvy-org";
	private static ArrayList<String> cloneUrl = new ArrayList<String>();
	
	
	public static void main(String[] args) { 

    token = args[0];
	
	//NOTE: (location of curl)curl (options for curl, used -s) https://api.github.com/orgs/[organization]/repos?access_token=[youroauth token]
    ProcessBuilder process = new ProcessBuilder(
    		curlLoc+"curl",
            "-s",
            org+token);
            
	Process p;
	try {
		p = process.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
			
			//filter our only clone url lines
			if(line.contains("clone_url"))
			{
				cloneUrl.add(line);
			}
		}
		
		String result = builder.toString();
		System.out.print(result);
		
		for(int i=0;i<cloneUrl.size();i++)
		{
			String[] bSplit = cloneUrl.get(i).split("\": \"");
			String b1 = bSplit[0].replaceAll("\"", "");
			String b2 = bSplit[1].replaceAll("\",", "");
			
			System.out.println(b2);
		}
	

	} catch (IOException e) {
		System.out.print("error");
		e.printStackTrace();
	}
	
		
	
	}
}//end class