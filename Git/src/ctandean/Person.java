package ctandean;

import com.fasterxml.jackson.annotation.JsonProperty;

//POJO for test person
//Form 
/*
[{ "Name" : "Chris", "Age" : 20, "Notes" : { "Entry" : "123t", "Comment" : "Hi" } } , { "Name" : "Tim", "Age" : 40, "Notes" : { "Entry" : "4567", "Comment" : "Bye" } }]
*/

/*
[
	{
	 "Name" : "Chris",
	 "Age" : 20,
	 "Notes" : {
	 "Entry" : "123t",
	 "Comment" : "Hi"
	 }
	},
	{
	 "Name" : "Tim",
	 "Age" : 40,
	 "Notes" : {
	 "Entry" : "4567",
	 "Comment" : "Bye"
	 }
	}	 
]

*/
public class Person {
	
	@JsonProperty("Name")
	public String name;
	
	@JsonProperty("Age")
	public int age;
	
	@JsonProperty("Notes")
	public Notes notes;
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	//inner class
	public static class Notes {
		
		@JsonProperty("Entry")
		public String entry;
		
		@JsonProperty("Comment")
		public String comment;
		
		public String getEntry() {
			return entry;
		}
		
		public String getComment() {
			return comment;
		}
	}
	
	
	
	
	
	
	
	
	
	
}