package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.Log;
import com.ni.crawler.utils.UrlUtilities;

public class OldExampleListProcessor extends GeneralPageProcessor  {

	private static final String[] NEXT_PAGE_URL = new String[] {
			"http:", "", "search.ni.com", "nisearch", "app", "main", "p", "bot", "no", "ap", "tech", "lang", "en", "pg", "{num}", "sn", "{*}"
	};
	
	private static final String[] EXAMPLE_URL = new String[] {
			"http:", "", "www.ni.com", "example", "{num}", "en"
	};
	
	public OldExampleListProcessor(TaskService taskService) {
		super(taskService);
	}

	@Override
	public List<Request> getSubRequests(Page page) {
		HashSet<String> urls = new HashSet<String>();
		List<Request> subRequests = new ArrayList<>();
		Element pagination = JsoupUtils.getElementByClass(Jsoup.parse(page.getPageContent()), "grid12");
		Elements links = JsoupUtils.selectElements(pagination, "a[href]");
		for(Element link : links) {
			String href = JsoupUtils.getAttributeValue(link, "href");			
			if ((UrlUtilities.isUrlPatternMatch(href, NEXT_PAGE_URL) && href.contains("catnav:ex")) ||
					UrlUtilities.isUrlPatternMatch(href, EXAMPLE_URL)) {
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
