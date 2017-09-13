package com.ni.crawler;

import java.util.List;

import com.ni.crawler.model.Request;
import com.ni.crawler.processor.ProcessorManager;
import com.ni.crawler.processor.ForumTopicListProcessorFactory;
import com.ni.crawler.processor.ForumTopicProcessorFactory;
import com.ni.crawler.scheduler.ParallelExecutionManager;
import com.ni.crawler.scheduler.RequestExecutionManager;

public class Crawler {
	
	private RequestExecutionManager executionManager = new ParallelExecutionManager(10);
	
	private static Crawler singleInstanceCrawler = new Crawler();
	
	public Crawler() {
		ProcessorManager.me().
			registerProcessorFactory(new ForumTopicListProcessorFactory()).
			registerProcessorFactory(new ForumTopicProcessorFactory());
	}
	
	public static Crawler me() {
		return singleInstanceCrawler;
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
