package com.ni.crawler.processor;

import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.UrlUtilities;

public class ForumTopicProcessorFactory implements PageProcessorFactory {

	private static final String[][] ACCEPTED_PATTERNS = new String[][] {
		{"https:", "", "forums.ni.com", "t5", "{*}", "{*}", "td-p", "{*}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "{*}", "ta-p", "{*}"}
	};
	
	@Override
	public boolean checkCapability(String url) {
		
		return UrlUtilities.isUrlPatternMatch(url, ACCEPTED_PATTERNS);
	}

	@Override
	public PageProcessor createPageProcessor(TaskService taskService) {
		
		return new ForumTopicProcessor(taskService);
	}

}
