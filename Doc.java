import java.util.*;

class Doc{
    Map<String, Integer> tfMap;     // termfrequncy table
    Map<String, Integer> dfMap;     // document frequency for each term
    public Map<String, Double> termsWeight;// weight for each term in this doc


    /**
    * This construction method creates a new Doc object
    * and computes its term -> frequency Map.
    * @param documentStringList The list of document effective strings.
    * @return a new Doc object.
    */
    public Doc(List<String> documentStringList){
        tfMap = new HashMap<String, Integer>();
        for(String term : documentStringList){
            if(!tfMap.containsKey(term)){
                tfMap.put(term, 0);
            }
            tfMap.put(term, tfMap.get(term)+1);
        }
        termsWeight = new HashMap<String, Double>();
    }

    /**
    * This method computes the weight for each term in this doc.
    * after calling this method, you can refer this.termsWeight as its Doc vector
    * @param documentString The list of document effective strings.
    * @param dfMap The map: term -> document frequency.
    */
    public void computeTermsWeight(Map<String, Integer> df){
        dfMap = df;
        for(Map.Entry<String, Integer> entry : tfMap.entrySet()){
            String term = entry.getKey();
            Integer fre = entry.getValue();
            if(!termsWeight.containsKey(term)){
                termsWeight.put(term, 0.0);
            }

            double weight =(double)Math.log10(1.0+fre) * Math.log10(10.0/dfMap.get(term));
            termsWeight.put(term, termsWeight.get(term)+weight);
        }
    }

    /**
     * @return  return the term-frequency map of this doc
     */
    public Map<String, Integer> getTfMap(){
        return this.tfMap;
    }

    // test
    public static void main(String[] args) {
        List<String> strs = new ArrayList<>(Arrays.asList("java", "blue", "cafe", "coffee", "coffee", "java", "java"));
        Map<String, Integer> df = new HashMap<>();
        df.put("java", 10); df.put("cafe", 5); df.put("coffee", 7);
        df.put("blue", 9); df.put("red", 6);

        Doc testDoc = new Doc(strs);
        // print the term frequency table
        System.out.println("term frequency: " + testDoc.tfMap.toString());

        testDoc.computeTermsWeight(df);
        // print the document frequency table
        System.out.println("doc frequency: " + testDoc.dfMap.toString());
        // print doc vector
        System.out.println("term weight: " + testDoc.termsWeight.toString());
    }


}
