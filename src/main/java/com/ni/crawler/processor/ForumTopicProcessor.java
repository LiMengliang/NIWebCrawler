package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.UrlUtilities;

public class ForumTopicProcessor extends GeneralPageProcessor  {
	
	public ForumTopicProcessor(TaskService taskService) {
		super(taskService);
	}

	private static final String FORUM_URL = "https://forums.ni.com";
	private static final String[] ATTACHMENT_URL_PATTERN = new String[] {
			"", "ni", "attachments", "ni", "{*}", "{*}", "{*}", "{*}"
	};

	@Override
	public List<Request> getSubRequests(Page page) {
		List<Request> subRequests = new ArrayList<>();
		
		Elements links = JsoupUtils.selectElements(page, "a[href]");
//		for(Element link : links) {
//			String href = JsoupUtils.getAttributeValue(link, "href");
//			if (UrlUtilities.isUrlPatternMatch(href, ATTACHMENT_URL_PATTERN)) {
//				subRequests.add(new Request(buildAttachmentUrl(href)));
//			}
//		}		
		super.onAnalyzeLinksFinished(page);
		return subRequests;
	}
	
	@Override
	public Page processContent(Page page) {
		return page;
	}
	
	private String buildAttachmentUrl(String relativeAttachmentUrl) {
		return (new StringBuilder(FORUM_URL).append(relativeAttachmentUrl)).toString();
	}
}
