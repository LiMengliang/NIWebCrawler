package com.ni.crawler.processor;

public interface PageProcessorFactory {
	
	boolean checkCapability(String url);
	
	PageProcessor createPageProcessor(); 
}
