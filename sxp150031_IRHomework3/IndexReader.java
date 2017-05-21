import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class IndexReader {
	private static Pattern pattern = Pattern.compile("<.?title>",Pattern.CASE_INSENSITIVE);
	public static Tokens initiate(String inputPath,String dc) throws IOException {
		
		List<String> listOfWords = new ArrayList<String>();
		Tokens tokObj = new Tokens();
		
		
			String docID = dc.replaceAll("[^\\d]", "");
			tokObj.docID = Integer.parseInt(docID);
			String textContent = getTokensListFromDoc(dc,inputPath);
			String [] textContentTokens = textContent.split(" ");
			for(String words : textContentTokens){
				if(words.endsWith("'s"))
					words = words.replace("'s", "");
				words=replacePunct(words);
				if(words.length()>0){
					listOfWords.add(words);
				}
			}
	
		tokObj.tokens = listOfWords;
		return tokObj;	
		
		
	}
	public static List<String> getToksFromQuery(StringTokenizer dc) {
		
		List<String> tokens = new ArrayList<String>();
		while(dc.hasMoreElements()){
			String term=dc.nextToken();
			//Converts Possessives - university's to university
			if(term.endsWith("'s")){
				term=term.replace("'s", "");
			}
			//Remove all characters apart from characters and digits
			term=term.replaceAll("[^a-zA-Z0-9]", "");
			if(term.length()>0){
				tokens.add(term);						
			}
	}
	return tokens;		
	}
	
	public static Tokens getQToks(String q) throws IOException{
		StringTokenizer t=new StringTokenizer(q);
		Tokens tokObj = new Tokens();		
		tokObj.tokens = getToksFromQuery(t);
		return tokObj;
	}
	


	public static String getTokensListFromDoc(String docName,String inputPath) throws IOException{
		StringBuffer textContent = new StringBuffer();
		//List<Word> tempList = new ArrayList<>();
		
	    BufferedReader br;
		try {
				br = new BufferedReader(new FileReader(inputPath+docName));
			  	String line;
			    while( (line = br.readLine()) != null){
				    if(!(line.contains("<") && line.contains(">"))){
				    	 String [] tokens = line.split("\\s+");
					      for (String token : tokens) {
					    	 // token = replacePunct(token);
					    	  token = token.replaceAll("[-]"," ").toLowerCase();
					    	  textContent.append(token+" ");
					      }
				    }
			    }
			    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return textContent.toString();
		
		
	}
	
	private static String replacePunct(String token) {
		// TODO Auto-generated method stub
		 token = token.replaceAll("[^a-zA-Z0-9]", ""); //ignore numbers
		 return token;
	}
	
	public static LinkedList<String> getFileNames(String inputPath){
		LinkedList<String> fileNames = new LinkedList<>();
		//System.out.println(inputPath);
		try{
			File folder = new File(inputPath);
			File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  fileNames.add(listOfFiles[i].getName());
		      } 
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return fileNames;
	}
	public static double calcW1(int tf, int max_tf, int df, long csize){
		double temp = 0.0;
		if(df == 0){
			return temp;
		}
		try {
			temp = ( 0.4 + 0.6 * Math.log (tf + 0.5) / Math.log (max_tf + 1.0) ) *  (Math.log(csize / (double)df) / Math.log(csize)) ;
		} catch (Exception e) {
			temp = 0.0;
		}
		return temp;
	}

	public static double  calcW2(int tf, int doclen, double avgdoclen, int df, long csize){
		double temp = 0.0;
		if(df == 0){
			return temp;
		}
		try {
			temp = (0.4 + 0.6 * (tf / (tf + 0.5 + 1.5 * (doclen / avgdoclen))) * Math.log (csize / (double)df) / Math.log(csize) );
			
		} catch (Exception e) {
			//e.printStackTrace();
			temp = 0.0;
		}		
		return temp;
	}
	public static String getTitle(String inputPath,String docN) {
		try {
			File file = new File(inputPath+docN);
			String data = new String(Files.readAllBytes(file.toPath()));
			String[] parts= pattern.split(data);
			if(parts.length>1){
				return parts[1].replace("\n", " ");
			}else 
				System.out.println("...."+file.getPath());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
