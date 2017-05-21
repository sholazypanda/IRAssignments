

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Tokenize {
	private String inputPath;
	private List<Word> wordList;
	private Word wObj;
	private HashMap<String,Dictionary> dict;
	LinkedList<String> docNames;
	private TreeMap<Integer,List<String>> tmap;// map to keep key as frequency and values as list of words with same frequency
	List<String> wordOnce; // list of words that occur only once
	Map<Integer, List<String>> newMap;  // tree map with descending order by key
	Long startTime;
	public Tokenize(String inputPath,Long startTime) {
		this.inputPath = inputPath;
		this.wordList = new ArrayList<Word>();
		this.dict = new HashMap<String,Dictionary>();
		this.wObj = new Word();
		this.tmap = new TreeMap<Integer,List<String>>();
		this.wordOnce = new ArrayList<>();
		this.newMap = new TreeMap(Collections.reverseOrder());
		this.startTime = startTime;
	}
	public Map<String, Dictionary> getTDict() {
		return dict;
	}
	public void initiate() throws IOException {
		docNames = getFileNames();
		for(String dc:docNames){
			String textContent = getTokensListFromDoc(dc);
			String [] textContentTokens = textContent.split(" ");
			for(String words : textContentTokens){
				if(words.endsWith("'s"))
					words = words.replace("'s", "");
				words=replacePunct(words);
				if(words.length()>0){
					Word w = new Word();
					w.dID = dc;
					w.tok = words;
					wordList.add(w);
				}
			}
			
			
		}
		for(Word w:wordList){
			if(dict.containsKey(w.tok)){
				Dictionary wordFromDict = dict.get(w.tok);
				Integer freq = wordFromDict.totFreq;
				wordFromDict.totFreq = freq+1;	
				Map<String, Integer> ptList = wordFromDict.docPostList;
				if(ptList.containsKey(w.dID)){
					ptList.put(w.dID, ptList.get(w.dID+1));
				}
				else{
					ptList.put(w.dID, 1);
					wordFromDict.NOD = wordFromDict.NOD+1;
				}
				
				
			}
			else{ 
				// when dictionary doesn't contain the word, create new dictionary, set word, number of documents and frequency
				// posting will have a word as key and its frequency as value.
				Dictionary d = new Dictionary();
				d.NOD = 1;
				d.term = w.tok;
				d.totFreq = 1;
				HashMap<String,Integer> ptFile = new HashMap<>();
				ptFile.put(w.dID,1);
				d.docPostList = ptFile;
				dict.put(w.tok, d);
			}
			
		}
		/*for(String key:dict.keySet()){
			System.out.println("Key is::"+key+"Value is::"+dict.get(key).getTerm()+" "+dict.get(key).getTotFreq());
		}
		System.out.println("End");
		System.out.println(dict.size());*/
		//wordList.size();
		
	}
	public String getTokensListFromDoc(String docName) throws IOException{
		StringBuffer textContent = new StringBuffer();
		//List<Word> tempList = new ArrayList<>();
		
	    BufferedReader br;
		try {
				br = new BufferedReader(new FileReader(inputPath+docName));
			  	String line;
			    while( (line = br.readLine()) != null){
				    if(!(line.contains("<") && line.contains(">"))){
				    	 String [] tokens = line.split("\\s+");
					      for (String token : tokens) {
					    	  //token = replacePunct(token);
					    	  token = token.replaceAll("[-]"," ").toLowerCase();
					    	  textContent.append(token+" ");
					      }
				    }
			    }
			    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return textContent.toString();
		
		
	}
	private String replacePunct(String token) {
		// TODO Auto-generated method stub
		 token = token.replaceAll("[^a-zA-Z]", ""); //ignore numbers
		 return token;
	}
	public LinkedList<String> getFileNames(){
		LinkedList<String> fileNames = new LinkedList<>();
		//System.out.println(inputPath);
		try{
			File folder = new File(inputPath);
			File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  fileNames.add(listOfFiles[i].getName());
		      } 
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return fileNames;
	}
	public void showResults(){
		 	System.out.println("Number of Tokens in the Cranfield text collection: "+ wordList.size()+".");
	        System.out.println("Number of Unique Words in the Cranfield text collection: "+ dict.size()+".");
	        System.out.println("Number of Words that occur once in the Cranfield text collection: "+ occurOnce()+".");
	        System.out.println("The 30 most frequent words in the Cranfield text collection.");
	        mostFreqWords();
	        
	        System.out.println("Average number of word tokens per documents: "+ wordList.size()/docNames.size());
	        long endTime = System.currentTimeMillis();
	        System.out.println("Time required to acquire text characteristic (in sec): " + ((endTime - startTime) / 1000)); 
	      //  System.out.println("List of words occuring once::"+wordOnce);
	}
	public int occurOnce(){
		int countOnce = 0;
		
		for(Entry<String, Dictionary> word : dict.entrySet()){
			Dictionary tWord = word.getValue();
			if(tWord.totFreq == 1){
				countOnce++;
				wordOnce.add(tWord.term);
			}
			if(tmap.containsKey(tWord.totFreq)){
				List<String> listofWords = tmap.get(tWord.totFreq); // list of words with same frequency;
				listofWords.add(tWord.term);
				tmap.put(tWord.totFreq,listofWords);
			}
			else{
				List<String> listofWords = new ArrayList<>();
				listofWords.add(tWord.term);
				tmap.put(tWord.totFreq,listofWords);
			}
		}
		return countOnce;
	}
	public void mostFreqWords(){
		
		newMap.putAll(tmap);
		int count = 1;
		for(Integer k: newMap.keySet()){
			if(count >=30) break;
			System.out.println("Key is :"+k+"Value is:"+newMap.get(k));
			count++;
		}
	}
}
