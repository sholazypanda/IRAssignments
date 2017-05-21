import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
public class DocFeatures {
	    
		int doclen; // total number of word occurrences , includes the number of stop words encountered in the respective document
		int max_tf =-1; // frequency of most frequent term or stem for each document
		String mostFrequentTerm;
		public Map<String,Double> w1 = new HashMap<String, Double>();
		public Map<String,Double> w1n = new HashMap<String, Double>();
		public Map<String,Double> w2 = new HashMap<String, Double>(); 
		public Map<String,Double> w2n = new HashMap<String, Double>();
		String docTit;
		String docName;
		public Map<String,Integer> doctf = new TreeMap<String,Integer>(); // doc term freq
		public Map<String,Double> doctfw = new TreeMap<String,Double>();; // doc term freq weight
		
		public void findMostFrequentTerm(Map<String,Integer> map){
			int max = -1;
			String mostFreqTerm ="";
			for(String word:map.keySet()){
				int maxFreq = map.get(word);
				if(maxFreq >max){
					max = maxFreq;
					mostFreqTerm = word;
				}
				
			}
			this.max_tf = max;
			this.mostFrequentTerm = mostFreqTerm;
			
		}
		public void getDocLen(Map<String,Integer> map){
			int dl =0;
			for(String s : map.keySet()){
				dl += map.get(s);
			}
			doclen=dl;
		}
		public void getW1(Map<String,TermFeatures> idx,long csize){
			//System.out.println("getW1");
			w1 = new HashMap<String, Double>();
			w1n = new HashMap<String, Double>();
			double ssm = 0.0;
			for(String tk : doctf.keySet()){	
				int tf = doctf.get(tk);
				int df =0;
				if(idx.get(tk) != null){
					df = idx.get(tk).df;
				}		
				//System.out.println(tk+" "+IndexReader.calcW1(tf,max_tf,df,csize));
				w1.put(tk, IndexReader.calcW1(tf,max_tf,df,csize));
				double pt = w1.get(tk);
				pt = pt*pt;
				ssm +=pt;
			}
			ssm = Math.sqrt(ssm);
			for(String tk : w1.keySet()){
				double v = w1.get(tk)/ssm;
				w1n.put(tk, v);
			}
		}
		public void getW2(Map<String,TermFeatures> idx,long csize,double avgdoclen){
			w2 = new HashMap<String, Double>();
			w2n = new HashMap<String, Double>();
			double ssm = 0.0;
			for(String tk : doctf.keySet()){	
				int tf = doctf.get(tk);
				int df =0;
				if(idx.get(tk) != null){
					df = idx.get(tk).df;
				}			
				
				w2.put(tk, IndexReader.calcW2(tf,doclen,avgdoclen,df,csize)); 
				double pt = w2.get(tk);
				pt = pt*pt;
				ssm +=pt;
			}
			ssm = Math.sqrt(ssm);
			for(String tk : w2.keySet()){
				double v = w2.get(tk)/ssm;
				w2n.put(tk, v);
			}	
		}
		
}

