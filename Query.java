import java.util.*;

public class Query{

	private List<String> words;
	public Query(String searchWord){
		words = new ArrayList<String>();
		String[] keywords = searchWord.split("\\s+");
		for(int i = 0; i<keywords.length;i++){
			words.add(keywords[i]);
		}
	}

	// debug
	/* 
	public static void main(String[] args) {
		String searchWord = "mask";
		Query query = new Query(searchWord);
		Map<String, Integer> dfMap = new HashMap<String, Integer>();
		dfMap.put("mask",2);dfMap.put("table",2);dfMap.put("cost",1);dfMap.put("ask",4);
		Map<String, Integer> qTFMap = new HashMap<String, Integer>();
		qTFMap.put("mask", 100); 
		Map<String, Double> test = query.getQTermsWeight(dfMap, qTFMap);
		for(Map.Entry<String, Double> en: test.entrySet()){
			System.out.println("word, weight: " + en.getKey() + " --> " + en.getValue());
		}
	}
	*/
	public List<String> getTermList(){
		return words;
	}

	public Map<String, Double> getQTermsWeight(Map<String, Integer> dfMap, 
													Map<String, Integer> qTFMap){
		Map<String, Double> tmpTermsWeight = new HashMap<>();
		for(Map.Entry<String, Integer> en : qTFMap.entrySet()){
			String term = en.getKey();
			int df = dfMap.get(term);
			int tf = qTFMap.get(term);
			double weight = (double)Math.log10(1.0+qTFMap.get(term)) * Math.log10(10/dfMap.get(term));

			if(!tmpTermsWeight.containsKey(term)){
				tmpTermsWeight.put(term, 0.0);
			}
			tmpTermsWeight.put(term, tmpTermsWeight.get(term)+weight);
		}
		return tmpTermsWeight;
	}
}