package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.UrlAssert;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.Log;
import com.ni.crawler.utils.UrlUtilities;

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
