package com.ni.crawler.scheduler;

import com.ni.crawler.downloader.Downloader;
import com.ni.crawler.downloader.HttpClientDownloader;
import com.ni.crawler.model.Request;

public class SingleThreadExecutor implements Executor {

	@Override
	public void acceptRequest(Request request) {
		// TODO Auto-generated method stub
		
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
	
//	private RequestFilter scheduler;
//	
//	
//	private Thread thread;
//	
//	public SingleThreadExecutor() {
//		thread = new Thread(new Runnable( ) {
//
//			@Override
//			public void run() {
//				while(!thread.isInterrupted()) {
//					Request request = scheduler.consumeNext();
//					// Thread.sleep(3000);
//					Downloader httpDownloader = new HttpClientDownloader();
//					httpDownloader.download(request);
//					System.out.println("Finish executing request");
//				}
//			}
//			
//		});
//	}		
//
//	@Override
//	public void start() {
//		thread.start();		
//	}
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void stop() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void setScheduler(RequestFilter scheduler) {
//		this.scheduler = scheduler;
//		
//	} 
}
