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
    * @param df The map: term -> document frequency.
    */
    public void computeTermsWeight(Map<String, Integer> df){
        dfMap = df;
        for(Map.Entry<String, Integer> entry : tfMap.entrySet()){
            String term = entry.getKey();
            Integer fre = entry.getValue();
            if(!termsWeight.containsKey(term)){
                termsWeight.put(term, 0.0);
            }

            double weight =(double)fre * (Math.log(10.0/dfMap.get(term)) + 1.0);
            termsWeight.put(term, termsWeight.get(term) + weight);
        }

        normalize();
        System.out.println(termsWeight.toString());
    }

    /**
    * Nomalize a termsWeight
    */

    private void normalize() {
        double card = 0.0;
        for(String key : termsWeight.keySet()) {
            double weight = termsWeight.get(key);
            card += weight * weight;
        }
        card = Math.sqrt(card);

        for(String key : termsWeight.keySet()) {
            double weight = termsWeight.get(key);
            termsWeight.put(key, weight / card);
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
        List<String> strs1 = new ArrayList<>(Arrays.asList("jiajun", "zhan", "zheng", "zheng", "sample"));
        List<String> strs2 = new ArrayList<>(Arrays.asList("jiajun", "zhan", "jia", "jia", "example", "example", "example"));
        Map<String, Integer> df = new HashMap<>();
        df.put("jiajun", 2); df.put("zhan", 2); df.put("zheng", 1);
        df.put("sample", 1); df.put("jia", 1); df.put("example", 1);

        Doc testDoc1 = new Doc(strs1);
        Doc testDoc2 = new Doc(strs2);
        // print the term frequency table
        System.out.println("term frequency 1: " + testDoc1.tfMap.toString());
        System.out.println("term frequency 1: " + testDoc2.tfMap.toString());

        testDoc1.computeTermsWeight(df);
        testDoc2.computeTermsWeight(df);
        // print the document frequency table
        System.out.println("doc frequency 1: " + testDoc1.dfMap.toString());
        System.out.println("doc frequency 2: " + testDoc2.dfMap.toString());
        // print doc vector
        System.out.println("term weight 1: " + testDoc1.termsWeight.toString());
        System.out.println("term weight 2: " + testDoc2.termsWeight.toString());
    }


}
