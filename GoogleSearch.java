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
import java.util.Arrays;


public class GoogleSearch {

	String API_KEY = "AIzaSyBpMdM3c6XYISNPICI0qEdEECtRo5gemqA";
	String ENGINE_KEY = "018258045116810257593:z1fmkqqt_di";
    String query = "per se";

	private List<Result> getItems(){
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);
        List<Result> items = null;
	    try {
	        com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse().list(query);
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

        float precision = (float)0.9;
        List<String> query = new ArrayList<>();
        query.add("per");
        query.add("se");

        // System.out.println("args len: " + args.length);
        System.out.println("query is " + Arrays.toString(args));

        // args
        if(args.length < 4 && args.length >0) {
            System.out.print("Usage: ./GoogleSearch <google api key> <google engine id> <precision> <query>\n" +
            "<google api key> is your Google Custom Search API Key\n" +
            "<google engine id> is your Google Custom Search Engine ID\n" +
            "<precision> is the target value for precision@10, a real number between 0 and 1\n" +
            "<query> is your query, a list of words in double quotes (e.g., “Milky Way”)\n"
            );
            System.exit(0);
        }

		GoogleSearch search = new GoogleSearch();
        if(args.length >= 4) {
            search.API_KEY = args[0];
            search.ENGINE_KEY = args[1];
            query.clear();
            for(int i = 4; i < args.length; i++) query.add(args[i]);
            System.out.println(query.toString());
            StringBuilder querySb = new StringBuilder();
            for(String str : query) querySb.append(query + " ");
            querySb.setLength(querySb.length() - 1);
            search.query = querySb.toString();
        }

        Filter filter = new Filter();
        Scanner sc = new Scanner(System.in);

        while(true) {

            List<Result> items = search.getItems();
            List<String> docsStrList = search.getDocsStrings(items);
            List<Doc> docsList = new ArrayList<>();
            List<Boolean> isRelevant = new ArrayList<>();
            List<List<String>> docEffectiveList = new ArrayList<>();
            filter.clearDfTable();

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
	            System.out.println("----------------------");

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
                Doc doc = new Doc(docEffective, filter.getDfTable());
                docsList.add(doc);
            }

            // System.out.println(isRelevant.toString());

            // decide precision
            int num_relevant = 0;
            for(int i = 0; i < isRelevant.size(); i++) {
                if(isRelevant.get(i)) num_relevant++;
            }
            float this_precision = (float)num_relevant / (float) isRelevant.size();
            System.out.println("precision is " + this_precision);
            if(this_precision >= precision) {
                System.out.println("precision >= " + precision);
                break;
            }
            else if(this_precision <= 0) {
                System.out.println("precision = 0");
                break;
            }

            // TODO update the query
            search.query = "java is good";
        }
	}
}
