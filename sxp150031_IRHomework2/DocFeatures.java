import java.util.Map;

public class DocFeatures {
	
		int doclen; // total number of word occurrences , includes the number of stop words encountered in the respective document
		int max_tf =-1; // frequency of most frequent term or stem for each document
		String mostFrequentTerm;
		
		public void findMostFrequentTerm(Map<String,Integer> map){
			int max = -1;
			String mostFreqTerm ="";
			for(String word:map.keySet()){
				int maxFreq = map.get(word);
				if(maxFreq >max){
					max = maxFreq;
					mostFreqTerm = word;
				}
				
			}
			this.max_tf = max;
			this.mostFrequentTerm = mostFreqTerm;
			
		}
}
