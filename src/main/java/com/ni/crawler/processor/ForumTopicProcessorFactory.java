package com.ni.crawler.processor;

import com.ni.crawler.utilities.UrlUtilities;

public class ForumTopicProcessorFactory implements PageProcessorFactory {

	private static final String[] ACCEPTED_PATTERN = new String[] {
			"https:", "", "forums.ni.com", "t5", "{*}", "{*}", "td-p", "{*}"
	};
	
	@Override
	public boolean checkCapability(String url) {
		
		return UrlUtilities.isUrlPatternMatch(url, ACCEPTED_PATTERN);
	}

	@Override
	public PageProcessor createPageProcessor() {
		
		return new ForumTopicProcessor();
	}

}
