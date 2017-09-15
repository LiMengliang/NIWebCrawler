package com.ni.crawler.processor;

import com.ni.crawler.model.TaskService;

public interface PageProcessorFactory {
	
	boolean checkCapability(String url);
	
	PageProcessor createPageProcessor(TaskService taskService); 
}
