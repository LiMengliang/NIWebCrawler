package com.ni.crawler.downloader;

import java.io.IOException;
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
import com.ni.crawler.utilities.HttpClientUtilities;

public class HttpClientDownloader extends AbstractDownloader {
	
//	@Autowired
	private TaskService taskService;
	
	private static final int HTTP_OK = 200;
	private static final String TEXT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/html/";
	private static final String ATTACHMENT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/attachment/";
	
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
			taskService.updateStatus(requesst.getUrl(), 'b');
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
		CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {    
            HttpGet httpget = new HttpGet(url);   
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {    
            	if (HttpClientUtilities.isTextHtmlType(response)) {            		
            		HttpEntity entity = response.getEntity();           	
            		String content = EntityUtils.toString(entity); 
            		String title = content.substring(content.indexOf("<title>") + "<title>".length(), content.indexOf("</title>")).replace('/', '-').replace('.', '-');
            		// cache locally
            		HttpClientUtilities.cacheText(content, TEXT_LOCAL_CACHE_PATH + title + ".html");
            	
            		if (isSuccess(response)) {
            			onSuccess(request);
            		}            	
            		return content;
            	}
            	else {            		
            		HttpClientUtilities.cacheBinary(response, ATTACHMENT_LOCAL_CACHE_PATH + url.substring(url.lastIndexOf("/")));
            		if (isSuccess(response)) {
            			onSuccess(request);
            		}  
            		taskService.updateStatus(request.getUrl(), 'c');
            		return url;
            	}
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {    
            try {  
                httpclient.close();  
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
