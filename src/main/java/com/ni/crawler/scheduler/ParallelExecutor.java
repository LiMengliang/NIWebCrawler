package com.ni.crawler.scheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ni.crawler.downloader.Downloader;
import com.ni.crawler.downloader.HttpClientDownloader;
import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.processor.PageProcessor;
import com.ni.crawler.processor.ProcessorManager;
import com.ni.crawler.utilities.Log;

public class ParallelExecutor implements Executor {
	
	private ExecutorService threadPool;
	private RequestExecutionManager executionManager;
	
	
	public ParallelExecutor(int parallelNum, RequestExecutionManager executionManager) {
		this.threadPool = Executors.newFixedThreadPool(parallelNum);
		this.executionManager = executionManager;
	}

	@Override
	public void acceptRequest(Request request) {
		Future<Object> submit = threadPool.submit(new Runnable() {

			@Override
			public void run() {
				// fetching raw data for request 
				Log.consoleWriteLine((new StringBuilder("Start fetching from ").append(request.getUrl())).toString());
				Downloader httpDownloader = new HttpClientDownloader();
				Page page = httpDownloader.download(request);
				
				// get sub requests
				PageProcessor pageProcessor = ProcessorManager.me().getBestProcessor(request.getUrl());
				List<Request> subRequests = pageProcessor.getSubRequests(page);
				for(Request subRequest : subRequests) {
					executionManager.addRequest(subRequest);
				}
			}
			
		}, null);
		boolean isDone = submit.isDone();
		
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
