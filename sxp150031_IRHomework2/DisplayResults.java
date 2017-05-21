import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisplayResults {
	public static Lemmatize lemmatizer = new Lemmatize();
	
	public static void showIdxRes(long time_taken,int index_size){
		
		System.out.println("Time Taken to build Index: "+time_taken+" milliSeconds");
		System.out.println("Number of inverted list in Index: "+index_size);
		System.out.println("---");
		System.out.println("---");
	}
	public static Set<String> constructLemma(Set<String> t){
		Set<String> lemSet = new HashSet<String>();
		for(String word: t){
			List<String> l = lemmatizer.doLemmatization(word);
			for(String lem: l){
				lemSet.add(lem);
			}
		}
		return lemSet;
	}
	public static Set<String> constructStem(Set<String> t){
		Set<String> stemSet = new HashSet<String>();
		for(String word: t){
			Stemmer stemmer=new Stemmer();
			stemmer.add(word.toCharArray(), word.length());
			stemmer.stem();
			stemSet.add(stemmer.toString());
		}
		return stemSet;
	}
	public static void showStats(Set<String> set,Map<String,TermFeatures> idx){
		System.out.println("Term---TF---DF---Size of Inverted List");
		for(String t: set){
			long len = 0;
			if(idx.containsKey(t)){
				TermFeatures tf = idx.get(t);
				len += Character.SIZE * t.length();
				len += Integer.SIZE * 2; // for doc freq and tot term freq
				for(Integer docIds :tf.docFreq.keySet()){
					len += Integer.SIZE * 2;
				}
				System.out.println(t + "---"+ idx.get(t).tf+
						"---"+idx.get(t).df+
						"---"+len+" bytes");
			}
			else{
				System.out.println(t+"Term was not found");
			}
		}
	}
	public static void showNasaStats(String t,Map<String,TermFeatures> tIndex,Map<Integer,DocFeatures> dIndex){
		System.out.println("Stats for term : "+ t);
		System.out.println("Total Term Frequency : "+ tIndex.get(t).tf);
		System.out.println("Document Frequency   : "+ tIndex.get(t).df);
		System.out.println("First Three Entries in Posting List");
		System.out.println("Document ID---TF---Max-TF---Doc-Length");
		int i=0;
		for(Integer docId:tIndex.get(t).docFreq.keySet()){
			System.out.print(docId+"---"+tIndex.get(t).docFreq.get(docId)+"---");
			System.out.println(dIndex.get(docId).max_tf+"---"
							+dIndex.get(docId).doclen);
			i++;
			if(i>2)break;
		}
	}
	public static void showDFStats(Map<String,TermFeatures> idx){
		int max=Integer.MIN_VALUE;
		int min=Integer.MAX_VALUE;
		List<String> mxList = new ArrayList<String>();
		List<String> minList = new ArrayList<String>();
		for(String keys: idx.keySet()){
				if(idx.get(keys).df > max){
					max = idx.get(keys).df;
					mxList = new ArrayList<String>();
					mxList.add(keys);
							
				}
				else if(idx.get(keys).df == max){
					mxList.add(keys);
				}
				if(idx.get(keys).df < min){
					min = idx.get(keys).df;
					minList = new ArrayList<String>();	
					minList.add(keys);
				}
				else if(idx.get(keys).df == min){
					minList.add(keys);
				}
		}
		System.out.println("The maximum Document Frequency is: "+max);
		System.out.println("The terms with the maximum Document Frequency is: "+mxList);
		System.out.println("The minimum Document Frequency is: "+min);
		System.out.println("The terms with the minimum Document Frequency: "+minList);
	}
	public static void showTFStats(Map<Integer,DocFeatures> dIndex){
		int mxTF=0;
		int mxDocLen =0;
		String mostFreqTerm="";
		List<Integer> mxTfList=new ArrayList<Integer>();
		List<Integer> mxDocList = new ArrayList<Integer>();
		for(Integer docId:dIndex.keySet()){
			if(dIndex.get(docId).max_tf > mxTF){
				mxTF = dIndex.get(docId).max_tf;
				mxTfList = new ArrayList<Integer>();
				mxTfList.add(docId);
				mostFreqTerm = dIndex.get(docId).mostFrequentTerm;
			}
			else if(dIndex.get(docId).max_tf == mxTF){
				mxTfList.add(docId);
			}
			if(dIndex.get(docId).doclen > mxDocLen){
				mxDocLen = dIndex.get(docId).doclen;
				mxDocList = new ArrayList<Integer>();
				mxDocList.add(docId);
			}
			else if(dIndex.get(docId).doclen == mxDocLen){
				mxDocList.add(docId);
			}
		}
		System.out.println("Document with largest max_tf : "+mxTfList+" which has most frequent term as "+mostFreqTerm+
				" which is repeated "+mxTF+" times.");
		System.out.println("Document with largest doclen : "+mxDocList+" it has doclen "+mxDocLen+" .");
	}
	
}	
