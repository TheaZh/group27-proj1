import java.util.*;

class Query{

    public List<String> searchWordList;
	public Set<String> searchWords;
    private Map<String, Integer> dfMap;
    public Map<String, Double> qTermsWeight;
    /**
    * Constructs a query object
    */
	public Query(String searchWord, Map<String, Integer> dfMapIn) {
        searchWordList = new ArrayList<String>();
		searchWords = new HashSet<String>();
        dfMap = dfMapIn;
        qTermsWeight = new HashMap<String, Double>();

		String[] keywords = searchWord.split("\\s+");
		for(int i = 0; i<keywords.length; i++){
			searchWords.add(keywords[i]);
            searchWordList.add(keywords[i]);
		}

        computeQTermsWeight();
	}

	// debug
	public static void main(String[] args) {
		String searchWord = "mask table";

		Map<String, Integer> dfMap = new HashMap<String, Integer>();
		dfMap.put("mask",2);dfMap.put("table",9);dfMap.put("cost",1);dfMap.put("ask",4);

        Query query = new Query(searchWord, dfMap);
        System.out.println("This query vector is: " + query.getQTermsWeight().toString());
	}

	public List<String> getTermList(){
		return this.searchWordList;
	}

	public void computeQTermsWeight() {
        System.out.println("in computeQTermsWeight: "+this.searchWords.toString());
        for(String term : this.searchWords) {
            int df = dfMap.get(term);
			int tf = 1;  // in q query each word frequency should be 1
            double weight = (double)Math.log10(1.0+tf) * Math.log10(10.0/df);
            qTermsWeight.put(term, weight);
        }
	}

    public Map<String, Double> getQTermsWeight() {
        return this.qTermsWeight;
    }
}
