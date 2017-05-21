import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;


public class Lemmatize {
	StanfordCoreNLP pipeline;
	public Lemmatize(){
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		this.pipeline = new StanfordCoreNLP(props);
	}

	public List<String> doLemmatization(String doc){
		List<String> lText = new LinkedList<String>();
		Annotation docAnnotate = new Annotation(doc);
		this.pipeline.annotate(docAnnotate);
		List<CoreMap> sentences = docAnnotate.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				lText.add(token.get(LemmaAnnotation.class));
			}
		}
		return lText;
	}

	
}
