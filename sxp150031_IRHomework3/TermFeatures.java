import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TermFeatures {
	int df; //document frequency  no of docs that the term occurs in
	int tf; //total frequency , total number of terms
	Map<Integer,Integer> docFreq = new TreeMap<Integer,Integer>(); // doc term frequency
	Map<String,List<Integer>> listOfDocsterm = new HashMap<String,List<Integer>>();
}
