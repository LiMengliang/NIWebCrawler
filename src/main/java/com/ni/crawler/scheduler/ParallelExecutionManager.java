package com.ni.crawler.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;

public class ParallelExecutionManager extends NoneDuplicateRequestExecutionManager {

	private Executor paraExecutor;
	private HashSet<String> visitedUrls; 
	private List<Request> requests;
	private AtomicBoolean started = new AtomicBoolean(false);
	
	
	public ParallelExecutionManager(int parallelNum, TaskService taskService) {
		paraExecutor = new ParallelExecutor(parallelNum, this, taskService);
		requests = new ArrayList<>();
		visitedUrls = new HashSet<>();
	}
	
	@Override
	protected void addWithoutDuplication(Request request) {
		if (!visitedUrls.contains(request.getUrl())) {
			visitedUrls.add(request.getUrl());
			if (!started.get()) {
				requests.add(request);
			}
			else {
				paraExecutor.acceptRequest(request);
			}
		}
	}

	@Override
	public void start() {
		started.set(true);
		for(Request request : requests) {
			paraExecutor.acceptRequest(request);
		}
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		started.set(false);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		started.set(false);
	}

}
