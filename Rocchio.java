import java.util.*;
class Rocchio{

    public Query query;
	private List<Doc> relevantDocs;
	private List<Doc> nonrelevantDocs;
	private Map<String, Integer> dfMap;         // doc frequency
    public Map<String, Double> qTermsWeight;    // q vector
    public Map<String, Double> vector;          // final Result for this round

    // three parameters
	private final double ALPHA = 1.0;
	private final double BETA = 0.75;
	private final double GAMMA = 0.15;

    // debug
	public static void main(String[] args) {
        String pass1 = "BlueJ. A free Java Development Environment designed for beginners, used by millions worldwide. Find out more... One of my favourite IDEs out there is BlueJ";
        String pass2 = "What are the hours of the Blue Java Coffee Bar in Butler Library? Question: What are the hours of the Blue Java Coffee Bar in Butler Library? Answer: See the ...";

        Filter filter = new Filter();
        Set<String> searchWordsSet = new HashSet<>(Arrays.asList("blue", "java"));
        List<String> effective1 = filter.filterStopwords(pass1, searchWordsSet);
        List<String> effective2 = filter.filterStopwords(pass2, searchWordsSet);
        Doc doc1 = new Doc(effective1);
        doc1.computeTermsWeight(filter.getDfMap());
        Doc doc2 = new Doc(effective2);
        doc2.computeTermsWeight(filter.getDfMap());
        List<Doc> relDocList = new ArrayList<>();
        relDocList.add(doc1);
        List<Doc> nonRelList = new ArrayList<>();
        nonRelList.add(doc2);

        String queryStr = "blue java";
        Query q = new Query(queryStr, filter.getDfMap());
        Rocchio algo = new Rocchio(q, filter.getDfMap(), relDocList, nonRelList);
        System.out.println("The new qeury is: " + algo.getNewQueryStr());
	}

    /**
    * Constructs a Rocchio object with relevant doc and non-relevant docs
    * @param df the document frequency map computed by filter each iteration
    * @param relDocList the list of relevant docs
    * @param nonRelList the list of non-relevant docs
    * @return a new Rocchio object
    */
	public Rocchio(Query queryIn, Map<String, Integer> df, List<Doc> relDocList, List<Doc> nonRelList){
        query = queryIn;
        dfMap = df;
		relevantDocs = relDocList;
		nonrelevantDocs = nonRelList;
        vector = new  HashMap<String, Double>();
        qTermsWeight = query.getQTermsWeight();

        computeAllWeight();
	}

	private void computeAllWeight() {

        // add q_i-1
		for(String term : qTermsWeight.keySet()){
			vector.put(term, qTermsWeight.get(term)*ALPHA);
		}

        // sum up two weight vector and compute |R| and |NR|
        // sum up relevantTermsWeight
        Map<String, Double> relevantTermsWeight = new HashMap<>();
        for(Doc doc : relevantDocs) {
            Map<String, Double> weights = doc.termsWeight;
            for(String term : weights.keySet()) {
                double weight = weights.get(term);
                relevantTermsWeight.put(term, relevantTermsWeight.getOrDefault(term, 0.0) + weight);
            }
        }
        // sum up nonrelevantTermsWeight
        Map<String, Double> nonrelevantTermsWeight = new HashMap<>();
        for(Doc doc : nonrelevantDocs) {
            Map<String, Double> weights = doc.termsWeight;
            for(String term : weights.keySet()) {
                double weight = weights.get(term);
                nonrelevantTermsWeight.put(term, nonrelevantTermsWeight.getOrDefault(term, 0.0) + weight);
            }
        }
        // compute |R| and |NR|
		double relNom = 0, nonRelNom = 0;
		for(Double val : relevantTermsWeight.values()){
			relNom += val * val;
		}
		for(Double val : nonrelevantTermsWeight.values()){
			nonRelNom += val * val;
		}
		relNom = Math.sqrt(relNom);
		nonRelNom = Math.sqrt(nonRelNom);

        // add these to vector
		for(String term : relevantTermsWeight.keySet()) {//!!!!!!!!!!!!!!!!
			double relevant = BETA * relevantTermsWeight.get(term) / relNom;
			if(!vector.containsKey(term)){
				vector.put(term, 0.0);
			}
			vector.put(term, vector.get(term) + relevant);
		}
        for(String term : nonrelevantTermsWeight.keySet()) {//!!!!!!!!!!!!!!!!
			double non_relevant = (GAMMA * nonrelevantTermsWeight.get(term)/nonRelNom);
			if(!vector.containsKey(term)){
				vector.put(term, 0.0);
			}
			vector.put(term, vector.get(term) - non_relevant);
		}
        // now vector is the new q_i
	}

    class Pair {
        String k;
        double v;
        public Pair(String key, double value) {
            k = key;
            v = value;
        }
    }

    public String getNewQueryStr() {
        List<String> oldQueryStrsList = this.query.getTermList();
        Set<String> oldQueryStrsSet = this.query.searchWords;

        StringBuilder sb = new StringBuilder();
        for(String str : oldQueryStrsList) sb.append(str + " ");

        // sort vector by weights
        List<Pair> pairList = new ArrayList<>();
        for(String key : vector.keySet()) {
            pairList.add(new Pair(key, vector.get(key)));
        }

        Collections.sort(pairList, new Comparator<Pair>() {
            @Override
            public int compare(Pair a, Pair b) {
                if(a.v < b.v) return 1;
                if(a.v > b.v) return -1;
                return 0;
            }
        });


        for(int i = 0; i < 10 && i < pairList.size(); i++)
            System.out.println(pairList.get(i).k+": "+pairList.get(i).v);
        
        int cnt = 0;
        int limit = oldQueryStrsSet.size();
        for(Pair pair : pairList) {
            if(!oldQueryStrsSet.contains(pair.k)&&cnt<2) {
                sb.append(pair.k + " ");
                cnt++;
            }
            if(oldQueryStrsSet.contains(pair.k)&&limit>0){
                sb.append(pair.k + " ");
                limit--;
            }
            if(cnt==2&&limit==0) break;
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
