package ctandean;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.diff.DiffEntry;

public class GitMainTest2 {
	
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
	    orgHash.remove("PANDORA");//for testing
	    orgHash.remove("GENERATION");//for testing 
	    
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
			
		    //ArrayList<CommitData> cd = GitGetData.listRepoContents(repo,"2018-09-13"); //test repo was create 9/23/2018, this should pull everything
			
			//for (CommitData cdloop: cd) {
			//	System.out.println(cdloop.toString());
			//}
	        
			
			System.out.println("Listing Branches");
			ArrayList<String> fqBranchNames = GitGetData.listBranchNames(repo,ListMode.ALL);
			
			for(String branches : fqBranchNames) {
				if(!branches.equalsIgnoreCase("refs/heads/master")) {
					//I don't care about thje master I want to use the master as a base of branch to campare to
					//then I only care about release branches
					if(branches.contains("refs/heads/Release_")) {
						
						ArrayList<DiffEntry> diff = GitGetData.listBranchDiff(repo, "refs/heads/master", branches);
						
						String branchMinRef = branches.replaceAll("refs/heads/", "");
						String exportDir = CLONE_DIR+"SVNEXPORT\\"+REPO_NAME+"_"+branchMinRef+"\\";
						
			            File expPath = new File(exportDir);
						if (!expPath.exists())
				        {
							FileUtils.deleteDirectory(expPath);
							expPath.mkdirs();
				        } else {
				        	expPath.mkdirs();
				        }
						
						//GitGetData.listBranchDiff(repo, "refs/heads/Release_20181015", "refs/heads/master");
				        for (DiffEntry entry : diff) {
				            System.out.println("Entry: " + entry
				            		+ " " + entry.getChangeType().toString()
				            		+ " " + entry.getOldPath()
				            		+ " " + entry.getNewPath()
				            		);
				            
				            String exportObj = orgObj.getSvnUrl()+"/branches/"+branchMinRef+"/"+entry.getNewPath();
				            
				            GitGetData.githubSvnExport(exportObj, expPath, token);

				        }//end diff entry loop
				        
					}
				}
	
			}
			
			//TESTING DIFFS
			//at this point we do have a release branch
			/*
			ArrayList<DiffEntry> diff = GitGetData.listBranchDiff(repo, "refs/heads/master", "refs/heads/Release_20181015");
			
			//GitGetData.listBranchDiff(repo, "refs/heads/Release_20181015", "refs/heads/master");
	        for (DiffEntry entry : diff) {
	            System.out.println("Entry: " + entry
	            		+ " " + entry.getChangeType().toString()
	            		+ " " + entry.getOldPath()
	            		+ " " + entry.getNewPath()
	            		);
	            
	            String exportObj = orgObj.getSvnUrl()+"/branches/"+"Release_20181015"+"/"+entry.getNewPath();
	            GitGetData.githubSvnExport(exportObj, CLONE_DIR, token);

	        }
			*/
			

			
			
			
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		


	}
	
	
	
	
}