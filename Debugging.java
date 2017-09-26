import java.util.*;

public class Debugging{
	public static void main(String[] args) {
		Map<String, Integer> dfMap = new HashMap<String, Integer>();
		Map<String, Double> termWeight = new HashMap<String, Double>();
		Query query = new Query("world", dfMap);
		Filter filter = new Filter(dfMap);
		List<String> list1 = filter.filterStopwords("this is a good world!!");
		List<String> list2 = filter.filterStopwords("the world is tough");
		List<String> list3 = filter.filterStopwords("oh My God!");
		System.out.println(list1.toString());
		System.out.println(list2.toString());
		System.out.println(list3.toString());
		System.out.println("-----------");

		Doc doc1 = new Doc(list1);
		doc1.computeTermsWeight(termWeight, dfMap);
		Doc doc2 = new Doc(list2);
		doc2.computeTermsWeight(termWeight, dfMap);
		Doc doc3 = new Doc(list3);
		doc3.computeTermsWeight(termWeight, dfMap);
		for(Map.Entry<String,Integer> en : filter.getDFMap().entrySet()){
			System.out.println("word, df: " + en.getKey() + ",  " + en.getValue());
		}
		System.out.println("------------");
		for(Map.Entry<String,Double> en : termWeight.entrySet()){
			System.out.println("word, df: " + en.getKey() + ",  " + en.getValue());
		}
	}
}