package com.ni.crawler.scheduler;

import com.ni.crawler.model.Request;

public abstract class NoneDuplicateRequestExecutionManager implements RequestExecutionManager {
		
	@Override
	public void addRequest(Request request) {
		addWithoutDuplication(request);
	}
	
	protected void addWithoutDuplication(Request request) {
		
	}
}
