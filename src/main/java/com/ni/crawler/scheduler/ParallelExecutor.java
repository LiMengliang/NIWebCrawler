package com.ni.crawler.scheduler;

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
import com.ni.crawler.utilities.Log;

public class ParallelExecutor implements Executor {
	
//	@Autowired
//	private TaskService taskService;
	
	private ExecutorService threadPool;
	private RequestExecutionManager executionManager;
	private TaskService taskService;
	
	public ParallelExecutor(int parallelNum, RequestExecutionManager executionManager, TaskService taskService) {
		this.threadPool = Executors.newFixedThreadPool(parallelNum);
		this.executionManager = executionManager;
		this.taskService = taskService;
	}

	@Override
	public void acceptRequest(Request request) {
		

		boolean taskUnprocesed = taskService.addUniqueTask(new Task(request.getUrl(), 'b', 'a'));
		if (taskUnprocesed) {
			Future<Object> submit = threadPool.submit(new Runnable() {	
				@Override
				public void run() {
	
					// fetching raw data for request 
					Log.consoleWriteLine((new StringBuilder("Start fetching from ").append(request.getUrl()).append("[").append(Thread.currentThread().getId()).append("]")).toString());
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
							if (subRequest.getUrl() == "http://search.ni.com/nisearch/app/main/p/ap/tech/lang/en/pg/11/ps/30/sn/catnav:ex/") {
								int a = 0;
							}
							executionManager.addRequest(subRequest);
						}
					}
					else {
						taskService.updateStatus(request.getUrl(), 'c');
					}
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
