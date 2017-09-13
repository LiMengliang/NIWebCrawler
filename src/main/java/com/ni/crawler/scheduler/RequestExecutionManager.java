package com.ni.crawler.scheduler;

import com.ni.crawler.model.Request;

public interface RequestExecutionManager {

	void addRequest(Request request);
	
	void start();
	
	void pause();
	
	void stop();
}
