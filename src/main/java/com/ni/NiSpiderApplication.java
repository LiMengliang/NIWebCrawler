package com.ni;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ni.analyze.ExampleAnalyzer;
import com.ni.crawler.Crawler;
import com.ni.crawler.model.Example;
import com.ni.crawler.model.ExampleDao;
import com.ni.crawler.model.ExampleService;
import com.ni.crawler.model.Task;
import com.ni.crawler.model.TaskDao;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.ArchiveUtils;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.Log;
import com.ni.crawler.utils.UrlUtilities;

@RestController
@SpringBootApplication
@EnableJpaRepositories 
public class NiSpiderApplication {
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	public TaskService taskService;	
	
	@Autowired
	private ExampleService exampleService;
	
	@RequestMapping("/crawl")
	public void crawl(){
		// get a logger instance named "com.foo"
		   Logger  logger = Logger.getLogger("com.ni");

		   // Now set its level. Normally you do not need to set the
		   // level of a logger programmatically. This is usually done
		   // in configuration files.
		   logger.setLevel(Level.INFO);
		// Crawler.me().addSeedUrl("https://forums.ni.com/t5/LabVIEW/bd-p/170").start();
		// Crawler.me().addSeedUrl("http://search.ni.com/nisearch/app/main/p/ap/tech/lang/en/pg/1/ps/30/sn/catnav:ex/").start();
// 		new Crawler(taskService).addSeedUrl("http://search.ni.com/nisearch/app/main/p/ap/tech/lang/en/pg/1/ps/30/sn/catnav:ex/").start();
		new Crawler(taskService).addSeedUrl("https://forums.ni.com/t5/LabVIEW/bd-p/170/page/1").start();
//		List<Task> task = taskDao.findByUrl("https://forums.ni.com/t5/LabVIEW/bd-p/17y");
		// List<Task> unfinished = taskService.getUnfinishedTasks();
	}
	
	@RequestMapping("/continue")
	public void continueCrawl() {
		List<Task> unfinishedTasks = taskService.getUnfinishedTasks();
		Crawler crawler = new Crawler(taskService);
		for(Task task : unfinishedTasks) {
			crawler.addSeedUrl(task.getUrl());
		}
		crawler.start();
	}	
	
	@RequestMapping("analyze")
	public void analyze() {
		List<Example> examples = new ArrayList<>();
		ExampleAnalyzer exAnalyzer = new ExampleAnalyzer();
		int pageIndex = 1;
		int insertedNum = 1;
		int totalNum = 1;
		boolean hasMore = true;
		while(hasMore) {
			Page<Task> page = this.taskService.getAllDownloadedTasks(pageIndex, 10);
			pageIndex += 1;
			
			if (page == null) {
				hasMore = false;
			}
			else {				
				List<Task> tasks = page.getContent();
				for(Task task : tasks) {
					totalNum++;
					Example e = (Example)exAnalyzer.analyze(task);
					if (e != null) {
						// examples.add(e);		
						exampleService.saveExample(e);
						insertedNum++;
						Log.consoleWriteLine("Analyzed " + task.getUrl());
					}
				}
				if (tasks.size() < 10) {
					hasMore = false;
				}
			}
		}
		System.out.println("Finished, insert " + insertedNum + " records");
		System.out.println("Finished, total " + totalNum + " records");
	}
	
	@RequestMapping("analyzeTest")
	public void analyzeTest() {
		Task task = new Task();
		// task.setLocalPath("/home/meli/NIWebCache_Ex/html/TabSpace Finder Using LabVIEW - Discussion Forums - National Instruments.html");
		// 		task.setLocalPath("/home/meli/NIWebCache_Ex/html/Lotus Notes Email VI's - Discussion Forums - National Instruments.html");
		// task.setLocalPath("/home/meli/NIWebCache_Ex/html/How to explain my finding in \"format to, scan from file\" phenomena? feeling weird-- - Discussion Forums - National Instruments.html");
		// task.setLocalPath("/home/meli/NIWebCache_Ex/html/Count Pulses in Software - Discussion Forums - National Instruments.html");
		// task.setLocalPath("/home/meli/NIWebCache_Ex/html/9- Ladies Dancing - Sorting Algorithm - Discussion Forums - National Instruments.html"); 
		task.setLocalPath("/home/meli/NIWebCache_Ex/html/Localizing Error Codes.htl"); // illegal argument
		task.setURL("http://test");
		task.setCategory('t');
		task.setStatus('c');
		ExampleAnalyzer exAnalyzer = new ExampleAnalyzer();
		exAnalyzer.analyze(task);	
		
	}
	
	@RequestMapping("unzipTest")
	public void unzipTest() {
		ArchiveUtils.unzip("/home/meli/NIWebCache_Ex/attachment/Queue_with_Cluster_2012.zip", "/home/meli/NIWebCache_Ex/attachment/A");
// 		ArchiveUtils.unzip("/home/meli/NIWebCache_Ex/attachment/cRIOWfm_IO%20LV2017.zip", "/home/meli/NIWebCache_Ex/attachment/A"); // can't find file path
//		ArchiveUtils.unzip("/home/meli/NIWebCache_Ex/attachment/TDMS%20Write%20Cluster%20Example%202015.zip", "/home/meli/NIWebCache_Ex/attachment/A");
	}
	
	
	@RequestMapping("test")
	public void test() {
		boolean match = UrlUtilities.isUrlPatternMatch("https://forums.ni.com/t5/LabVIEW/bd-p/170/page/2203?q=_change_me_", new String[] {
				"https:", "", "forums.ni.com", "t5", "{*}", "bd-p", "{*}", "page", "{num}"
		});
	}
	
	private void getByUrl(int id) {
		
		Task task = taskDao.findOne(id);
	}
	
	private void testDatabase() {
		
		Task task = new Task();
 		task.setURL("https://forums.ni.com/t5/LabVIEW/bd-p/17y");
		task.setLocalPath("/home/test.pdf");
		task.setStatus('A');
		task.setCategory('B');
		taskDao.save(task);		
	}
	
private void testDatabase2() {
		
// 		Task task = new Task("https://forums.ni.com/t5/LabVIEW/bd-p/171", 'B', 'A');
		Task task = new Task();
 		task.setURL("https://forums.ni.com/t5/LabVIEW/bd-p/174");
		task.setLocalPath("/home/test.pdf");
		task.setStatus('A');
		task.setCategory('B');
		taskDao.save(task);		
	}
			
	public static void main(String[] args) {
				
		SpringApplication.run(NiSpiderApplication.class, args);
	}
}
