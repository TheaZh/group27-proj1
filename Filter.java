import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.*;

class Filter{
	private Set<String> stopwords;
	private Map<String, Integer> dfMap;

    /**
    * construct a new Filter, init its stopwords, create empty dfMap.
    */
	public Filter(){
		this.stopwords = new HashSet<String>();
		this.dfMap = new HashMap<String, Integer>();
		getStopwords();
	}

	// read stopword file, and create a stopword set
	private void getStopwords(){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader("sup/proj1-stopword.txt"));
			String line = null;
			while((line = br.readLine()) != null){
				stopwords.add(line);
				//System.out.println(line);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    /**
	* add efficient terms in token list, filter those stopwords
	* @return tokens --> a efficient word list of this doc
    */
	public List<String> filterStopwords(String str){
		List<String> tokens = new ArrayList<String>();
		String format = "\\d+.\\d+|\\w+|\\w+-w+";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(str);
		Set<String> wordSet = new HashSet<String>();
		while(matcher.find()){
			String token = matcher.group();
            token = token.toLowerCase();
			// not a stopword
			// if(!stopwords.contains(token)){ // using filter
            if(true){ // not using filter
				//System.out.println(token);
				tokens.add(token);
				/*
				 * if it's the first time that the term appears in this doc
				 * the number of doc that word appears in ++
				 */
				if(wordSet.add(token)){
					if(!dfMap.containsKey(token)){
						dfMap.put(token, 0);
					}
					dfMap.put(token, dfMap.get(token)+1);
				}
			}
		}
		return tokens;
	}

	/**
	 * @return a table : <String, Integer>
	 * the number of docs that a word appears in
	 */
	public Map<String, Integer> getDfMap(){
		return this.dfMap;
	}

    // debug
	public static void main(String[] args) {
		Filter filter = new Filter();

		List<String> list;

		list = filter.filterStopwords("Per Se entrance. center. Macadamia nut dipped in chocolate. left. Mini meat filled pastries. right. Per Se Salon. center. Fish and vegetables. right. Oysters and ...");
		System.out.println(list.toString());
        System.out.println(filter.getDfMap().toString());

        list = filter.filterStopwords("Daily Menus Two tasting menus are offered daily: a nine-course chef's tasting menu as well as a nine-course vegetable tasting menu. No single ingredient is ...");
		System.out.println(list.toString());
        System.out.println(filter.getDfMap().toString());
	}
}
