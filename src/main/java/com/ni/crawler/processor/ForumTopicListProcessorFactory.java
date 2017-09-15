package com.ni.crawler.processor;

import com.ni.crawler.model.TaskService;
import com.ni.crawler.utilities.UrlUtilities;

public class ForumTopicListProcessorFactory implements PageProcessorFactory {
	
	private final static String[][] ACCAPTED_PATTERNs = new String[][] {
			{"https:", "", "forums.ni.com", "t5", "{*}", "bd-p", "{*}"},
			{"https:", "", "forums.ni.com", "t5", "{*}", "bd-p", "{*}", "page", "{*}"}
	};

	@Override
	public boolean checkCapability(String url) {
		
		return UrlUtilities.isUrlPatternMatch(url, ACCAPTED_PATTERNs);
	}

	@Override
	public PageProcessor createPageProcessor(TaskService taskService) {

		return new ForumTopicListProcessor(taskService);
	}

}
