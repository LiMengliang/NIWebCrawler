package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utilities.JsoupUtilities;
import com.ni.crawler.utilities.UrlUtilities;

public class ForumTopicListProcessor extends GeneralPageProcessor  {
	
	public ForumTopicListProcessor(TaskService taskService) {
		
		super(taskService);
	}

	private final String forumUrl = "https://forums.ni.com";
	private final String[] topicLinkPattern = new String[] {
		"", "t5", "{*}", "{*}", "td-p", "{*}"
	};
	private final String[] nextPageLinkPattern = new String[] {
		"https:", "", "forums.ni.com", "t5", "{*}", "bd-p", "{*}", "page", "{*}"
	};
	
	@Override
	public List<Request> getSubRequests(Page page) {
				
		List<Request> subRequests = new ArrayList<>();
		
		Elements links = JsoupUtilities.selectElements(page, "a[href]");
		for(Element link : links) {
			String href = JsoupUtilities.getAttributeValue(link, "href");			
			if (UrlUtilities.isUrlPatternMatch(href, topicLinkPattern)) {
				subRequests.add(new Request(buildTopicUrl(href)));
			}
			else if (UrlUtilities.isUrlPatternMatch(href, nextPageLinkPattern)) {
				subRequests.add(new Request(href));
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

	private String buildTopicUrl(String relativeTopicUrl) {
		return forumUrl + relativeTopicUrl;
	}
}
