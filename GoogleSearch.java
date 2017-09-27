import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.util.*;


public class GoogleSearch {

	String API_KEY = "AIzaSyBpMdM3c6XYISNPICI0qEdEECtRo5gemqA";
	String ENGINE_KEY = "018258045116810257593:z1fmkqqt_di";
    String query = "per se";

    /**
    * get the search result list for this.query
    * @return a List of query Result
    */
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

    /**
    * Change Result list into doc string list
    * @param items result object list
    * @return doc string list, for one doc
    */
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
            precision = Float.parseFloat(args[2]);
            query.clear();
            for(int i = 3; i < args.length; i++) query.add(args[i]);
            StringBuilder querySb = new StringBuilder();
            for(String str : query) querySb.append(str + " ");
            querySb.setLength(querySb.length() - 1);
            search.query = querySb.toString();
        }

        Filter filter = new Filter();
        Scanner sc = new Scanner(System.in);
        int round = 0;
        while(true) {
            // start a new iteration
            System.out.println("-------------------------------");
            System.out.println("           Round "+ ++round);
            System.out.println("-------------------------------");
            System.out.println("Now the query is: " + search.query);
            List<Result> items = search.getItems();
            // each doc is a string, 10 doc in a list
            List<String> docsStrList = search.getDocsStrings(items);
            List<Doc> docsList = new ArrayList<>();
            List<Boolean> isRelevant = new ArrayList<>();
            // each doc is a list of string, and is filtered
            List<List<String>> docEffectiveList = new ArrayList<>();
            filter.clearDfMap();

            for(int i = 0; i < items.size(); i++) {
                // split and filt stopword
                String docStr = docsStrList.get(i);
                Set<String> searchWordsSet = new HashSet<>();
                for(String str : search.query.split("\\s+")) searchWordsSet.add(str);
                List<String> tokens = filter.filterStopwords(docStr, searchWordsSet);
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
                Doc doc = new Doc(docEffective);
                doc.computeTermsWeight(filter.getDfMap());
                docsList.add(doc);
            }

            // now we have a List of Doc, and a List of boolean for according relevance

            // decide precision
            List<Doc> relDocList = new ArrayList<>();
            List<Doc> nonRelList = new ArrayList<>();
            for(int i = 0; i < isRelevant.size(); i++) {
                if(isRelevant.get(i)) {
                    relDocList.add(docsList.get(i));
                }
                else {
                    nonRelList.add(docsList.get(i));
                }
            }
            float this_precision = (float)relDocList.size() / (float) docsList.size();
            System.out.println("precision is " + this_precision);
            if(this_precision >= precision) {
                System.out.println("precision >= " + precision);
                System.out.println("Stop at round " + round);
                break;
            }
            else if(this_precision <= 0) {
                System.out.println("precision = 0");
                System.out.println("Stop at round " + round);
                break;
            }

            // System.out.println("DF Map: "+ filter.getDfMap());
            /*
            for(Doc doc : docsList) {
                System.out.println("TF Map: " + doc.tfMap.toString());
                System.out.println("d vector: " + doc.termsWeight.toString());
            }
            */

            // update the query using Rocchio
            // search.query = "java is good";
            Query q = new Query(search.query, filter.getDfMap());
            // System.out.println("q vector: " + q.qTermsWeight.toString());
            Rocchio algo = new Rocchio(q, filter.getDfMap(), relDocList, nonRelList);
            // System.out.println("new q: " + algo.vector.toString());
            search.query = algo.getNewQueryStr();
        }
	}
}
