package com.ni.analyze;

import java.io.IOException;

import org.jsoup.select.Elements;

import com.ni.crawler.model.Task;
import com.ni.crawler.utilities.JsoupUtilities;

public class ExampleAnalyzer implements PageAnalyzer {

	@Override
	public Object analyze(Task task) {
		
		String localPath = task.getLocalPath();
		Elements elements = JsoupUtilities.getElementsByClass(localPath, "lia-page");
		
		if (elements != null && elements.size() >= 1) {
			return null;
		} 
		
		elements = JsoupUtilities.getElementsByClass(localPath, "pnx-page-wrap");
		if (elements != null && elements.size() >= 1) {
			return null;
		}
		return null;
	}

	@Override
	public void toXml(Task task) {
		// TODO Auto-generated method stub
		
	}

}
