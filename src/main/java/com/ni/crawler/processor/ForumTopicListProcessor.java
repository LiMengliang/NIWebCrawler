package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.UrlUtilities;

public class ForumTopicListProcessor extends GeneralPageProcessor  {
	
	public ForumTopicListProcessor(TaskService taskService) {
		
		super(taskService);
	}

	private final String forumUrl = "https://forums.ni.com";
	private final String[][] topicLinkPattern = new String[][] {
		{"", "t5", "{*}", "{*}", "td-p", "{num}"},
		{"", "t5", "{*}", "{*}", "ta-p", "{num}"},
		{"", "t5", "{*}", "{*}", "ba-p", "{num}"},
		{"", "t5", "{*}", "{*}", "gpm-p", "{num}"},
		
	};
	private final String[][] nextPageLinkPattern = new String[][] {
		{"https:", "", "forums.ni.com", "t5", "{*}", "bd-p", "{*}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "bd-p", "{*}", "page", "{num}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "bg-p", "{*}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "bg-p", "{*}", "page", "{num}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "ct-p", "{*}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "ct-p", "{*}", "page", "{num}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "gp-p", "{*}"},
		{"https:", "", "forums.ni.com", "t5", "{*}", "gp-p", "{*}", "page", "{num}"}
	};

	
	// TODO: need to get rid of duplicated urls.
	@Override
	public List<Request> getSubRequests(Page page) {
				
		List<Request> subRequests = new ArrayList<>();
		
		Elements links = JsoupUtils.selectElements(page, "a[href]");
		for(Element link : links) {
			String href = JsoupUtils.getAttributeValue(link, "href");			
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
