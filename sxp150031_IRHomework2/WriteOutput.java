import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;





public class WriteOutput {
	

	public static void constructUncompressedFile(String fileName, Map<String, TermFeatures> Idx,
			Map<Integer, DocFeatures> tMap) throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(fileName);
		int idxLen = 0;
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		for(String keys:Idx.keySet()){
			StringBuilder sb = new StringBuilder();
			idxLen += Character.SIZE * keys.length();
			writer.print(keys + "-" + Idx.get(keys).df +
					","+Idx.get(keys).tf+
					" {");
			idxLen += Integer.SIZE; //  doc freq
			Map<Integer,Integer> docTFreq = Idx.get(keys).docFreq;
			for(Integer docIds: docTFreq.keySet()){
				sb.append(docIds+"-"+docTFreq.get(docIds)+",");
				idxLen += Integer.SIZE*2; // docId,term freq
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("}");
			writer.println(sb.toString());
		}
		writer.println("doc features");
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Integer keys: tMap.keySet()){
			sb.append(keys+"-"+tMap.get(keys).max_tf+","+tMap.get(keys).mostFrequentTerm+","+tMap.get(keys).doclen+"$");
			idxLen += Integer.SIZE * 3;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		writer.println(sb.toString());
		System.out.println("Uncompressed Index Size:"+idxLen+"bytes");
		System.out.println(file.length());
		//return file.length();
	}
	public static void constructCompressedFile(String fileName, Map<String, TermFeatures> Idx,
			Map<Integer, DocFeatures> tMap,String comptype,String encoding, int n) throws IOException {
		Compression compObj = new Compression();
		compObj.constructCompIdx(Idx, comptype, encoding, n);
		compObj.constructCompressList(tMap, comptype);
		int idxLen = 0;
		FileOutputStream fobj = new FileOutputStream(fileName);
		fobj.write(compObj.tmstr);
		idxLen += compObj.tmstr.length;
		for(Compression.TermFeatureCompress tfc : compObj.idxTermComp){
			fobj.write(tfc.idxT);
			idxLen += tfc.idxT.length;
			fobj.write(tfc.docfreq);
			idxLen += tfc.docfreq.length;
			for(Compression.DocTermFeatureCompress dtf:tfc.dTlist){
				fobj.write(dtf.docIdgp);
				idxLen+= dtf.docIdgp.length;
				fobj.write(dtf.dTFreq);
				idxLen+= dtf.dTFreq.length;
			}
			
		}
		for(Compression.DocFeatureCompress dfc: compObj.idxDocComp){
			fobj.write(dfc.docIdgp);
			idxLen+= dfc.docIdgp.length;
			fobj.write(dfc.maxTf);
			idxLen+= dfc.maxTf.length;
			fobj.write(dfc.doclen);
			idxLen+= dfc.doclen.length;
			
			
		}
		fobj.close();
		File file = new File(fileName);
		System.out.println("Compressed Index Size:"+idxLen+" bytes");
	}
	
	
}
