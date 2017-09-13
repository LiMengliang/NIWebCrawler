package com.ni.crawler.scheduler;

import com.ni.crawler.model.Request;

public interface Executor {
	
	void acceptRequest(Request request);

	void start();
	
	void pause();
	
	void stop();
}
