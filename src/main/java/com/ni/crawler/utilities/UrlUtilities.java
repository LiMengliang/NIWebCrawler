package com.ni.crawler.utilities;

import java.util.List;

public class UrlUtilities {
	
	public static boolean isUrlPatternMatch(String url, String[] pattern) {
		
		String[] subsets = url.split("/");
		if (subsets.length != pattern.length) {
			return false;
		}
		for(int i = 0; i < subsets.length; i++) {
			String expected = pattern[i];
			String actual = subsets[i];
			if (expected != "{*}" && !expected.equals(actual)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isUrlPatternMatch(String url, String[][] patterns) {
		for(String[] pattern : patterns) {
			if (isUrlPatternMatch(url, pattern)) {
				return true;
			}
		}
		return false;
	}
}
