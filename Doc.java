import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

public class Documents{
    List<String> listOfDocs;
	public Doc(List<String> documentStrings, ){
		// count the frequency of each token
		tfTable = new Hashtable<String, Integer>();
		for(String token : tokens){
			if(tfTable.containsKey(token)){
				tfTable.put(token, tfTable.get(token) + 1);
			}
			else{
				tfTable.put(token, 1);
			}
		}
	}


}
