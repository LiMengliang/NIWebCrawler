package com.ni.crawler.utils;

import java.util.ArrayList;
import java.util.List;

import org.lemurproject.kstem.KrovetzStemmer;

public class StemUtils {
	
	private static final KrovetzStemmer STEMMER = new KrovetzStemmer();

	public static List<String> getStems(String source) {
		List<String> results = new ArrayList<String>(1024);
		String[] splits = source.split(" ");
		for(String split : splits) {
			results.add(STEMMER.stem(split));
		}
		return results;
	}
}
