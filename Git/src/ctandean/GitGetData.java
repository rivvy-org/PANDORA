package ctandean;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.util.SVNEncodingUtil;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/*
 * This is a class meant to contain methods that return data from a repository whether local or remote
 * Use this as a tool box and add any new methods deemed necessary 
 * Some good references:
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/WalkAllCommits.java
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ListBranches.java
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/api/WalkRev.java
 * https://stackoverflow.com/questions/12927163/jgit-checkout-a-remote-branch
 * https://stackoverflow.com/questions/45587631/how-to-checkout-a-remote-branch-without-knowing-if-it-exists-locally-in-jgit
 * https://stackoverflow.com/questions/27993576/how-do-i-use-filters-in-jgit
 * https://github.com/eclipse/jgit/commit/1f6d43a6528212f270f0f4542ee0cb54cd1a2435#diff-89b3d87f8c852e16e57e7a5b35117e56 getAllRefs deprecated
 * https://stackoverflow.com/questions/47086943/how-to-find-the-branch-for-a-commit-with-jgit
 * https://stackoverflow.com/questions/28897658/how-do-you-get-the-name-of-a-git-tag-with-the-jgit-api
 * https://stackoverflow.com/questions/2479348/is-it-possible-to-get-identical-sha1-hash
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowBranchDiff.java
 * https://gist.github.com/cliffdarling/2360866
 */

public class GitGetData {
	private static String REMOTE_URL = "https://github.com/rivvy-org/EXTRACT.git";
	private static String CLONE_DIR = "S:\\GIT_WORK\\";
	private static String ORG_URL = "https://github.com/rivvy-org/";
	private static String REPO_NAME = "";
	private static String LOCAL_REPO_PATH = "";
	
	
	public static void main(String[] args) throws IOException, GitAPIException, JGitInternalException, ParseException { 
		//will use gitRepo.java  to play with extract repo
		REPO_NAME = REMOTE_URL.replaceAll(ORG_URL, "").replaceAll(".git", "")+"\\";
		LOCAL_REPO_PATH = CLONE_DIR+REPO_NAME;
		//System.out.println(LOCAL_REPO_PATH);
		
		//once a repo is cloned/reset
		//attempt top walk all the commits on the master
		Git repo = GitCloneRepo.clone(REMOTE_URL, LOCAL_REPO_PATH,true);
		
		//listCommits(repo);

		//System.out.println("Listing Branches");
		//listBranchNames(repo,ListMode.ALL);
		
		//iterateCommits(repo, "refs/remotes/origin/branch_20181012");
		//iterateCommits(repo, "refs/remotes/origin/master");
		
		//get every commit in this repo
		
		ArrayList<CommitData> cd = listRepoContents(repo,"2018-09-13");
		
		for (CommitData cdloop: cd) {
			System.out.println(cdloop.toString());
		}
		
		
		//System.out.println(getBranchName(repo, "c617bc281ad1756dac45638cebdc6bac129fba1d", "")) ;
		
		//try to get branch name per commit id
		/*
		Map<ObjectId, String> map = repo
				  .nameRev()
				  //.addPrefix( "refs/heads" )
				  .add( ObjectId.fromString( "abae8f0e78d4786815ab2d318cffc56f052366dc" ) )
				  .call();
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    */
		
	}
	
	/*
	 * List all log commits in a given repository for all branches. Basically a git log.
	 * Can be expanded.
	 * Parameters: 
	 * 	Git object
	 * Returns:
	 * 	Nothing
	 */
	public static void listCommits(Git repo) throws IOException,GitAPIException {

        Iterable<RevCommit> commits;
		commits = repo.log().all().call(); //get all log
        int count = 0;
        for (RevCommit commit : commits) {
            count++;
  
        }
        System.out.println(count);
	}
	
	/*
	 * Method used to retrieve a branch name given a SHA-1 commit id
	 * Note: If cloning bare repository remote will not exists, use local refs/heads
	 * Can be expanded.
	 * Parameters: 
	 * 	Git object
	 *  String id - represents a String of the SHA-1 commit id
	 *  String refType - can be 3 things:
	 *  	refs/heads - search local branches
	 *  	refs/remote - search branches on the remote repository
	 *  	refs/tags - search tags
	 * Returns:
	 * 	String branch name 
	 * 		Note: Branch name  normally looks like master or master~2.
	 * 		The second master~2 means this is on the master branch non head commit 2 behind the current head
	 * 		This method will parse out the ~ and number, we only care about the actual branch name
	 */
	public static String getBranchName(Git repo, String id, String refType) throws GitAPIException, MissingObjectException, JGitInternalException {
		String branchName = "";
		Map<ObjectId, String> map = repo
				  .nameRev()
				  .addPrefix( refType )
				  .add( ObjectId.fromString( id ))
				  .call();
		
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        String[] nameformat = ((String) pair.getValue()).split("~");
	        branchName = nameformat[0];
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return branchName;
	}
	
	/*
	 * List commits in a repository for a particular branch.
	 * Can be expanded.
	 * Parameters: 
	 * 	Git object
	 * 	String branchName - name of a given branch in repository
	 * 		EX: local refs/heads/master , remote refs/remotes/origin/master
	 * Returns:
	 * 	Nothing
	 */	
	public static void iterateCommits(Git repo, String branchName) throws IOException {
        try (Repository repository = repo.getRepository()) {
            Ref head = repository.exactRef(branchName);

            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(head.getObjectId());
                System.out.println("Start-Commit: " + commit);

                System.out.println("Walking all commits starting at HEAD");
                walk.markStart(commit);
                int count = 0;
                for (RevCommit rev : walk) {
                    System.out.println("Commit: " + rev);
                    count++;
                }
                System.out.println(count);

                walk.dispose();
            }
        }	
	}
	
	/*
	 * Method to returns a collection of strings representing branches in a repository 
	 * Can be expanded.
	 * Parameters: 
	 * 	Git object
	 *  ListMode - Object that is a enumeration.
	 *  	ListMode.ALL - returns all branches local and remote
	 *  	ListMode.REMOTE - returns only remote branches
	 * 	String branchName - name of a given branch in repository
	 * 		EX: local refs/heads/master , remote refs/remotes/origin/master
	 * Returns:
	 * 	ArrayList if Strings representing branch names fully qualified
	 * 	EX: local refs/heads/master , remote refs/remotes/origin/master
	 */	
	public static ArrayList<String> listBranchNames(Git git, ListMode m ) throws IOException,GitAPIException {
		ArrayList<String> retList = new ArrayList<String>();
        System.out.println("Listing local branches:\n");
        List<Ref> call = git.branchList().setListMode(m).call();
        for (Ref ref : call) {
            System.out.println("Branch: " + ref.getObjectId().getName() + " " + ref.getTarget().getName());
            retList.add(ref.getTarget().getName());
   
            }
		return retList;
	}
	
	/*
	 * Template method why which listBranchNames(Git git, ListMode m ) was based on. Prints a list simple branch information locally or remotely
	 * Can be expanded.
	 * Parameters: 
	 * 	Git object
	 * Returns:
	 * 	nothing
	 */	
	public static void listBranches(Git repo) {
        try (Repository repository = repo.getRepository()) {
            System.out.println("Listing local branches:");
            try (Git git = new Git(repository)) {
                List<Ref> call = git.branchList().call();
                for (Ref ref : call) {
                    System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
                }

                System.out.println("Now including remote branches:");
                call = git.branchList().setListMode(ListMode.ALL).call();
                for (Ref ref : call) {
                    System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
                    System.out.println("Test: " + ref.getName());
                }
            } catch (GitAPIException a) {
    			System.out.print("Caught a GitAPIException");
    			a.printStackTrace();
    		}
        } 
	}
	
	/*
	 * List out repository contents and will walk over all commits
	 * 
	 * Can be expanded. Current thoughts: add filters to limit by commit date, return Array list, Use object class CommitData to facilitate storage?
	 * Parameters: 
	 * 	Git object
	 * String strDate - this is a date string in the format YYYY-MM-DD EX: 2018-01-20 year month day
	 * 		If string is not set as is blank string, the date filter is not used. Using the date filter only retrieves commits after said date
	 * 		You can use various kinds of filters, this can be expanded to not only use date, but for our purposes this is the most useful
	 * Returns:
	 * 	ArrayList of CommitData objects - CommitData object contains data pertaining to a particular git commit in a repository
	 */	
	public static ArrayList<CommitData> listRepoContents(Git git) throws JGitInternalException, IOException, GitAPIException, ParseException {
		//overloaded method to force a blank date for no filter use if preferred
		return listRepoContents(git, "");
	}
	
	public static ArrayList<CommitData> listRepoContents(Git git, String strDate) throws IOException, JGitInternalException, GitAPIException, ParseException {
		ArrayList<CommitData> comd = new ArrayList<CommitData>();
		Repository repository = git.getRepository();
		//Collection<Ref> allRefs = repository.getAllRefs().values(); <- old way, deprecated now in jgit
		Collection<Ref> allRefs = repository.getRefDatabase().getRefs();
		
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		Date date = simpleDateFormat.parse(strDate);
		
		RevFilter filter = CommitTimeRevFilter.after(date);
				
        // a RevWalk allows to walk over commits based on some filtering that is defined
		RevWalk revWalk = new RevWalk(repository);
		if(!strDate.isEmpty())
			revWalk.setRevFilter(filter); 
		
        for( Ref ref : allRefs ) {
        	//System.out.println("Branch: " + ref );
            revWalk.markStart( revWalk.parseCommit( ref.getObjectId() ));
        }
        System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
       //walk.setRevFilter(newFilter);
        int count = 0;
        for( RevCommit commit : revWalk ) {
            //System.out.println("\nCommit: " + commit);
            RevTree tree = commit.getTree();
            //System.out.println("Having tree: " + tree);
            // now use a TreeWalk to iterate over all files in the Tree recursively
            // you can set Filters to narrow down the results if needed
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {		
            CommitData cd = new CommitData(commit.getId().getName(),getBranchName(git, commit.getId().getName(), "refs/heads"),
            			treeWalk.getPathString(), treeWalk.getNameString(), commit.getAuthorIdent().getName(), 
            			commit.getCommitterIdent().getName(), commit.getShortMessage(), commit.getFullMessage(), 
            			convertEpoch(commit.getCommitTime()), treeWalk.getPathLength(), treeWalk.getObjectId(0).getName());
            comd.add(cd);
    
            /*
            System.out.println("found: " + getBranchName(git, commit.getId().getName(), "refs/heads") + " path: " + treeWalk.getPathString() + " name: "+treeWalk.getNameString() 
            + "\n author: "+commit.getAuthorIdent().getName() + " commiter: " +commit.getCommitterIdent().getName()
            + "\n short msg: "+commit.getShortMessage()+" long msg: "+commit.getFullMessage() 
            + "\n commit time: " + convertEpoch(commit.getCommitTime()) + " path length: "+treeWalk.getPathLength() +" depth: "+treeWalk.getDepth()
            + "\n Object id: "+ treeWalk.getObjectId(0).getName());
            //note objectID(0) from a tree file is the file sh1 hash
        	*/
            }
            count++;
        }
        //System.out.println("Had " + count + " commits");
		return comd;
	}
	
	/*
	 * Deprecated version of listRepoContents. Use listRepoContents(Git git) 
	 * Was using this to play around traversing a specific branches. 
	 */
	private static void listRepoContents(Git git, Ref ref) throws IOException {
		Repository repository = git.getRepository();
		
		//Ref head = repository.exactRef(branch);

        // a RevWalk allows to walk over commits based on some filtering that is defined
        RevWalk walk = new RevWalk(repository);
        //walk.setRevFilter(newFilter);
        RevCommit commit = walk.parseCommit(ref.getObjectId());
        RevTree tree = commit.getTree();
        //System.out.println("Having tree: " + tree);

        // now use a TreeWalk to iterate over all files in the Tree recursively
        // you can set Filters to narrow down the results if needed
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        while (treeWalk.next()) {
        	
            System.out.println("found: " + treeWalk.getPathString() + " name: "+treeWalk.getNameString() 
            + "\n author: "+commit.getAuthorIdent().getName() + " commiter: " +commit.getCommitterIdent().getName()
            + "\n short msg: "+commit.getShortMessage()+" long msg: "+commit.getFullMessage() 
            + "\n commit time: " + convertEpoch(commit.getCommitTime()) + " path length: "+treeWalk.getPathLength() +" depth: "+treeWalk.getDepth());
    
        }
    }
	
	/*
	 * Convert a epoch date into actual readable date of format yyyy-MM-dd HH:mm:ss
	 * Parameters: 
	 * 	int epoch number 
	 * Returns:
	 * 	String date looks like yyyy-MM-dd HH:mm:ss 4 digit year, 2 digit month, 2 digit day, hours(24 format)/mins/secs
	 */	
	public static String convertEpoch(int epoch) {
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String formattedDate = dateTime.format(formatter);
		//System.out.println(formattedDate); // Tuesday,November 1,2011 12:00,AM
		return formattedDate;
		
	}
	
	/*
	 * Copy of git cookbook:
	 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowBranchDiff.java
	 * Parameters:
	 * 	Repository object
	 * String branch name
	 * Returns:
	 * 	a AbtractTreeIterator. 
	 */
    public static AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        Ref head = repository.exactRef(ref);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }
    
    /*
     * Finds all differences given 2 branches in a repository
     * Parameters: 
     * 	Git git - git object
     *  String oldBranch - This can be used to reference the branch you want to compare too
     *  String newBranch - This can be used to reference the branch you are making changes to
     * Returns:
     *	ArrayList of type DiffEntry, DiffEntry can return a few things useful but the 3 suggested are:
     *	1 the changes time ex: MODIFY, ADD, DELETE use getChangeType()
     *	2 the old branch path of the change	use getOldPath()
     *	3 the new branch path of the change	use getNewPath()
     *	Note: If such a path does not exist a output of /dev/null will be used instead.
     *	EX: ADD /dev/null ENEX0005_new_release.sql 
     *		or MODIFY ENEX0001_MP_TEST1.sql ENEX0001_MP_TEST1.sql 
     *		or DELETE ENEX0005_new_release.sql /dev/null
     */
    public static ArrayList<DiffEntry> listBranchDiff(Git git, String oldBranch, String newBranch) throws IOException, GitAPIException {
    	Repository repo = git.getRepository();
    	
        AbstractTreeIterator oldTreeParser = prepareTreeParser(repo, oldBranch); 
        AbstractTreeIterator newTreeParser = prepareTreeParser(repo, newBranch); 
        ArrayList<DiffEntry> diff = (ArrayList<DiffEntry>) git.diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).call();
        
        /*
        for (DiffEntry entry : diff) {
            System.out.println("Entry: " + entry
            		+ " " + entry.getChangeType().toString()
            		+ " " + entry.getOldPath()
            		+ " " + entry.getNewPath()
            		);
        }
        */

    	return diff;
    }
    
    /*
     * You are asking why we have svn commands in a git library? 
     * This method is here to facilitate single artifact exports from Github
     * This only works with Github as every git repository hosted there is also a svn repository
     * Git by nature loves to clone, pulling only changes from a git repository involves cloning
     * all artifacts. We are using svn here because we can and it is far easier to do so. 
     * Parameters:
     * 	String exportObj - this can be a top level directory, but in thi case we forcing this to always be a file from a git diff
     *  File exportPath - top level folder destination where the export will go
     *  String token - github auth token pelase generate and dont share
     * Return:
     * 	Nothing since this will just export
     */
    public static void githubSvnExport(String exportObj, File exportPath, String token) {
    	
		SVNRepository repository = null;
		
		try{
			
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(SVNEncodingUtil.autoURIEncode(exportObj)));
			//create authentication data
			ISVNAuthenticationManager authManager =  SVNWCUtil.createDefaultAuthenticationManager("", token.toCharArray());
			
			
			repository.setAuthenticationManager(authManager);
			System.out.println( "Repository Root: " + repository.getRepositoryRoot( true ) );
			System.out.println(  "Repository UUID: " + repository.getRepositoryUUID( true ) );
			
			long latestRevision = repository.getLatestRevision();
			System.out.println(  "Repository Latest Revision: " + latestRevision);
			
			SVNClientManager ourClientManager = SVNClientManager.newInstance();
			ourClientManager.setAuthenticationManager(authManager);
			
			SVNUpdateClient updateClient = ourClientManager.getUpdateClient( );
			updateClient.setIgnoreExternals( false );
			updateClient.doExport( repository.getLocation(), exportPath, 
					SVNRevision.create(latestRevision), SVNRevision.create(latestRevision), 
					null, true, SVNDepth.INFINITY);
			/*
			  static SVNDepth 	EMPTY
	          		Just the named directory D, no entries.
	          static SVNDepth 	EXCLUDE
	          		Exclude (don't descend into) directory D.
	          static SVNDepth 	FILES
	          		D and its file children, but not subdirectories.
	          static SVNDepth 	IMMEDIATES
	          		D and its immediate children (D and its entries).
	          static SVNDepth 	INFINITY
	          		D and all descendants (full recursion from D).
	          static SVNDepth 	UNKNOWN
	          		Depth undetermined or ignored.
			*/
			
		} catch (SVNException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done");
		}
    	
    	
    }
    
    
    
    
	
	
	
}