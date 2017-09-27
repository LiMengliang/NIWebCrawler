package com.ni.crawler.downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ni.NiSpiderApplication;
import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.HttpClientUtils;
import com.ni.crawler.utils.Log;

public class HttpClientDownloader extends AbstractDownloader {
	
//	@Autowired
	private TaskService taskService;
	
	private static final int HTTP_OK = 200;
	private static final String TEXT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/html/";
	private static final String ATTACHMENT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/attachment/";
	private static final String OLD_ATTACHMENT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache_for/attachment/";
	
	public HttpClientDownloader(TaskService taskService) {
		this.taskService = taskService;
	}	
	@Override
	public Page download(Request request) {
		return new Page(get(request), request.getUrl());		
	}
	
	@Override
	protected void onSuccess(Request requesst) {
		
		try {		
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onError(Request request) {
		
	}
	
	private String get(Request request) {
		String url = request.getUrl();
		 CloseableHttpClient httpclient = null;
       // JUST A WORKAROUND TO IMPROVE PERFORMANCE
       String lastSegment = url.substring(url.lastIndexOf("/")).toLowerCase();
       if (lastSegment.contains(".png") || lastSegment.contains(".vi") || lastSegment.contains(".pdf") || lastSegment.contains(".zip")) {
       	String oldPath = OLD_ATTACHMENT_LOCAL_CACHE_PATH + url.substring(url.lastIndexOf("/"));
       	String newPath = ATTACHMENT_LOCAL_CACHE_PATH + url.substring(url.lastIndexOf("/"));
       	File oldfile = new File(oldPath);
       	File newfile = new File(newPath);
           if(oldfile.exists() && !newfile.exists()) {
               try {
               	Log.consoleWriteLine((new StringBuilder("Copy from existing ").append(request.getUrl()).append("[").append(Thread.currentThread().getId()).append("]")).toString());
					
					Files.copy(oldfile.toPath(), newfile.toPath());
					taskService.update(request.getUrl(), 'c', 'b', newPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               return url;
           }
       }
       // END
       
       try {   
       	Log.consoleWriteLine((new StringBuilder("Start fetching from ").append(request.getUrl()).append("[").append(Thread.currentThread().getId()).append("]")).toString());
			httpclient = HttpClients.createDefault(); 
       	HttpGet httpget = new HttpGet(url); 
	        CloseableHttpResponse response = httpclient.execute(httpget);  
	        try {    
	        	if (HttpClientUtils.isTextHtmlType(response)) {            		
	            	HttpEntity entity = response.getEntity();           	
	            	String content = EntityUtils.toString(entity); 
	            	String title = content.substring(content.indexOf("<title>") + "<title>".length(), content.indexOf("</title>")).replace('/', '-').replace('.', '-');
	            	// cache locally
	            	String localPath = TEXT_LOCAL_CACHE_PATH + title + ".html";
	            	HttpClientUtils.cacheText(content, localPath);
	            	taskService.update(request.getUrl(), 'b', 't', localPath);
	            	if (isSuccess(response)) {
	            		onSuccess(request);
	            	}            	
	            	return content;
	            }
	            else {      
	            	String localPath = ATTACHMENT_LOCAL_CACHE_PATH + url.substring(url.lastIndexOf("/"));
	            	HttpClientUtils.cacheBinary(response, localPath);
	            	if (isSuccess(response)) {
	            		onSuccess(request);
	            	}  
	            	taskService.update(request.getUrl(), 'c', 'b', localPath);
	            	return url;
	            }
	        } finally {  
	        	response.close();  
	        } 
       } catch (java.net.SocketException e) {
       	taskService.updateStatus(request.getUrl(), 'a');
           e.printStackTrace();  
       } catch (ClientProtocolException e) {  
       	taskService.updateStatus(request.getUrl(), 'a');
           e.printStackTrace();  
       } catch (IOException e) {  
       	taskService.updateStatus(request.getUrl(), 'a');
           e.printStackTrace();  
       } finally {    
           try {
           	if (httpclient != null) {            		
           		httpclient.close();
           	}  
           } catch (IOException e) {  
               e.printStackTrace();  
           }  
       } 
       return "";
	}
	
	private boolean isSuccess(HttpResponse response) {
		return response.getStatusLine().getStatusCode() == HTTP_OK;
	}
}
