package com.ni.crawler.model;

public class Page {
	
	private String url;

	private String pageContent;
	
	public Page(String pageContent, String url) {
		this.pageContent = pageContent;
		this.url = url;
	}
	
	public String getPageContent() {
		return pageContent;
	}
	
	public String getUrl() {
		return url;
	}
	
}
