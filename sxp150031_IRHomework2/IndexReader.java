import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class IndexReader {
	
	public Tokens initiate(String inputPath,String dc) throws IOException {
		
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
	
	public String getTokensListFromDoc(String docName,String inputPath) throws IOException{
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
	
	private String replacePunct(String token) {
		// TODO Auto-generated method stub
		 token = token.replaceAll("[^a-zA-Z]", ""); //ignore numbers
		 return token;
	}
	
	public LinkedList<String> getFileNames(String inputPath){
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
}
