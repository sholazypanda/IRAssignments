import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

public class Compression {
	 class TermFeatureCompress{
	    byte[] idxT;
		byte[] docfreq;
	    List<DocTermFeatureCompress> dTlist;
	}
	 class DocFeatureCompress{
		byte[] docIdgp;
		byte[] maxTf;
		byte[] doclen;
		
	}
	 class DocTermFeatureCompress{
		byte[] docIdgp;
		byte[] dTFreq;
	}

	
	byte[] tmstr;
	
	public List<TermFeatureCompress> idxTermComp;
	public List<DocFeatureCompress> idxDocComp;
	
	
	
	public byte[] convertGamma(int n){
		String gc = getGC(n);		
		return toByteArray(gc);
	}
	
	public String getUV(int l) {
		String uV="";
		for(int i=0;i<l;i++){
			uV=uV.concat("1");
		}
		return uV;
	}
	
	public String getGC(int n) {
		String bin = Integer.toBinaryString(n);
		String offset = bin.substring(1);	
		String uV = getUV(offset.length());	
		String gc =  uV.concat("0").concat(offset);
		return gc;
	}
	public byte[] toByteArray(String gc) {
		BitSet bS = new BitSet(gc.length());
		for(int i = 0; i < gc.length(); i ++){
			Boolean v = gc.charAt(i) == '1' ? true : false;
			bS.set(i, v);
		}
		return bS.toByteArray();
	}
	public byte[] convertDelta(int n){
		String bin = Integer.toBinaryString(n);
		String gc = getGC(bin.length());
		String offset = bin.substring(1);
		return toByteArray(gc.concat(offset));
	}
	public void constructCompIdx(Map<String,TermFeatures> idx,String comptype,String encoding,int n){
		StringBuilder tb = new StringBuilder();
		this.idxTermComp = new ArrayList<TermFeatureCompress>();
		List<String> bb = new ArrayList<String>();
		int i=0;
		for(String keys: idx.keySet()){
			TermFeatureCompress tfc = new TermFeatureCompress();
			if(i%n==0){
				int idxForEntry = tb.length();
				if(comptype == "Gamma"){
					tfc.idxT = convertGamma(idxForEntry);	
				}
				if(comptype == "Delta"){
					tfc.idxT = convertDelta(idxForEntry);
				}
				if(encoding == "blocked"){					
					tb.append(constructBlockEncoding(bb));					
				}
				if(encoding == "frontcoding"){					
						
					tb.append(constructFrontEnCoding(bb));
				}
				bb = new ArrayList<String>();
			}
			bb.add(keys);
			if(comptype == "Gamma"){
				tfc.docfreq = convertGamma(idx.get(keys).df);
			}
			if(comptype == "Delta"){
				tfc.docfreq = convertDelta(idx.get(keys).df);
			}
			List<DocTermFeatureCompress> dtfList = new ArrayList<DocTermFeatureCompress>();
			int tempdocId = 0;
			for(Integer dc: idx.get(keys).docFreq.keySet()){
				DocTermFeatureCompress dtf = new DocTermFeatureCompress();
				int docId = dc;
				int docTmFreq = idx.get(keys).docFreq.get(dc);
				int docIdGap = docId - tempdocId;
				tempdocId = docId;
				if(comptype == "Gamma"){
					dtf.docIdgp = convertGamma(docIdGap);
					dtf.dTFreq = convertGamma(docTmFreq);
				}
				if(comptype == "Delta"){
					dtf.docIdgp = convertDelta(docIdGap);
				    dtf.dTFreq = convertDelta(docIdGap);
				}
				dtfList.add(dtf);
			}
			tfc.dTlist = dtfList;
			i++;
		}
		if(encoding == "blocked"){
			tb.append(constructBlockEncoding(bb));					
		}
		if(encoding == "frontcoding"){
			tb.append(constructFrontEnCoding(bb));			
		}
		this.tmstr = tb.toString().getBytes(Charset.forName("UTF-8"));
	}
	public String constructFrontEnCoding(List<String> bb) {
		StringBuilder sb= new StringBuilder();
		String ip = getIdenticalPrefix(bb);
		sb.append(ip.length()+ip+"*");
		for(final String term : bb){
			String lst =term.replaceFirst(ip, "");
			sb.append(lst+lst.length()+"#");
		}
		if(sb.length() < 3){
			return "";
		}
		return sb.toString();
		
	}
	private String getIdenticalPrefix(List<String> bb) {
		StringBuilder sb = new StringBuilder();
		if(bb.size()<1) {			
			return "";
		}			
		gt:
		for(int i=0;i<bb.get(0).length();i++){
			char iden=bb.get(0).charAt(i);
			for(int j=1;j<bb.size();j++){
				if(i<bb.get(j).length()){
					if(bb.get(j).charAt(i) != iden){
						break gt;
					}
				}else{
					break gt;
				}
			}
			sb.append(iden);
		}
		return sb.toString();
		
	}

	public String constructBlockEncoding(List<String> bb) {
		StringBuilder sb= new StringBuilder();
		for(String t : bb){
			sb.append(t.length()+t);
		}
		if(sb.length() < 3){
			return "";
		}
		return sb.toString();
		
	}
	public void constructCompressList(Map<Integer,DocFeatures> dc,String comptype){
		List<DocFeatureCompress> dfc = new ArrayList<DocFeatureCompress>();
		int tempDocId=0;
		for(Integer dcId :dc.keySet()){
			int docId=dcId;
			int mxtf = dc.get(dcId).max_tf;
			int dclen = dc.get(dcId).doclen;
			int dcIDgp = docId - tempDocId;
			tempDocId = docId;
			
			DocFeatureCompress df = new DocFeatureCompress();
			if(comptype =="Gamma"){
				df.docIdgp = convertGamma(dcIDgp);
				df.maxTf = convertGamma(mxtf);
				df.doclen = convertGamma(dclen);
				
			}
			if(comptype == "Delta"){
				df.docIdgp = convertDelta(dcIDgp);
				df.maxTf = convertDelta(mxtf);
				df.doclen = convertDelta(dclen);
			}			
			dfc.add(df);
		}
		this.idxDocComp = dfc;
	}
}
