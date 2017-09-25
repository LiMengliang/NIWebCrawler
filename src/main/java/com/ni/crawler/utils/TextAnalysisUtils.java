package com.ni.crawler.utils;

import java.util.List;

public class TextAnalysisUtils {

	// TODO: fuzzy match should be used here
	// TODO: remove 
	public static boolean isSpecifiedTag(String text, List<String> tags) {
		String lowerCase = text.toLowerCase();
		StringBuilder textWithoutPunctuation = new StringBuilder();
		for(char c : lowerCase.toCharArray()) {
			if (c == ' ' || (c >= 'a' && c <= 'z')) {
				textWithoutPunctuation.append(c);
			}
		}
		
		return tags.stream().anyMatch(x -> x.equals(textWithoutPunctuation.toString()));
	}
	
	public static String toLowerCaseAndRemovePunctuation(String text) {
		String lowerCase = text.toLowerCase();
		StringBuilder textWithoutPunctuation = new StringBuilder();
		for(char c : lowerCase.toCharArray()) {
			if (c == ' ' || (c >= 'a' && c <= 'z')) {
				textWithoutPunctuation.append(c);
			}
		}
		return lowerCase;
	}
}
