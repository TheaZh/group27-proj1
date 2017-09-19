import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.util.List;


public class GoogleSearch {

	public static void main(String[] args) {
		GoogleSearch test = new GoogleSearch();
		test.get();

	}
	
	private void get(){
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);
		
	    try {
	        com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse().list("java");
	        list.setKey("AIzaSyBpMdM3c6XYISNPICI0qEdEECtRo5gemqA");
	        list.setCx("018258045116810257593:z1fmkqqt_di");
	        Search results = list.execute();
	        List<Result> items = results.getItems();
	
	        for(Result result:items)
	        {
	            //System.out.println("Title:"+result.getHtmlTitle());
	            System.out.println("Title: "+ result.getTitle());
	            System.out.println("URL: " + result.getLink());
	            //System.out.println("display: " + result.getDisplayLink());
	            System.out.println("snippet: " + result.getSnippet());
	            System.out.println("----------------------");
	        }
	
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
