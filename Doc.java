import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

public class Doc{
	private Hashtable<String, Integer> tfTable;

	public Doc(List<String> tokens){
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