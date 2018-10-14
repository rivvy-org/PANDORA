package ctandean;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;

//https://github.com/centic9/jgit-cookbook
//https://stackoverflow.com/questions/13586502/how-to-check-if-a-git-clone-has-been-done-already-with-jgit
//https://github.com/eclipse/jgit/commit/1f6d43a6528212f270f0f4542ee0cb54cd1a2435#diff-d6b7dae2cec0366c4d5ed0888eef5f4a
//https://stackoverflow.com/questions/50851277/how-to-pull-from-origin-using-jgit-on-existing-repository

public class GitCloneRepo
{

	private static String REMOTE_URL = "https://github.com/rivvy-org/EXTRACT.git";
	private static String CLONE_DIR = "S:\\GIT_WORK\\";
	private static String ORG_URL = "https://github.com/rivvy-org/";
	private static String REPO_NAME = "";
	private static String LOCAL_REPO_PATH = "";

	
	
	public static void main(String[] args) { 
		REPO_NAME = REMOTE_URL.replaceAll(ORG_URL, "").replaceAll(".git", "")+"\\";
		LOCAL_REPO_PATH = CLONE_DIR+REPO_NAME;
		//System.out.println(LOCAL_REPO_PATH);
	
		Git result = clone(REMOTE_URL, LOCAL_REPO_PATH,true);
		
		//System.out.println("Having repository: " + result.getRepository().getDirectory());
	  
		
	}
	
	/*
	 * Main method exposed to the public, this will find and clone a repository assuming one does not exists already
	 * If a local clone of a repository exist already, it will ascertain if the repository is valid.
	 * If local repository is not valid it will delete, clean up, and then re-clone
	 * If local repository  is valid it will force a hard reset to sync up with remote repository
	 * End result will be a exact mirror image of a remote repository
	 * Parameters:
	 * String remoteUrl - expects a fully qualified github clone url  ex: https://github.com/<organization>/<repository>.git
	 * String localDir - expects a local directory where you want to store this clone repository. Note the name includes the actual folder
	 * 					 of the repository as well ex: C:\<top level folder>\<repository name>\
	 * boolean bareClone - Set true if you want a full clone, set false for meta data (which is faster but no local files)
	 * Notes: Git reset may be cheaper than a full re-clone but without any refs/branches passed it it may not reset everything.
	 * 		  Bare clone may be quicker to clone and we traverse meta data only. But note a bare true will always force a no local repo found and force a re-clone
	 */
	public static Git clone(String remoteUrl, String localDir, boolean bareClone) {
		File localPath = new File(localDir);
		System.out.println("\n---------- Working on: " + remoteUrl +" ----------");
		Git result = null;
		try {
			
			
			if (localPath.exists())
	        {
				System.out.println("Dir already exists.");
				
				//just because a path exists doesn't mean the repository is there, check on this
				//if it is a repository check to see if it is malformed, if so delete and re-clone
				//if it is not malformed do a hard reset to make it match the remote repository
				//System.out.println(localDir+".git");
				if (RepositoryCache.FileKey.isGitRepository(new File(localDir+".git"), FS.DETECTED)) 
				{
					System.out.println("Repository exists at path "+localPath);
					//Repository is here but is it malformed? 
					result = Git.open(localPath);
					if (!hasAtLeastOneReference(result.getRepository())) {
						System.out.println("Repository at Dir " +localPath+ " is malformed! Will clean up and reclone.");
						//if this returns false the repository is malformed
						result.close(); //close the first open, reassign new Git object
						FileUtils.deleteDirectory(localPath);
						result = cloneRepo(result, remoteUrl, localPath, bareClone);
					} else {
						if(bareClone) {
							//want a bare repository? Force a cleanup and re-clone bare
							System.out.println("Reclone as bare repository");
							FileUtils.deleteDirectory(localPath);
							result = cloneRepo(result, remoteUrl, localPath, bareClone);	
							
						} else {
							//seems to exists, do a update. 
							System.out.println("Repository exists, doing a hard reset "+localPath);
							result.reset()
							.setMode (ResetType.HARD)
							.call();
						}	
					}
					//Repository exists locally and it not malformed, opened Git object is good at this point
	
				} else {
					//Folder structure exists but there is no repository here
					//We are unsure what is in here, might want to clean up
					System.out.println("No Repository at " +localPath+ " found. Will clean up and reclone.");
					FileUtils.deleteDirectory(localPath);
					result = cloneRepo(result, remoteUrl, localPath, bareClone);	
				}	
				
				
	        } else {
	        	System.out.println("Dir not exists.");
	        	localPath.mkdirs();
	        	result = cloneRepo(result, remoteUrl, localPath, bareClone);
	        }       
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
		} catch (IOException e) {
			System.out.print("Caught a IOException");
			e.printStackTrace();
		} catch (GitAPIException a) {
			System.out.print("Caught a GitAPIException");
			a.printStackTrace();
		} finally {
			
			System.out.println("---------- Finished with: " + remoteUrl +" ----------\n");
		}

		return result;
	}

	
	/*
	 * Takes a empty Git object and return a cloned repository
	 * Git repo - blank Git object that is returned as a repository once cloned
	 * String remoteUrl - The url of remote repository you want to clone
	 * String localPath - Location you want to clone the remote repository locally
	 * boolean bareClone - false for full clone, true for just meta data clone
	 */
	private static Git cloneRepo(Git repo, String remoteUrl, File localPath, boolean bareClone) {
		System.out.println("Cloning from " + remoteUrl+ " to " + localPath);
		try {	
	        repo = Git.cloneRepository()
	                .setURI(remoteUrl)
	                .setDirectory(localPath)
	                .setBare(bareClone)
	                .call();
	
		} catch (GitAPIException a) {
			System.out.print("Caught a GIT API Exception");
			a.printStackTrace();
        } /*finally {
        	if (repo != null)
        		repo.close();
        }*/
		
		return repo;
	}
	
	
	/*
	 * Check if repository has been initialized locally
	 * Takes in File object which is local repository location
	 */
	private static boolean repoDoesExist(File repoDir) {
		
		if(RepositoryCache.FileKey.isGitRepository(repoDir, FS.DETECTED)) 
		{
			return true;
		} else {
			return false;
		}
	}
	 
	/*
	 * Check if repository is malformed, it could be tampered with
	 */
	private static boolean hasAtLeastOneReference(Repository repo) throws IOException {
	    for (Ref ref : repo.getRefDatabase().getRefs()) {
	        if (ref.getObjectId() == null)
	            continue;
	        return true;
	    }

	    return false;
	}
		
}