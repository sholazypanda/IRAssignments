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
	public Set<String> stopWList;
	private static Lemmatize lemmatizer = new Lemmatize();
	private Map<String,TermFeatures> lemIdxMap;
	public long csize;
	public double adoclen;
	private Map<Integer,DocFeatures> lemTreeMap; // sorted order of doc ids
		
	IndexReader idxReader;
	public Map<String, TermFeatures> getLemIdx() {
		return lemIdxMap;
	}
	public Map<Integer, DocFeatures> getLemTreeMap() {
		return lemTreeMap;
	}

	public Indexing(String inputPath, String stopWordsPath){
			this.inputPath = inputPath;
			this.stopWordsPath = stopWordsPath;
			this.stopWList = new HashSet<String>();
			this.idxReader = new IndexReader();
			this.lemIdxMap = new TreeMap<String, TermFeatures>();
			
			this.lemTreeMap = new TreeMap<Integer, DocFeatures>();
			
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
		csize = docNames.size();
		long sdl = 0;
		for(String docStrId:docNames){
		Tokens tok = idxReader.initiate(inputPath,docStrId);
		Map<String,Integer>lemCountMap = buildDictLemmas(tok);
		int docId = tok.docID;
		DocFeatures lemDoc = new DocFeatures();
		lemDoc.getDocLen(lemCountMap);
		sdl+=lemDoc.doclen;
		lemDoc.findMostFrequentTerm(lemCountMap);
		lemDoc.docTit = IndexReader.getTitle(inputPath, docStrId);
		lemDoc.docName = docStrId;
		lemDoc.doctf = lemCountMap;
		//System.out.println("doctf"+(lemDoc.doctf.isEmpty()==true));
		buildIndex(docId,lemCountMap,lemIdxMap); // create index map with term features
		lemTreeMap.put(docId, lemDoc); // create treemap of docid's and doc features
		}
		adoclen = (double)sdl / csize;
		for(Integer docID : lemTreeMap.keySet()){
			DocFeatures d = lemTreeMap.get(docID);
			d.getW1(lemIdxMap,csize); // insert the lemmaIndex use his to get the document fequency
			d.getW2(lemIdxMap,csize,adoclen);
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

	

	public Map<String, Integer> buildDictLemmas(Tokens tok) {
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

	
	
		
}
