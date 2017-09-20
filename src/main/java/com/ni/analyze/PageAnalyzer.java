package com.ni.analyze;

import com.ni.crawler.model.Task;

public interface PageAnalyzer<T> {

	T analyze(Task task);
	
	void toXml(Task task);
}
