package com.ni.analyze;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.lemurproject.kstem.KrovetzStemmer;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class TextAnalyzer {

	private static final HashSet<String> STOP_WORDS = new HashSet<String>() {{
		add("a"); add("about"); add("above"); add("above"); add("across"); add("after"); add("afterwards"); 
		add("again"); add("against"); add("all"); add("almost"); add("alone"); add("along"); add("already"); 
		add("also"); add("although"); add("always"); add("am"); add("among"); add("amongst"); add("amoungst"); 
		add("amount"); add("an"); add("and"); add("another"); add("any"); add("anyhow"); add("anyone"); 
		add("anything"); add("anyway"); add("anywhere"); add("are"); add("around"); add("as"); add("at"); 
		add("back"); add("be"); add("became"); add("because"); add("become"); add("becomes"); add("becoming");
		add("been"); add("before"); add("beforehand"); add("behind"); add("being"); add("below"); add("beside"); 
		add("besides"); add("between"); add("beyond"); add("bill"); add("both"); add("bottom"); add("but"); add("by"); 
		add("call"); add("can"); add("cannot"); add("cant"); add("co"); add("con"); add("could"); add("couldnt"); 
		add("de"); add("describe"); add("detail"); add("do"); add("done"); add("down"); add("due"); add("during"); 
		add("each"); add("eg"); add("eight"); add("either"); add("eleven"); add("else"); add("elsewhere");
		add("empty"); add("enough"); add("etc"); add("even"); add("ever"); add("every"); add("everyone"); add("everything"); 
		add("everywhere"); add("except"); add("few"); add("fifteen"); add("fify"); add("fill"); add("find"); 
		add("fire"); add("first"); add("five"); add("for"); add("former"); add("formerly"); add("forty"); 
		add("found"); add("four"); add("from"); add("front"); add("full"); add("further"); add("get"); add("give");
		add("go"); add("had"); add("has"); add("hasnt"); add("have"); add("he"); add("hence"); add("her"); add("here");
		add("hereafter"); add("hereby"); add("herein"); add("hereupon"); add("hers"); add("herself"); add("him"); 
		add("himself"); add("his"); add("how"); add("however"); add("hundred"); add("ie"); add("if"); add("in"); 
		add("inc"); add("indeed"); add("interest"); add("into"); add("is"); add("it"); add("its"); add("itself");
		add("keep"); add("last"); add("latter"); add("latterly"); add("least"); add("less"); add("ltd"); add("made");
		add("many"); add("may"); add("me"); add("meanwhile"); add("might"); add("mill"); add("mine"); add("more"); 
		add("moreover"); add("most"); add("mostly"); add("move"); add("much"); add("must"); add("my"); add("myself");
		add("name"); add("namely"); add("neither"); add("never"); add("nevertheless"); add("next"); add("nine"); 
		add("no"); add("nobody"); add("none"); add("noone"); add("nor"); add("not"); add("nothing"); add("now"); 
		add("nowhere"); add("of"); add("off"); add("often"); add("on"); add("once"); add("one"); add("only"); 
		add("onto"); add("or"); add("other"); add("others"); add("otherwise"); add("our"); add("ours");
		add("ourselves"); add("out"); add("over"); add("own"); add("part"); add("per"); add("perhaps"); 
		add("please"); add("put"); add("rather"); add("re"); add("same"); add("see"); add("seem"); add("seemed");
		add("seeming"); add("seems"); add("serious"); add("several"); add("she"); add("should"); add("show"); 
		add("side"); add("since"); add("sincere"); add("six"); add("sixty"); add("so"); add("some"); add("somehow");
		add("someone"); add("something"); add("sometime"); add("sometimes"); add("somewhere"); add("still"); add("such"); 
		add("system"); add("take"); add("ten"); add("than"); add("that"); add("the"); add("their"); add("them"); add("themselves");
		add("then"); add("thence"); add("there"); add("thereafter"); add("thereby"); add("therefore"); add("therein"); 
		add("thereupon"); add("these"); add("they"); add("thickv"); add("thin"); add("third"); add("this"); add("those"); 
		add("though"); add("three"); add("through"); add("throughout"); add("thru"); add("thus"); add("to"); 
		add("together"); add("too"); add("top"); add("toward"); add("towards"); add("twelve"); add("twenty"); 
		add("two"); add("un"); add("under"); add("until"); add("up"); add("upon"); add("us"); add("very"); add("via"); 
		add("was"); add("we"); add("well"); add("were"); add("what"); add("whatever"); add("when"); add("whence");
		add("whenever"); add("where"); add("whereafter"); add("whereas"); add("whereby"); add("wherein"); add("whereupon"); 
		add("wherever"); add("whether"); add("which"); add("while"); add("whither"); add("who"); add("whoever"); add("whole"); 
		add("whom"); add("whose"); add("why"); add("will"); add("with"); add("within"); add("without"); add("would"); add("yet");
		add("you"); add("your"); add("yours"); add("yourself"); add("yourselves"); add("the");
	}};
	
	private static final KrovetzStemmer KROVETZ_STEMMER = new KrovetzStemmer();
	private static final SnowballStemmer SNOWBALL_STEMMER = new englishStemmer();
	
	public List<String> filterStopWords(String[] worlds) {
//		Iterator<String> iterator = worlds.iterator();
//		while(iterator.hasNext()) {
//			String word = iterator.next();
//			if (STOP_WORDS.contains(word)) {
//				iterator.remove();
//			}
//		}
		List<String> filtered = new ArrayList<>();
		for(String word : worlds) {
			if (!STOP_WORDS.contains(word)) {
				filtered.add(word);
			}
		}
		return filtered;
	}
	
	public String toLowerCase(String source) {
		return source.toLowerCase();
	}
	
	public String filterPuncutation(String source) {
		return source.replaceAll("[^a-zA-Z0-9 ]", "");
	}
	
	public String[] splitWithWhiteSpace(String source) {
		return source.split(" ");
	}
	
	public List<String> stem(List<String> words) {
		List<String> stemmed = new ArrayList<>();
		for(String word : words) {
			stemmed.add(KROVETZ_STEMMER.stem(word));			
		}
		return stemmed;
	}
	
	public List<String> combineWithNGram(List<String> terms, int n) {
		List<String> nGramTerms = new ArrayList<>();
		int length = terms.size();
		for(int i = 0; i < length; i++) {
//			int left = i - (n - 1);
			int right = i + (n);
//			if (left >= 0) {
//				List<String> leftNTerms = terms.subList(left, i + 1);
//				nGramTerms.add(stringListToString(leftNTerms));
//			}
			if (right < length) {
				List<String> rightNTerms = terms.subList(i, right);
				nGramTerms.add(stringListToString(rightNTerms));
			}
		}
		nGramTerms.forEach(x -> {
			terms.add(x);
		});
		return nGramTerms;
	}
	
	private String stringListToString(List<String> terms) {
		StringBuilder sb = new StringBuilder();
		for(String term : terms) {
			sb.append(term).append(" ");
		}
		return sb.toString();
	}
	
	public List<String> stemWithSnowball(List<String> words) {
		List<String> stemmed = new ArrayList<>();
		for(String word : words) {
			SNOWBALL_STEMMER.setCurrent(word);
			SNOWBALL_STEMMER.stem();
			stemmed.add(SNOWBALL_STEMMER.getCurrent());			
		}
		return stemmed;
	}
	
	public List<String> analyze(String source) {

		String lowerCase = toLowerCase(source);
		String withoutPuncuation = filterPuncutation(lowerCase);
		String[] splits = splitWithWhiteSpace(withoutPuncuation);
		List<String> removeStopWords = filterStopWords(splits);
		List<String> stemmed = stem(removeStopWords);
		return stemmed;
	}
	
	public List<String> analyzeWithSnowball(String source) {

		String lowerCase = toLowerCase(source);
		String withoutPuncuation = filterPuncutation(lowerCase);
		String[] splits = splitWithWhiteSpace(withoutPuncuation);
		List<String> removeStopWords = filterStopWords(splits);
		List<String> stemmed = stemWithSnowball(removeStopWords);
		return stemmed;
	}
	
	public List<String> analyzeWith2Gram(String source) {

		String lowerCase = toLowerCase(source);
		String withoutPuncuation = filterPuncutation(lowerCase);
		String[] splits = splitWithWhiteSpace(withoutPuncuation);
		List<String> removeStopWords = filterStopWords(splits);
		List<String> stemmed = stem(removeStopWords);
		List<String> grams = combineWithNGram(stemmed, 3);
		return stemmed;
	}
	
}
