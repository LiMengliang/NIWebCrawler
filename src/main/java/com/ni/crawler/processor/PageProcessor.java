package com.ni.crawler.processor;

import java.util.List;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;

public interface PageProcessor {
	
	List<Request> getSubRequests(Page page);
	
	Page processContent(Page page);

}
