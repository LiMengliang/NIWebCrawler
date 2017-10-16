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

public class ExamplePageProcessor extends GeneralPageProcessor  {

	private static final String[] ATTACHMENT_URL = new String[] {
			"http:", "", "fpt.ni.com", "pub", "gdc", "tut", "{*}"
	};
	
	public ExamplePageProcessor(TaskService taskService) {
		super(taskService);
	}

	@Override
	public List<Request> getSubRequests(Page page) {
		HashSet<String> urls = new HashSet<String>();
		List<Request> subRequests = new ArrayList<>();
		Elements links = JsoupUtils.selectElements(page, "a[href]");
		for(Element link : links) {
			String href = JsoupUtils.getAttributeValue(link, "href");			
//			if (UrlUtilities.isUrlPatternMatch(href, ATTACHMENT_URL)) {
//				urls.add(href);
//			}
			if (href.contains("http://ftp.ni.com/pub")) {
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
