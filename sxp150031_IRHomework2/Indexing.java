import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Indexing {
	private String inputPath;	
	private String stopWordsPath;
	private Set<String> stopWList;
	
	public static Lemmatize lemmatizer = new Lemmatize();
	
	private Map<String,TermFeatures> lemIdxMap;
	private Map<String,TermFeatures> stmIdxMap;
	
	private Map<Integer,DocFeatures> lemTreeMap; // sorted order of doc ids
	private Map<Integer,DocFeatures> stemTreeMap; // sorted order of doc ids	
	IndexReader idxReader;
	public Map<String, TermFeatures> getLemIdx() {
		return lemIdxMap;
	}
	public Map<Integer, DocFeatures> getLemTreeMap() {
		return lemTreeMap;
	}
	public Map<String, TermFeatures> getstmIdx() {
		return stmIdxMap;
	}
	public Map<Integer, DocFeatures> getStmTreeMap() {
		return stemTreeMap;
	}
	public Indexing(String inputPath, String stopWordsPath){
			this.inputPath = inputPath;
			this.stopWordsPath = stopWordsPath;
			this.stopWList = new HashSet<String>();
			this.idxReader = new IndexReader();
			this.lemIdxMap = new TreeMap<String, TermFeatures>();
			this.stmIdxMap = new TreeMap<String, TermFeatures>();
			this.lemTreeMap = new TreeMap<Integer, DocFeatures>();
			this.stemTreeMap = new TreeMap<Integer, DocFeatures>();
			BufferedReader br;
			try{
				br = new BufferedReader(new FileReader(stopWordsPath));
			  	String line;
			    while( (line = br.readLine()) != null){
			    	stopWList.add(line.trim());
			    }
			    br.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public void constructIndex() throws IOException{
		LinkedList<String> docNames =idxReader.getFileNames(inputPath);
		for(String docStrId:docNames){
		Tokens tok = idxReader.initiate(inputPath,docStrId);
		Map<String,Integer>lemCountMap = buildDictLemmas(tok);
		Map<String,Integer>stemCountMap = buildDictStem(tok);
		int docId = tok.docID;
		DocFeatures stemDoc = new DocFeatures();
		DocFeatures lemDoc = new DocFeatures();
		stemDoc.doclen = tok.tokens.size();
		lemDoc.doclen = tok.tokens.size();
		stemDoc.findMostFrequentTerm(stemCountMap);
		lemDoc.findMostFrequentTerm(lemCountMap);
		buildIndex(docId,lemCountMap,lemIdxMap); // create index map with term features
		buildIndex(docId,stemCountMap,stmIdxMap);
		
		lemTreeMap.put(docId, lemDoc); // create treemap of docid's and doc features
		stemTreeMap.put(docId, stemDoc); 
		}
		// create separate dictionaries everytime
	}

	private void buildIndex(int docId, Map<String, Integer> countMap, Map<String, TermFeatures> idxTreeMap) {
		// TODO Auto-generated method stub
		//List<Integer> docIds = new ArrayList<Integer>();
		for(String word:countMap.keySet()){
		TermFeatures t;	
			if(idxTreeMap.containsKey(word)){
				t = idxTreeMap.get(word);
				
			}else{
				t = new TermFeatures();
				idxTreeMap.put(word, t);
			}
			t.df = t.df + 1; // no of docs this word occurs
			t.tf = t.tf + countMap.get(word); // countMap contains count of each word , so total count of a term is countMap+existing term frequency
			t.docFreq.put(docId,countMap.get(word)); // doc term freq, number of times a word occurs in a particular doc 
			//docIds.add(docId);
			//t.listOfDocsterm.put(word,docIds);
		}
	}

	

	private Map<String, Integer> buildDictLemmas(Tokens tok) {
		// TODO Auto-generated method stub
		Map<String,Integer>lemCountMap = new HashMap<String,Integer>();
		List<String> words= tok.tokens;
		for(String word:words){
			if(!stopWList.contains(word)){
				List<String> lemmas = lemmatizer.doLemmatization(word);
				for(String lemma:lemmas){
					if(!lemCountMap.containsKey(lemma)){
						lemCountMap.put(lemma, 1);
					}
					else{
						lemCountMap.put(lemma, lemCountMap.get(lemma)+1);
					}
				}
			}
		}
		
		return lemCountMap;
	}

	private Map<String, Integer> buildDictStem(Tokens tok) {
		Map<String,Integer>stemCountMap = new HashMap<String,Integer>();
		List<String> words= tok.tokens;
		for(String word:words){
			if(!stopWList.contains(word)){
				Stemmer stemmer=new Stemmer();
				stemmer.add(word.toCharArray(), word.length());
				stemmer.stem();
				String stm=stemmer.toString();
				
					if(!stemCountMap.containsKey(stm)){
						stemCountMap.put(stm, 1);
					}
					else{
						stemCountMap.put(stm, stemCountMap.get(stm)+1);
					}
				
			}
		}
		
		return stemCountMap;
	}
	
	
		
}
