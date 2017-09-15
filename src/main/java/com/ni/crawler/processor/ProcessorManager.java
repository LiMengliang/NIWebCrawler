package com.ni.crawler.processor;

import java.util.ArrayList;
import java.util.List;

import com.ni.crawler.model.TaskService;

public class ProcessorManager {

	private final static List<PageProcessorFactory> processorFactories = new ArrayList<>();
	
	private final static ProcessorManager singleProcessorManager = new ProcessorManager();
	
	public static ProcessorManager me() {
		return singleProcessorManager;
	}
	
	public ProcessorManager registerProcessorFactory(PageProcessorFactory factory) {
		processorFactories.add(factory);
		return this;
	}
	
	public PageProcessor getBestProcessor(String url, TaskService taskService) {
		for(PageProcessorFactory factory : processorFactories) {
			if (factory.checkCapability(url)) {
				return factory.createPageProcessor(taskService);
			}
		}
		return null;
	}
	
}
