package com.ni.crawler.processor;

import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.UrlUtilities;

public class ExamplePageProcessorFactory implements PageProcessorFactory {

	private static final String[] EXAMPLE_URL_PATTERN = new String[] {
			"http:", "", "www.ni.com", "example", "{num}", "en"
	};
	@Override
	public boolean checkCapability(String url) {
		return UrlUtilities.isUrlPatternMatch(url, EXAMPLE_URL_PATTERN);
	}

	@Override
	public PageProcessor createPageProcessor(TaskService taskService) {
		// TODO Auto-generated method stub
		return new ExamplePageProcessor(taskService);
	}

}
