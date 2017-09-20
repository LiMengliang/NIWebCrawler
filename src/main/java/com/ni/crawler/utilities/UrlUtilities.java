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
			if (expected.equals("{*}")) {
				continue;
			} 
			else if (expected.equals("{num}")) {
				if (!isNumber(actual)) {
					return false;
				}
			}
			else if (!expected.equals(actual)){
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
	
	private static boolean isNumber(String string) {
	    if (string == null || string.isEmpty()) {
	        return false;
	    }
	    int i = 0;
	    if (string.charAt(0) == '-') {
	        if (string.length() > 1) {
	            i++;
	        } else {
	            return false;
	        }
	    }
	    for (; i < string.length(); i++) {
	        if (!Character.isDigit(string.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
	}

}
