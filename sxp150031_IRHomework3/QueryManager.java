import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryManager {
	private Set<String> stopWords;
	private static long csize;
	private static double adoclen;
	private static Map<String,TermFeatures> termfe;
	private static Map<Integer,DocFeatures> docfe; 
	private static String inputPath;
	private static Indexing con;
	QueryManager(Indexing idxCon,String inputPath){
		this.csize = idxCon.csize;
		this.adoclen = idxCon.adoclen;		
		this.stopWords = idxCon.stopWList;
		this.termfe = idxCon.getLemIdx();
		this.docfe = idxCon.getLemTreeMap();
		this.inputPath = inputPath;
		this.con = idxCon;
	}
	public static QueryFeature run(String q) throws IOException{
		Tokens tokObj = IndexReader.getQToks(q);
		Map<String,Integer> lemCountMap = con.buildDictLemmas(tokObj);
		DocFeatures qvObj = new DocFeatures();
		qvObj.getDocLen(lemCountMap);
		qvObj.docTit = q;
		qvObj.findMostFrequentTerm(lemCountMap);
		qvObj.doctf = lemCountMap;
		qvObj.getW1(termfe, csize);
		qvObj.getW2(termfe, csize, adoclen);
		Map<Integer, Double> s1 = new HashMap<Integer, Double>(); //[docID: score sum from normalized weights after summation
		Map<Integer, Double> s2 = new HashMap<Integer, Double>();
		for(String tok : lemCountMap.keySet()){			
			TermFeatures idx = termfe.get(tok);			
			if(idx != null){
				for(int did : idx.docFreq.keySet()){
					DocFeatures dvObj = docfe.get(did);
					double dw1n = dvObj.w1n.get(tok);
					double qw1n = qvObj.w1n.get(tok);
					if(s1.get(did) == null){
						s1.put(did, dw1n*qw1n);
					}else{
						double t = s1.get(did);
						s1.put(did, (dw1n*qw1n) + t);
					}
					
					double dw2n = dvObj.w2n.get(tok);
					double qw2n = qvObj.w2n.get(tok);
					if(s2.get(did) == null){
						s2.put(did, dw2n*qw2n);
					}else{
						double t = s2.get(did);
						s2.put(did, (dw2n*qw2n) + t);
					}				
				}
			}						
		}		
		return new QueryFeature(qvObj,s1,s2);
	}
}	
