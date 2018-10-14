package ctandean;

/*
 * Java object class meant to represent commit data compiled through git logs
 * Focus is on commit data inside each git commit .
 */

public class CommitData {
	private String sha1 = "";
	private String branchName = "";
	private String artifactPath = "";
	private String artifactName = "";
	private String author = "";
	private String commiter = "";
	private String shortMessage = "";
	private String longMessage = "";
	private String commitDate = "";
	private int pathLength = 0;
	
	public CommitData(String sha1,String branchName,String artifactPath,
					  String artifactName,String author,String commiter,
					  String shortMessage,String longMessage,String commitDate,
					  int pathLength) {
		this.sha1 = sha1;
		this.branchName = branchName;
		this.artifactPath = artifactPath;
		this.artifactName = artifactName;
		this.author = author;
		this.commiter = commiter;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
		this.commitDate = commitDate;
		this.pathLength = pathLength;
		
	}
    
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	
	public String getSha1() {
		return sha1;
	}
	
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public String getBranchName() {
		return branchName;
	}
	
	public void setArtifactPath(String artifactPath) {
		this.artifactPath = artifactPath;
	}
	
	public String getArtifactPath() {
		return artifactPath;
	}
	
	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}
	
	public String getArtifactName() {
		return artifactName;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}	
	
	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}
	
	public String getCommiter() {
		return commiter;
	}
	
	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}
	
	public String getShortMessage() {
		return shortMessage;
	}
	
	public void setLongMessage(String longMessage) {
		this.longMessage = longMessage;
	}
	
	public String getLongMessage() {
		return longMessage;
	}
	
	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}
	
	public String getCommitDate() {
		return commitDate;
	}
	
	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}
	
	public int getPathLength() {
		return pathLength;
	}
	
	public String toString() {
		return "Sha1-ID: " + sha1 + " Branch Name: " + branchName + " Artifact Path: " + artifactPath
				+ " Artifact Name: " + artifactName + " Author: " + author + " Committer: " + commiter
				+ " Short Message: " + shortMessage + " Long Message: " + longMessage
				+ " Commit Date: " + commitDate + " Path length: " + pathLength;	
	}
	
}