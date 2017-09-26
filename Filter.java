import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Hashtable;

class Filter{
	private Set<String> stopwords;
	private Hashtable<String, Integer> dfTable;

	public Filter(){
		stopwords = new HashSet<String>();
		dfTable = new Hashtable<String, Integer>();
		getStopwords();
	}

	// debug
	public static void main(String[] args) {
		Filter filter = new Filter();
		//Set<String> t = filter.getStopwords();
		List<String> list = new ArrayList<String>();
		filter.filterStopwords(list, "a book-about of Java 3.33");
		System.out.println(list.toString());
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

	// add efficient terms in token list, filter those stopwords
	// return tokens --> a efficient word list of this doc
	public List<String> filterStopwords(List<String> tokens, String str){
		String format = "\\d+.\\d+|\\w+|\\w+-w+";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(str);
		HashSet<String> tmpSet = new HashSet<String>();
		while(matcher.find()){
			String token = matcher.group();
			// not a stopword
			if(!stopwords.contains(token)){
				//System.out.println(token);
				tokens.add(token);
				/*
				 * if it's the first time that the term appears in this doc
				 * the number of doc that word appears in ++
				 */
				if(tmpSet.add(token)){
					if(!dfTable.containsKey(token)){
						dfTable.put(token, 0);
					}
					dfTable.put(token, dfTable.get(token)+1);
				}
			}
		}
		return tokens;
	}

	/*
	 * @return a table : <String, Integer>
	 * the number of docs that a word appears in
	 */
	public Hashtable<String, Integer> getDFTable(){
		return dfTable;
	}
}
