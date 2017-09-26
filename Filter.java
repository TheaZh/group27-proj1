import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Filter{
	private Set<String> stopwords;
	
	public Filter(){
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
		stopwords = new HashSet<String>();
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
	private List<String> filterStopwords(List<String> tokens, String str){
		String format = "\\d+.\\d+|\\w+|\\w+-w+";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(str);

		while(matcher.find()){
			String token = matcher.group();
			if(!stopwords.contains(token)){
				System.out.println(token);
				tokens.add(token);
			}
		}
		return tokens;
	}
}