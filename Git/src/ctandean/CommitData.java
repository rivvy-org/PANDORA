package ctandean;

/*
 * Java object class meant to represent commit data compiled through git logs
 * Focus is on commit data inside each git commit .
 */

public class CommitData {
	private String commitSha1 = "";
	private String branchName = "";
	private String artifactPath = "";
	private String artifactName = "";
	private String author = "";
	private String commiter = "";
	private String shortMessage = "";
	private String longMessage = "";
	private String commitDate = "";
	private int pathLength = 0;
	private String artifactSha1 = "";
	
	public CommitData(String commitSha1,String branchName,String artifactPath,
					  String artifactName,String author,String commiter,
					  String shortMessage,String longMessage,String commitDate,
					  int pathLength, String artifactSha1) {
		this.commitSha1 = commitSha1;
		this.branchName = branchName;
		this.artifactPath = artifactPath;
		this.artifactName = artifactName;
		this.author = author;
		this.commiter = commiter;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
		this.commitDate = commitDate;
		this.pathLength = pathLength;
		this.artifactSha1 = artifactSha1;
	}
    
	public void setCommitSha1(String sha1) {
		this.commitSha1 = commitSha1;
	}
	
	public String getCommitSha1() {
		return commitSha1;
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
	
	public void setArtifactSha1(String sha1) {
		this.artifactSha1 = artifactSha1;
	}
	
	public String getArtifactSha1() {
		return artifactSha1;
	}
	
	public String toString() {
		return "CommitSha1-ID: " + commitSha1 + " ArtifactSha1-ID: " + artifactSha1 +" Branch Name: " + branchName 
				+ " Artifact Path: " + artifactPath + " Artifact_Name: " + artifactName + " Author: " + author 
				+ " Committer: " + commiter + " Short_Message: " + shortMessage + " Long_Message: " + longMessage
				+ " Commit_Date: " + commitDate + " Path_length: " + pathLength;	
	}
	
}