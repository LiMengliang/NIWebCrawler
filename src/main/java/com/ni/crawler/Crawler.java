package com.ni.crawler;

import java.util.List;

import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.processor.ProcessorManager;
import com.ni.crawler.processor.ExampleListProcessorFactory;
import com.ni.crawler.processor.ExamplePageProcessorFactory;
import com.ni.crawler.processor.ExampleProgramsListProcessorFactory;
import com.ni.crawler.processor.ForumTopicListProcessorFactory;
import com.ni.crawler.processor.ForumTopicProcessorFactory;
import com.ni.crawler.processor.OldExampleListProcessorFactory;
import com.ni.crawler.scheduler.ParallelExecutionManager;
import com.ni.crawler.scheduler.RequestExecutionManager;

public class Crawler {
	
	private RequestExecutionManager executionManager;
	
	// private static Crawler singleInstanceCrawler = new Crawler();
	
	public Crawler(TaskService taskService) {
		executionManager = new ParallelExecutionManager(10, taskService);
		ProcessorManager.me().
			registerProcessorFactory(new ForumTopicListProcessorFactory()).
			registerProcessorFactory(new ForumTopicProcessorFactory()).
			// registerProcessorFactory(new ExampleListProcessorFactory());
			registerProcessorFactory(new ExamplePageProcessorFactory()).
			registerProcessorFactory(new ExampleProgramsListProcessorFactory()).
			registerProcessorFactory(new OldExampleListProcessorFactory());	
		
	}
	
	public Crawler addSeedUrl(String url) {
		Request request = new Request(url);
		executionManager.addRequest(request);
		return this;
	}
	
	public Crawler addSeedUrls(List<String> urls) {
		for(String url : urls) {
			Request request = new Request(url);
			executionManager.addRequest(request);
		}
		return this;
	}
	
	public void start() {
		executionManager.start();
	}
}
