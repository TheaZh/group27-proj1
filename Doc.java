import java.util.*;

public class Doc{
    List<String> docStrings;        // all the strings in this doc
    List<String> terms;             // all the terms in all the docs
    Map<String, Integer> tfMap;   // termfrequncy table
    List<Double> d;                 // doc vector for this doc
    Map<String, Integer> dfMap;


    public Doc(List<String> documentStringList){
        tfMap = new HashMap<String, Integer>();
        for(String term : documentStringList){
            if(!tfMap.containsKey(term)){
                tfMap.put(term, 0);
            }
            tfMap.put(term, tfMap.get(term)+1);
        }
    }






    /**
    * This method returns the square of num.
    * This is a multiline description. You can use
    * as many lines as you like.
    * @param documentString The list of document effective strings.
    * @param df The map: term -> document frequency.
    * @return num squared.
    */
    /*
	public Doc(List<String> documentStringList, Map<String, Integer> df){
        this.docStrings = documentStringList;
        this.terms = this.getAllTerms(df);

        // count the term frequency
        this.tfMap = new HashMap<String, Integer>();
        for(String each : this.terms) {
            this.tfMap.put(each, 0);
        }
        for(String word : this.docStrings) {
            this.tfMap.put(word, this.tfMap.get(word) + 1);
        }

        this.d = this.getDocVector();
        System.out.println(this.d.toString());
	}

     */
    private void computeTermsWeight(Map<String, Double> termsWeight, Map<String, Integer> dfMap){
        for(Map.Entry<String, Integer> entry : tfMap.entrySet()){
            String term = entry.getKey();
            Integer fre = entry.getValue();
            if(!termsWeight.containsKey(term)){
                termsWeight.put(term, 0.0);
            }
            double weight =(double)Math.log10(1.0+tfMap.get(term)) * Math.log10(10/dfMap.get(term));
            termsWeight.put(term, termsWeight.get(term)+weight);
        }
    }

    /*
    // get sorted terms in all docs
    private List<String> getAllTerms(Map<String, Integer> df) {
        List<String> termList = new ArrayList<>(df.keySet());
        Collections.sort(termList);
        return termList;
    }

    // get the document vector
    public List<Double> getDocVector() {
        List<Double> vector = new ArrayList<>();

        for(int i=0; i<terms.size(); i++) {
            String term = terms.get(i);
            vector.add(Math.log10(1+tfMap.get(term)) * Math.log10(10/dfMap.get(term)));
        }
        return vector;
    }
    */

    /*
     * @return  return the term-frequency map of this doc
     */
    public Map<String, Integer> getTFMap(){
        return this.tfMap;
    }

}
