

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ParSecStemmer {
	
	private Tokenize tokObj; //tokenize obj
	private Map<String,Integer> sDict; // stem dict
	private int NOS; // no of stems
	Long startTime;
	private TreeMap<Integer,List<String>> tmap;// map to keep key as frequency and values as list of words with same frequency
	List<String> wordOnce; // list of words that occur only once
	Map<Integer, List<String>> newMap;  // tree map with descending order by key
	public ParSecStemmer(Tokenize tokObj,Long startTime){
		this.tokObj = tokObj;
		this.sDict = new HashMap<String,Integer>();
		this.tmap = new TreeMap<Integer,List<String>>();
		this.wordOnce = new ArrayList<>();
		this.newMap = new TreeMap(Collections.reverseOrder());
		this.startTime = startTime;
	}

	public void fillStemDict(){
		Map<String,Dictionary> tDict = tokObj.getTDict();
		for(String key:tDict.keySet()){
			Dictionary d = tDict.get(key);
			Stemmer stemObj=new Stemmer();			  
			stemObj.add(d.term.toCharArray(), d.term.length());
			stemObj.stem();
			String stemString=stemObj.toString();
			if(sDict.containsKey(stemString)){				  
				  sDict.put(stemString,sDict.get(stemString)+d.totFreq);
			  }else{				  
				  sDict.put(stemString, d.totFreq);
			  }
			NOS+=d.totFreq;
		}
	}
	public void showResults(){
		System.out.println("The number of distinct stems in the Cranfield text collection: "+ sDict.size()+".");
        System.out.println("The number of stems that occur only once in the Cranfield text collection: "+ occurOnce()+".");
        System.out.println("The 30 most frequent stems in the Cranfield text collection.");
        mostFreqStems();
        
        System.out.println("Average number of word stems per documents: "+ NOS/tokObj.docNames.size());
        long endTime = System.currentTimeMillis();
        System.out.println("Time required to acquire text characteristic (in sec): " + ((endTime - startTime) / 1000)); 
	}
	public int occurOnce(){
		int countOnce = 0;
		
		for(String key: sDict.keySet()){
			Integer len = sDict.get(key);
			if(len == 1){
				countOnce++;
				wordOnce.add(key);
			}
			if(tmap.containsKey(len)){
				List<String> listofStems = tmap.get(len); // list of words with same frequency;
				listofStems.add(key);
				tmap.put(len,listofStems);
			}
			else{
				List<String> listofStems = new ArrayList<>();
				listofStems.add(key);
				tmap.put(len,listofStems);
			}
		}
		return countOnce;
	}
	public void mostFreqStems(){
		
		newMap.putAll(tmap);
		int count = 1;
		for(Integer k: newMap.keySet()){
			if(count >=30) break;
			System.out.println("Key is :"+k+"Value is:"+newMap.get(k));
			count++;
		}
	}
}
