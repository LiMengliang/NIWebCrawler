package com.ni.crawler.processor;

import org.assertj.core.api.UrlAssert;

import com.ni.crawler.model.TaskService;
import com.ni.crawler.utilities.UrlUtilities;

public class ExampleListProcessorFactory implements PageProcessorFactory {

	private static final String[] ACCEPTED_URL_PATTERN = new String[] {
			"http:", "", "search.ni.com", "nisearch", "app", "main", "p", "ap", "tech", "lang", "{*}", "pg", "{*}", "ps", "30", "sn", "catnav:ex"
	};
	@Override
	public boolean checkCapability(String url) {
		
		return UrlUtilities.isUrlPatternMatch(url, ACCEPTED_URL_PATTERN);
	}

	@Override
	public PageProcessor createPageProcessor(TaskService taskService) {
		
		return new ExampleListProcessor(taskService);
	}

}
