import java.util.HashMap;
import java.util.Map;

public class QueryFeature {
	//public QueryFeature qfeature;
	public DocFeatures docObj;
	public Map<Integer, Double> s1 = new HashMap<Integer, Double>(); //(docID,document Score)
	public Map<Integer, Double> s2 = new HashMap<Integer, Double>();
	
	public QueryFeature(DocFeatures docObj,Map<Integer, Double> s1,Map<Integer, Double> s2) {
		this.docObj = docObj;
		this.s1 = s1;
		this.s2 = s2;
	}
}
