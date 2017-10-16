package com.ni.crawler.processor;

import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.UrlUtilities;

public class ExampleProgramsListProcessorFactory implements PageProcessorFactory  {

	private final static String[][] ACCAPTED_PATTERNs = new String[][] {
		{"https:", "", "forums.ni.com", "t5", "Example-Programs", "tkb-p", "{num}"},
		{"https:", "", "forums.ni.com", "t5", "Example-Programs", "tkb-p", "{num}", "page", "{num}"}
	};
	
	@Override
	public boolean checkCapability(String url) {
		return UrlUtilities.isUrlPatternMatch(url, ACCAPTED_PATTERNs);
	}

	@Override
	public PageProcessor createPageProcessor(TaskService taskService) {
		return new ExampleProgramsListProcessor(taskService);
	}
}
