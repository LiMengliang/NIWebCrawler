package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.Log;
import com.ni.crawler.utils.UrlUtilities;

public class ExampleProgramsListProcessor extends GeneralPageProcessor {

	private static final String[] EXAMPLE_URL_PATTERN = new String[] {
			"https:", "", "forums.ni.com", "t5", "Example-Programs", "{*}", "ta-p", "{*}"
	};
	
	private static final String[] NEXT_PAGE_URL_PATTERN = new String[] {
			"https:", "", "forums.ni.com", "t5", "Example-Programs", "tkb-p", "{num}", "page", "{num}"	
	};
	
	public ExampleProgramsListProcessor(TaskService taskService) {
		super(taskService);
	}

	@Override
	public List<Request> getSubRequests(Page page) {
		HashSet<String> urls = new HashSet<String>();
		List<Request> subRequests = new ArrayList<>();
		Elements links = JsoupUtils.selectElements(page, "a[href]");
		for(Element link : links) {
			String href = JsoupUtils.getAttributeValue(link, "href");			
			if (UrlUtilities.isUrlPatternMatch(href, EXAMPLE_URL_PATTERN) ||
					UrlUtilities.isUrlPatternMatch(href, NEXT_PAGE_URL_PATTERN)) {
				urls.add(href);
			}
//			else {
//				Log.consoleWriteLine("Not processed: " + href);
//			}
		}
		for(String url : urls) {
			subRequests.add(new Request(url));
		}
		super.onAnalyzeLinksFinished(page);
		return subRequests;
	}

	@Override
	public Page processContent(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
