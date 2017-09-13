package com.ni.spider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ni.crawler.Crawler;
import com.ni.crawler.utilities.UrlUtilities;

@RestController
@SpringBootApplication
public class NiSpiderApplication {

	@RequestMapping("/")

	public void crawl(){
		
		//Test
		

//		Spider.create(new GithubRepoPageProcessor()).
//			addUrl("https://github.com/code4craft").
//			addPipeline(new JsonFilePipeline("/home/meli/spider repo")).thread(5).run();		
//		List<String> urls = new ArrayList<String>();
//		urls.add("https://forums.ni.com/t5/Discussion-Forums/ct-p/discussion-forums");
//		urls.add("https://forums.ni.com/t5/LabVIEW/bd-p/170");
		Crawler.me().addSeedUrl("https://forums.ni.com/t5/LabVIEW/bd-p/170").start();
	}
	private static final String[] topicLinkPattern = new String[] {
			"t5", "{*}", "{*}", "td-p", "{*}"
	};
	
	public static void main(String[] args) {
		
		UrlUtilities.isUrlPatternMatch("/t5/LabVIEW/Running-a-sub-vi-using-VISA-in-parallel-in-a-subpanel/td-p/3687133", topicLinkPattern);
		
		SpringApplication.run(NiSpiderApplication.class, args);
	}
}
