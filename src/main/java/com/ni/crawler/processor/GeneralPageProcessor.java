package com.ni.crawler.processor;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.TaskService;

public abstract class GeneralPageProcessor implements PageProcessor {

	private TaskService taskService;
	
	public GeneralPageProcessor(TaskService taskService) {
		
		this.taskService = taskService;
	}
	
	protected void onAnalyzeLinksFinished(Page page) {
		
		taskService.updateStatus(page.getUrl(), 'c');
	}
}
