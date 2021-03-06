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
import com.ni.crawler.downloader.FtpClientDownloader;
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
		
		Log.consoleWriteLine("[Try to Submit] " + request.getUrl());
		boolean taskUnprocesed = !processingUrls.contains(request.getUrl()) && taskService.addUniqueTask(new Task(request.getUrl(), 'b', 'a'));
		if (taskUnprocesed) {
			processingUrls.add(request.getUrl());
			Log.consoleWriteLine("[Submit] " + request.getUrl());
			Future<Object> submit = threadPool.submit(new Runnable() {	
				@Override
				public void run() {
					Downloader downloader = null;
					if (request.getUrl().contains("ftp.ni.com")) {
						try {
							downloader = new FtpClientDownloader(taskService);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						downloader = new HttpClientDownloader(taskService); 
					}
					
					// fetching raw data for request 
					// Downloader httpDownloader = new HttpClientDownloader(taskService);
					Page page = downloader.download(request);
					
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
		} else {
			if (processingUrls.contains(request.getUrl()))
			Log.consoleWriteLine("[Skip Submit] " + request.getUrl());
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
