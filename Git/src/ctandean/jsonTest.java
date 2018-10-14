package ctandean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class jsonTest {
	
	public static void main(String[] args) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			
			String jsonInString1 = "{ \"Name\" : \"Chris\", \"Age\" : 20, \"Notes\" : { \"Entry\" : \"123t\", \"Comment\" : \"Hi\" } }";
			String jsonInString2 = "[{ \"Name\" : \"Chris\", \"Age\" : 20, \"Notes\" : { \"Entry\" : \"123t\", \"Comment\" : \"Hi\" } } , { \"Name\" : \"Tim\", \"Age\" : 40, \"Notes\" : { \"Entry\" : \"4567\", \"Comment\" : \"Bye\" } }]";
			Person person1 = mapper.readValue(jsonInString1, Person.class);
			System.out.println(person1.getName());
			
			Map<String, String> map = new HashMap<String, String>();
			
			List<Person> list = mapper.readValue(jsonInString2, new TypeReference<List<Person>>(){});
			
			for (int i=0;i<list.size();i++) {
				System.out.println(list.get(i).getName()+" "+list.get(i).getAge()+" "+list.get(i).notes.getEntry()+" "+list.get(i).notes.getComment()); 

			}
			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
		
}