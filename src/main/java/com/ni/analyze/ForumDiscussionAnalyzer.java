package com.ni.analyze;

import org.jsoup.select.Elements;

import com.ni.crawler.model.Post;
import com.ni.crawler.model.Task;
import com.ni.crawler.utilities.JsoupUtilities;

public class ForumDiscussionAnalyzer implements PageAnalyzer<Post> {

	@Override
	public Post analyze(Task task) {

		String localPath = task.getLocalPath();
		
		return null;
	}

	@Override
	public void toXml(Task task) {
		// TODO Auto-generated method stub
		
	}

}
