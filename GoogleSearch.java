import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class GoogleSearch {

	private final String API_KEY = "AIzaSyBpMdM3c6XYISNPICI0qEdEECtRo5gemqA";
	private final String ENGINE_KEY = "018258045116810257593:z1fmkqqt_di";

	private List<Result> getItems(){
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);
        List<Result> items = null;
	    try {
	        com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse().list("per se");
	        list.setKey(API_KEY);
	        list.setCx(ENGINE_KEY);
	        Search results = list.execute();
	        items = results.getItems();

	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
        return items;
	}

    public List<String> getDocsStrings(List<Result> items) {
        List<String> docsStrList = new ArrayList<>();
        for(Result result:items) {
            StringBuilder sb = new StringBuilder();
            sb.append(result.getTitle());
            sb.append(" ");
            sb.append(result.getLink());
            sb.append(" ");
            sb.append(result.getSnippet());
            docsStrList.add(sb.toString());
        }
        return docsStrList;
    }

    public static void main(String[] args) {
		GoogleSearch search = new GoogleSearch();

        Filter filter = new Filter();
        Scanner sc = new Scanner(System.in);

        while(true) {
            List<Result> items = search.getItems();
            List<String> docsStrList = search.getDocsStrings(items);
            List<Doc> docsList = new ArrayList<>();
            List<Boolean> isRelevant = new ArrayList<>();
            List<List<String>> docEffectiveList = new ArrayList<>();

            for(int i = 0; i < items.size(); i++) {
                // split and filt stopword
                String docStr = docsStrList.get(i);
                List<String> tokens = new ArrayList<>();
                filter.filterStopwords(tokens, docStr);
                docEffectiveList.add(tokens);

                Result result = items.get(i);
                System.out.println("Title: "+ result.getTitle());
	            System.out.println("URL: " + result.getLink());
	            System.out.println("snippet: " + result.getSnippet());
	            System.out.println("----------------------\n");

                System.out.println("Is it relevant? Enter 'y' for yes, 'n' for no.");
                String feedback = sc.nextLine();
                if(feedback.equals("y")) {
                    System.out.println("this is relevant.");
                    isRelevant.add(true);
                }
                else { // "n"
                    System.out.println("this is NOT relevant.");
                    isRelevant.add(false);
                }
            }

            // create Doc Array
            for(int i = 0; i < docEffectiveList.size(); i++) {
                List<String> docEffective = docEffectiveList.get(i);
                Doc doc = new Doc(docEffective, filter.getDFTable());
                docsList.add(doc);
            }

            System.out.println(isRelevant.toString());
        }
	}
}
