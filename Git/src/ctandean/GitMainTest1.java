package ctandean;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

public class GitMainTest1 {
	
	private static String REMOTE_URL = "https://github.com/rivvy-org/EXTRACT.git";
	private static String CLONE_DIR = "S:\\GIT_WORK\\";
	private static String ORG_URL = "https://github.com/rivvy-org/";
	private static String REPO_NAME = "";
	private static String LOCAL_REPO_PATH = "";
	
	private static String token;
	//organization should be known, you can pass this as a String parameter
	private static String org = "https://api.github.com/orgs/rivvy-org/repos?access_token=";
	
	public static void main(String[] args) throws IOException, GitAPIException, JGitInternalException, ParseException { 
		//will use gitRepo.java  to play with extract repo
		//REPO_NAME = REMOTE_URL.replaceAll(ORG_URL, "").replaceAll(".git", "")+"\\";
		//LOCAL_REPO_PATH = CLONE_DIR+REPO_NAME;
		//System.out.println(LOCAL_REPO_PATH);
		token = args[0];
		
	    HashMap orgHash = GitGetOrgRepos.getOrgList(org, token);
	    Iterator it = orgHash.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String repoName = (String) pair.getKey();
	        Organization orgObj = ((Organization)pair.getValue());
	        //System.out.println("Organization: " + orgObj.owner.getLogin()+" @ "+orgObj.owner.getHtmlUrl()
	        //					+ " Repository: " + orgObj.getName() + " id/node_id: "+orgObj.getId()+"/"+orgObj.getNodeId() + " clone_url: "+orgObj.getCloneUrl());
	        
	        REMOTE_URL = orgObj.getCloneUrl();
	        //ORG_URL = orgObj.owner.getHtmlUrl()+"/";
			REPO_NAME = orgObj.getName();
			LOCAL_REPO_PATH = CLONE_DIR+REPO_NAME+"\\";
	        
			//once a repo is cloned/reset
			//attempt top walk all the commits on the master
			Git repo = GitCloneRepo.clone(REMOTE_URL, LOCAL_REPO_PATH,true);
	        
			ArrayList<CommitData> cd = GitGetData.listRepoContents(repo,"2018-09-01"); //test repo was create 9/23/2018, this should pull everything
			
			for (CommitData cdloop: cd) {
				System.out.println(cdloop.toString());
			}
			
			
			
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		


	}
	
	
	
	
}