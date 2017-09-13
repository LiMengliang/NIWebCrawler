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

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.utilities.HttpClientUtilities;

public class HttpClientDownloader extends AbstractDownloader {
	
	private static final int HTTP_OK = 200;
	private static final String TEXT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/html/";
	private static final String ATTACHMENT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/attachment/";
	
	public HttpClientDownloader() {
		
	}	
	@Override
	public Page download(Request request) {
		return new Page(get(request));		
	}
	
	@Override
	protected void onSuccess(Request requesst) {
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
            		String title = content.substring(content.indexOf("<title>") + "<title>".length(), content.indexOf("</title>")).replace('/', '-');
            		// cache locally
            		int randomNum = (new Random()).nextInt() + 1;
            		HttpClientUtilities.cacheText(content, TEXT_LOCAL_CACHE_PATH + title + ".html");
            	
            		if (isSuccess(response)) {
            			onSuccess(request);
            		}            	
            		return content;
            	}
            	else {            		
            		HttpClientUtilities.cacheBinary(response, ATTACHMENT_LOCAL_CACHE_PATH + url.substring(url.lastIndexOf("/")));
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
