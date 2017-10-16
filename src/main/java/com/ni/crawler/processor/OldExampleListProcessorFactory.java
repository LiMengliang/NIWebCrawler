package com.ni.crawler.processor;

import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.UrlUtilities;

public class OldExampleListProcessorFactory implements PageProcessorFactory {

	private static final String[][] ACCEPTED_PATTERNS = new String[][] {
		{"http:", "", "search.ni.com", "nisearch", "app", "main", "p", "ap", "tech", "lang", "en", "pg", "{num}", "sn", "{*}"},
		{"http:", "", "search.ni.com", "nisearch", "app", "main", "p", "bot", "no", "ap", "tech", "lang", "en", "pg", "{num}", "sn", "{*}"}
	};
	
	@Override
	public boolean checkCapability(String url) {
		return UrlUtilities.isUrlPatternMatch(url, ACCEPTED_PATTERNS) && url.contains("catnav:ex");
	}

	@Override
	public PageProcessor createPageProcessor(TaskService taskService) {
		// TODO Auto-generated method stub
		return new OldExampleListProcessor(taskService);
	}

}
