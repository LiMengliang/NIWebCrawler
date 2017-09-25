package com.ni.crawler.scheduler;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import com.ni.NiSpiderApplication;
import com.ni.crawler.downloader.Downloader;
import com.ni.crawler.downloader.HttpClientDownloader;
import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.Task;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.processor.PageProcessor;
import com.ni.crawler.processor.ProcessorManager;
import com.ni.crawler.utils.Log;

public class ParallelExecutor implements Executor {
	
//	@Autowired
//	private TaskService taskService;
	
	private ExecutorService threadPool;
	private RequestExecutionManager executionManager;
	private TaskService taskService;
	private HashSet<String> processingUrls = new HashSet<>();
	
	public ParallelExecutor(int parallelNum, RequestExecutionManager executionManager, TaskService taskService) {
		this.threadPool = Executors.newFixedThreadPool(parallelNum);
		this.executionManager = executionManager;
		this.taskService = taskService;
	}

	@Override
	public void acceptRequest(Request request) {
		

		boolean taskUnprocesed = taskService.addUniqueTask(new Task(request.getUrl(), 'b', 'a')) && !processingUrls.contains(request.getUrl());
		if (taskUnprocesed) {
			processingUrls.add(request.getUrl());
			Future<Object> submit = threadPool.submit(new Runnable() {	
				@Override
				public void run() {
	
					// fetching raw data for request 
					Downloader httpDownloader = new HttpClientDownloader(taskService);
					Page page = httpDownloader.download(request);
					
					char status = taskService.getStatus(request.getUrl());
					
					if(status != 'b') {
						return;
					}
					// get sub requests
					PageProcessor pageProcessor = ProcessorManager.me().getBestProcessor(request.getUrl(), taskService);
					if (pageProcessor != null) {					
						List<Request> subRequests = pageProcessor.getSubRequests(page);
						for(Request subRequest : subRequests) {
							executionManager.addRequest(subRequest);
						}
					}
					else {
						taskService.updateStatus(request.getUrl(), 'c');
					}
					processingUrls.remove(request.getUrl());
				}
				
			}, null);
		}
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
