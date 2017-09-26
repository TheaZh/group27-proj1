import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Math;

public class Doc{
    List<String> docStrings;        // all the strings in this doc
    List<String> terms;             // all the terms in all the docs
    Map<String, Integer> tfTable;   // termfrequncy table
    List<Double> d;                 // doc vector for this doc
    Map<String, Integer> dfTable;
    /**
    * This method returns the square of num.
    * This is a multiline description. You can use
    * as many lines as you like.
    * @param documentString The list of document effective strings.
    * @param df The map: term -> document frequency.
    * @return num squared.
    */
	public Doc(List<String> documentStringList, Map<String, Integer> df){
        this.docStrings = documentStringList;
        this.terms = this.getAllTerms(df);

        // count the term frequency
        this.tfTable = new HashMap<String, Integer>();
        for(String each : this.terms) {
            this.tfTable.put(each, 0);
        }
        for(String word : this.docStrings) {
            this.tfTable.put(word, this.tfTable.get(word) + 1);
        }

        this.d = this.getDocVector();
        System.out.println(this.d.toString());
	}

    // get sorted terms in all docs
    private List<String> getAllTerms(Map<String, Integer> df) {
        List<String> termList = new ArrayList<>(df.keySet());
        Collections.sort(termList);
        return termList;
    }

    // get the document vector
    public List<Double> getgetDocVector() {
        List<Double> vector = new ArrayList<>();

        for(int i=0; i<terms.size(); i++) {
            String term = terms.get(i);
            vector.add(Math.log10(1+tfTable.get(term)) * Math.log10(10/dfTable.get(term)));
        }
    }


}
