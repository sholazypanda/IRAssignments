import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Pattern;
public class Homework3 {
	public static String cranDataPath; 
	public static String stopWordsPath;
	public static String queryPath;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args.length < 1 ){
			cranDataPath = "/Users/shobhikapanda/Documents/InformationRetrieval/IRHomeWork2/src/Cranfield/"; ///people/cs/s/sanda/cs6322/Cranfield/
			stopWordsPath="/Users/shobhikapanda/Documents/InformationRetrieval/IRHomeWork2/src/resourcesIR/stopwords"; ///people/cs/s/sanda/cs6322/resourcesIR/stopwords
			queryPath ="/Users/shobhikapanda/Documents/InformationRetrieval/IRHomeWork3/src/hw3.queries"; ///people/cs/s/sanda/cs6322/hw3.queries
		} else {
			cranDataPath = args[0];
			stopWordsPath= args[1];
			queryPath = args[2];
		}
		long startTime = System.currentTimeMillis();
		Indexing indexCon = new Indexing(cranDataPath,stopWordsPath);
		indexCon.constructIndex();
		List<String> queryList = getQuery(queryPath);
		QueryManager qm = new QueryManager(indexCon,cranDataPath);
		for(int i=0;i<queryList.size();i++){
			System.out.println("\nQuery"+(i+1)+ ": " + queryList.get(i));
			QueryFeature q = qm.run(queryList.get(i));
			System.out.println("Query Lemmas: "+ q.docObj.doctf.toString() );
			System.out.println("Weight 1 Vector: \n"+q.docObj.w1.toString());
			System.out.println("Normalized: \n"+q.docObj.w1n.toString());
			System.out.println();
			System.out.println("TOP 5 document by WEIGHT-1");
			getT5(q.s1,indexCon.getLemTreeMap(),"W1");
			System.out.println();
			System.out.println("TOP 5 document by WEIGHT-2");
			System.out.println("Weight 2 Vector: \n"+q.docObj.w2.toString());
			System.out.println("Normalized: \n"+q.docObj.w2n.toString());
			getT5(q.s2,indexCon.getLemTreeMap(),"W2");
			
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time Taken to Execute : "+(endTime-startTime)+" milliseconds");
	}
	private static List<String> getQuery(String fn) {
		try{
			String data = new String(Files.readAllBytes(new File(fn).toPath() ));
			String[] pts= Pattern.compile("[Q0-9:]+").split(data);
			List<String> qrs = new ArrayList<>();
			for(String p : pts ){
				String q = p.trim().replaceAll("\\r\\n", " ");
				if(q.length() > 0){
					qrs.add(q);
				}
			}
			return qrs;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public static void getT5(Map<Integer, Double> st,Map<Integer,DocFeatures> d , String tp) {
		
		class ValueComparator implements Comparator<Entry<Integer, Double>> {
			@Override
			public int compare(Entry<Integer, Double> t1, Entry<Integer, Double> t2) {
				if(t1.getValue() < t2.getValue()){
					return 1;
				}	
			   return -1;
			}
		}
		
		TreeSet<Entry<Integer, Double>> sortedSet = new TreeSet<Entry<Integer, Double>>(new ValueComparator());
		sortedSet.addAll(st.entrySet());		
		Iterator<Entry<Integer, Double>> iterator = sortedSet.iterator();
		
		System.out.println("Rank" + "\t Score          " + "\t Doc. Identifier" + " \t " + "Document Title");
		for(int i = 0 ; i < 5 && iterator.hasNext(); i++){
			Entry<Integer, Double> entry = iterator.next();		
			int did = entry.getKey();				
			System.out.println((i+1) + "\t" + entry.getValue() + "\t" + 
					d.get(did).docName + "\t" + d.get(did).docTit);
	
		}	
		System.out.println();		
		
	}

}
