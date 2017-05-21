import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Homework2 {
	public static void main(String args[]) throws IOException{
		
	    String inputPath;
	    String stopWordPath;
		
		if(args.length < 1 ){
			inputPath = "/Users/shobhikapanda/Documents/InformationRetrieval/IRHomeWork2/src/Cranfield/";	
			stopWordPath = "/Users/shobhikapanda/Documents/InformationRetrieval/IRHomeWork2/src/resourcesIR/stopwords";
		} else {
			inputPath = args[0].toString();	
			stopWordPath = args[1].toString();
		}
		long startTime = System.currentTimeMillis();
		Indexing indexCon = new Indexing(inputPath,stopWordPath);
		indexCon.constructIndex();
		long endTime = System.currentTimeMillis();
		long timeConstructIdx = endTime-startTime;
		String fileName="V1_uncompressed";
		//long startWrite = Calendar.getInstance().getTimeInMillis();
		WriteOutput.constructUncompressedFile(fileName,
				indexCon.getLemIdx(), indexCon.getLemTreeMap());
		DisplayResults.showIdxRes(timeConstructIdx,indexCon.getLemIdx().size());
		
		fileName="V2_uncompressed";
		//long startWrite = Calendar.getInstance().getTimeInMillis();
		WriteOutput.constructUncompressedFile(fileName,
				indexCon.getstmIdx(), indexCon.getStmTreeMap());
		DisplayResults.showIdxRes(timeConstructIdx,indexCon.getstmIdx().size());
		
		fileName ="V1_compressed";
		startTime = System.currentTimeMillis();
		WriteOutput.constructCompressedFile(fileName, indexCon.getLemIdx(), indexCon.getLemTreeMap(), "Gamma", "blocked", 8);
		endTime = System.currentTimeMillis();
		DisplayResults.showIdxRes(endTime-startTime,indexCon.getLemIdx().size());
		
		fileName ="V2_compressed";
		startTime = System.currentTimeMillis();
		WriteOutput.constructCompressedFile(fileName, indexCon.getstmIdx(), indexCon.getStmTreeMap(), "Delta", "frontcoding", 8);
		endTime = System.currentTimeMillis();
		DisplayResults.showIdxRes(endTime-startTime,indexCon.getstmIdx().size());
		System.out.println("--section-break--");
		Set<String> listTerms = new HashSet<String>();
		listTerms.add("reynolds");
		listTerms.add("nasa");
		listTerms.add("prandtl");
		listTerms.add("flow");
		listTerms.add("pressure");
		listTerms.add("boundary");
		listTerms.add("shock");
		
		Set<String> lem = DisplayResults.constructLemma(listTerms);
		System.out.println("Lemma Stats");
		DisplayResults.showStats(lem,indexCon.getLemIdx());
		System.out.println();
		DisplayResults.showNasaStats("nasa", indexCon.getLemIdx(), indexCon.getLemTreeMap());
		System.out.println();
		DisplayResults.showDFStats(indexCon.getLemIdx());
		System.out.println();
		DisplayResults.showTFStats(indexCon.getLemTreeMap());
		System.out.println("--section-break--");
		Set<String> stem = DisplayResults.constructStem(listTerms);
		System.out.println("Stem stats Stats");
		DisplayResults.showStats(stem,indexCon.getstmIdx());
		System.out.println();
		DisplayResults.showNasaStats("nasa", indexCon.getstmIdx(),indexCon.getStmTreeMap());
		System.out.println();
		DisplayResults.showDFStats(indexCon.getstmIdx());
		System.out.println();
		DisplayResults.showTFStats(indexCon.getStmTreeMap());
		
		
	}
}
