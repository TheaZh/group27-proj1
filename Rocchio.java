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

	public static void main(String[] args) {
		Map<String, Integer> dfMap = new HashMap<String, Integer>();
		dfMap.put("mask",2);dfMap.put("table",2);dfMap.put("cost",1);dfMap.put("ask",4);
        Query query = new Query("mask", dfMap);
		// Rocchio test = new Rocchio(dfMap);
		// test.computeQ(query);

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

	/**
	 * compute the Q vector
	 */
    /*
	private void computeQ(){
		// first: compute query's df & tf
		//Map<String, Integer> dfMap = new HashMap<String, Integer>();
		Map<String, Integer> qTFMap = new HashMap<String, Integer>();
			// for those relevant docs
		for(Doc d : relevantDocs){
			d.computeTermsWeight(relevantTermsWeight);
			for(String term : query.getTermList()){
				if(d.getTFMap().containsKey(term)){
						// df
					/
					if(!dfMap.containsKey(term)){
						dfMap.put(term, 0);
					}
					dfMap.put(term, dfMap.get(term)+1);
                    /

						// tf
					if(!qTFMap.containsKey(term)){
						qTFMap.put(term, 0);
					}
					qTFMap.put(term, qTFMap.get(term)+d.getTFMap().get(term));
				}
			}
		}

			// for those non-relevant docs
		for(Doc d : nonrelevantDocs){
			d.computeTermsWeight(nonrelevantTermsWeight);
			for(String term : query.getTermList()){
				if(d.getTFMap().containsKey(term)){
						// df
					/
					if(!dfMap.containsKey(term)){
						dfMap.put(term, 0);
					}
					dfMap.put(term, dfMap.get(term)+1);
					/
						// tf
					if(!qTFMap.containsKey(term)){
						qTFMap.put(term, 0);
					}
					qTFMap.put(term, qTFMap.get(term)+d.getTFMap().get(term));
				}
			}
		}

		// Second: compute query's weight

		qTermsWeight = query.getQTermsWeight(dfMap, qTFMap);

		// get Q vector

	}
    */


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
			double non_relevant = nonrelevantTermsWeight.containsKey(term) ? (GAMMA * nonrelevantTermsWeight.get(term)/nonRelNom): 0.0;

			if(!vector.containsKey(term)){
				vector.put(term, 0.0);
			}

			double weight = relevant - non_relevant;
			vector.put(term, vector.get(term) + weight);
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
            @override
            public int compare(Pair a, Pair b) {
                return a.value - b.value;
            }
        });

        int cnt = 0;
        for(Pair pair : pairList) {
            if(!oldQueryStrsSet.containsKey(pair.k)) {
                sb.append(pair.v + " ");
                cnt++;
                if(cnt >= 2) break;
            }
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
