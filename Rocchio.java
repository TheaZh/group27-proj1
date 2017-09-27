import java.util.*;
class Rocchio{

	private List<Doc> relevantDocs;
	private List<Doc> nonrelevantDocs;
	private Map<String, Double> qTermsWeight;
	private Map<String, Integer> dfMap;
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
	public Rocchio(Map<String, Integer> df, List<Doc> relDocList, List<Doc> nonRelList){
		/*
		 * compute q
		 */
		dfMap = df;
		relevantDocs = relDocList;
		nonrelevantDocs = nonRelList;
	}

	/**
	 * compute the Q vector
	 */
	private void computeQ(Query query){
		// first: compute query's df & tf
		//Map<String, Integer> dfMap = new HashMap<String, Integer>();
		Map<String, Integer> qTFMap = new HashMap<String, Integer>();
			// for those relevant docs
		for(Doc d : relevantDocs){
			d.computeTermsWeight(relevantTermsWeight);
			for(String term : query.getTermList()){
				if(d.getTFMap().containsKey(term)){
						// df
					/*
					if(!dfMap.containsKey(term)){
						dfMap.put(term, 0);
					}
					dfMap.put(term, dfMap.get(term)+1);

					*/
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
					/*
					if(!dfMap.containsKey(term)){
						dfMap.put(term, 0);
					}
					dfMap.put(term, dfMap.get(term)+1);
					*/
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

	private void computeAllWeight(){
		for(String term : qTermsWeight.keySet()){
			qTermsWeight.put(term, qTermsWeight.get(term)*ALPHA);
		}
		doulble relNom = 0, nonRelNom = 0;
		for(Double val : relevantTermsWeight.valueSet()){
			relNom += val * val;
		}
		for(Double val : nonrelevantTermsWeight.valueSet()){
			nonRelNom += val * val;
		}
		relNom = Math.sqrt(relNom);
		nonRelNom = Math.sqrt(nonRelNom);
		for(String term : relevantTermsWeight.keySet()) {//!!!!!!!!!!!!!!!!
			double relevant = BETA * relevantTermsWeight.get(term) / relNom;
			double non_relevant = nonrelevantTermsWeight.containsKey(term) ? (GAMMA * nonrelevantTermsWeight.get(term)/nonRelNom): 0.0;

			if(!qTermsWeight.containsKey(term)){
				qTermsWeight.put(term, 0.0);
			}
			double weight = relevant - non_relevant;
			qTermsWeight.put(term, qTermsWeight.get(term) + weight);
		}
	}
}
