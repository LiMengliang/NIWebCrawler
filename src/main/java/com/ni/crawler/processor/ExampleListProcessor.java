package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utilities.JsoupUtilities;
import com.ni.crawler.utilities.Log;
import com.ni.crawler.utilities.UrlUtilities;
import com.ni.crawler.processor.GeneralPageProcessor;

public class ExampleListProcessor extends GeneralPageProcessor {

	public ExampleListProcessor(TaskService taskService) {
		super(taskService);
	}

	private static final String[] EXAMPLE_URL_PATTERN = new String[] {
			"https:", "", "forums.ni.com", "t5", "Example-Programs", "{*}", "ta-p", "{*}"
	};
	
	private static final String[] EXAMPLE_DRAFT_URL_PATTERN = new String[] {
			"https:", "", "forums.ni.com", "t5", "Example-Program-Drafts", "{*}", "ta-p", "{*}"
	};
	
	private static final String[] NEXT_PAGE_URL_PATTERN = new String[] {
			"http:", "", "search.ni.com", "nisearch", "app", "main", "p", "ap", "tech", "lang", "{*}", "pg", "{*}", "ps", "30", "sn", "catnav:ex"
	};
	
	@Override
	public List<Request> getSubRequests(Page page) {

		List<Request> subRequests = new ArrayList<>();
		if (page.getUrl().equals("http://search.ni.com/nisearch/app/main/p/ap/tech/lang/en/pg/10/ps/30/sn/catnav:ex/ ")) {
			int a= 0;
		}
		Elements links = JsoupUtilities.selectElements(page, "a[href]");
		for(Element link : links) {
			String href = JsoupUtilities.getAttributeValue(link, "href");			
			if (UrlUtilities.isUrlPatternMatch(href, EXAMPLE_URL_PATTERN) ||
					UrlUtilities.isUrlPatternMatch(href, EXAMPLE_DRAFT_URL_PATTERN) ||
					UrlUtilities.isUrlPatternMatch(href, NEXT_PAGE_URL_PATTERN)) {
				subRequests.add(new Request(href));
			}
			else {
				Log.consoleWriteLine("Not processed: " + href);
			}
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