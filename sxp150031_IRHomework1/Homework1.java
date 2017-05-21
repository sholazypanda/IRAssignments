

import java.io.IOException;

public class Homework1 {
	public static String inputPath;
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		if(args.length < 1 ){
			inputPath = "/Users/shobhikapanda/Documents/InformationRetrieval/IRProj1/src/Cranfield/";			
		} else {
			inputPath = args[0].toString();			
		}
		Tokenize tokObj = new Tokenize(inputPath,startTime);
		tokObj.initiate();
		tokObj.showResults();
		long sTime = System.currentTimeMillis();
		ParSecStemmer stemObj = new ParSecStemmer(tokObj,sTime);
		stemObj.fillStemDict();
		stemObj.showResults();
		
	}

}
