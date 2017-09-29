package com.ni;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.lemurproject.kstem.KrovetzStemmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.ni.analyze.ExampleAnalyzer;
import com.ni.analyze.TextAnalyzer;
import com.ni.crawler.Crawler;
import com.ni.crawler.model.Example;
import com.ni.crawler.model.ExampleDao;
import com.ni.crawler.model.ExampleService;
import com.ni.crawler.model.Task;
import com.ni.crawler.model.TaskDao;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.ArchiveUtils;
import com.ni.crawler.utils.JSONUtils;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.Log;
import com.ni.crawler.utils.StemUtils;
import com.ni.crawler.utils.UrlUtilities;

@RestController
@SpringBootApplication
@EnableJpaRepositories 
public class NiSpiderApplication {
	
	private static final String EXAMPLE_XML_PATH = "/home/meli/NIWebCache_Ex/xml/";
	
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
		
		String solrServerUrl = "http://localhost:8983/solr/niweb/";
		SolrClient solr = new HttpSolrClient.Builder(solrServerUrl).build();
		
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
					Example example = (Example)exAnalyzer.analyze(task);
					if (example != null) {
						// examples.add(e);		
						exampleService.saveExample(example);
						insertedNum++;
						// write to xml
						example.toSolrSchema(EXAMPLE_XML_PATH + task.getLocalPath().substring(task.getLocalPath().lastIndexOf('/')) + ".xml");
						// create solr document and add to solr
						SolrInputDocument solrDocument = example.toSolrInputDocument();
						try {
							UpdateResponse response = solr.add(solrDocument);
						} catch (SolrServerException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Log.consoleWriteLine("Analyzed " + task.getUrl());
						
					}
				}
				if (tasks.size() < 10) {
					hasMore = false;
				}
			}
		}
		// commit to solr
		try {
			solr.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished, insert " + insertedNum + " records");
		System.out.println("Finished, total " + totalNum + " records");
	}
	
	@RequestMapping("termFreq")
	public void analyzeTermFrequency() {
		
		int pageIndex = 1;
		int insertedNum = 1;
		int totalNum = 1;
		ExampleAnalyzer exAnalyzer = new ExampleAnalyzer();
		boolean hasMore = true;
//		Map<String, Double> termFreq = new TreeMap<>();
		Map<String, Double> termAndDocFreq = new TreeMap<>();
		
		TextAnalyzer analyzer = new TextAnalyzer();
		int analyzedArticleSize = 0;
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
					Example example = (Example)exAnalyzer.analyze(task);
					if (example != null && totalNum % 3 == 0) {
						analyzedArticleSize++;
						HashSet<String> termsOfExample = new HashSet<>();
						String fullContent = example.getFullContent();
						if (fullContent != null) {
							List<String> terms = analyzer.analyze(fullContent);
							for(String term : terms) {								
//								double count = termFreq.getOrDefault(term, 0.0);
								termsOfExample.add(term);
//								termFreq.put(term, count + 1);
							}
							for(String term : termsOfExample) {
								double count = termAndDocFreq.getOrDefault(term, 0.0);
								termAndDocFreq.put(term, count + 1);
							}
						}
					}
					Log.consoleWriteLine("Analyzed " + totalNum);
				}
				if (tasks.size() < 10) {
					hasMore = false;
				}
			}
		}
			
		Map<String, Double> finalTermAndDocFreq = new TreeMap<>();
		for(Map.Entry<String, Double> entry : termAndDocFreq.entrySet()) {
			finalTermAndDocFreq.put(entry.getKey(), Math.log(analyzedArticleSize / entry.getValue() + 1));
		}
		
//		Map<Double, List<String>> freqAndTerm = reverseKeyAndValue(termFreq);
//		Map<Double, List<String>> docFreqAndTerm = reverseKeyAndValue(finalTermAndDocFreq);
		JSONArray jsonArray = JSONUtils.mapToJson(finalTermAndDocFreq);
		
//		for(Map.Entry<String, Double> entry : termAndDocFreq.entrySet()) {
//			termAndDocFreq.put(entry.getKey(), Math.log(entry.getValue() + 1));
//		}
//		
//		Map<Double, List<String>> freqAndTerm = reverseKeyAndValue(termFreq);
//		Map<Double, List<String>> docFreqAndTerm = reverseKeyAndValue(termAndDocFreq);
//		JSONArray jsonArray = JSONUtils.mapToJson(termAndDocFreq);
		JSONUtils.toFile("/home/meli/TermFreq.json", jsonArray);		
	}
	
	@RequestMapping("termFreq2Gram")
	public void analyzeTermFrequencyWith2Gram() {
		
		int pageIndex = 1;
		int insertedNum = 1;
		int totalNum = 1;
		ExampleAnalyzer exAnalyzer = new ExampleAnalyzer();
		boolean hasMore = true;
		Map<String, Double> termFreq = new TreeMap<>();
		Map<String, Double> termAndDocFreq = new TreeMap<>();
		
		TextAnalyzer analyzer = new TextAnalyzer();
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
					Example example = (Example)exAnalyzer.analyze(task);
					if (example != null && totalNum % 3 == 0) {
						HashSet<String> termsOfExample = new HashSet<>();
						String fullContent = example.getFullContent();
						if (fullContent != null) {
							List<String> terms = analyzer.analyzeWith2Gram(fullContent);
							for(String term : terms) {
								
								double count = termFreq.getOrDefault(term, 0.0);
								termsOfExample.add(term);
								termFreq.put(term, count + 1);
							}
							for(String term : termsOfExample) {
								double count = termAndDocFreq.getOrDefault(term, 0.0);
								termAndDocFreq.put(term, count + 1);
							}
						}
					}
					Log.consoleWriteLine("Analyzed " + totalNum);
				}
				if (tasks.size() < 10) {
					hasMore = false;
				}
			}
		}
				
		Map<String, Double> finalTermAndDocFreq = new TreeMap<>();
		for(Map.Entry<String, Double> entry : termAndDocFreq.entrySet()) {
			if (entry.getKey().contains(" ") && entry.getValue() < 3) {
				continue;
			}
			finalTermAndDocFreq.put(entry.getKey(), Math.log(entry.getValue() + 1));
		}
		
		Map<Double, List<String>> freqAndTerm = reverseKeyAndValue(termFreq);
		Map<Double, List<String>> docFreqAndTerm = reverseKeyAndValue(finalTermAndDocFreq);
		JSONArray jsonArray = JSONUtils.mapToJson(finalTermAndDocFreq);
		JSONUtils.toFile("/home/meli/TermFreq3Grams.json", jsonArray);		
	}
	
	@RequestMapping("estimateTags")
	public void estimateTags() {
				
		int pageIndex = 1;
		int insertedNum = 1;
		int totalNum = 1;
		ExampleAnalyzer exAnalyzer = new ExampleAnalyzer();
		boolean hasMore = true;
		Map<String, Double> termAndDocFreq = null;
		try {
			termAndDocFreq = JSONUtils.jsonToMap(JSONUtils.readFromFile("/home/meli/TermFreq.json"));
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextAnalyzer analyzer = new TextAnalyzer();
		Map<String, List<String>> estimatedKeyWords = new HashMap<>();
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
					Example example = (Example)exAnalyzer.analyze(task);
					if (example != null) {
						String overview = example.getOverview();
						String description = example.getDescription();
						if (overview != null) {
							List<String> terms = analyzer.analyze(overview + " " + example.getTitle());
							Map<String, Double> termsAndScore = new TreeMap<>();
							for(String term : terms) {
								
								double count = termsAndScore.getOrDefault(term, 0.0);
								termsAndScore.put(term, count + 1);
							}
							for(Map.Entry<String, Double> score : termsAndScore.entrySet()) {
								if (score.getValue() >= 1) {
									double idf = termAndDocFreq.getOrDefault(score.getKey(), 1.0);
									double finalScore = score.getValue()*idf;
									termsAndScore.put(score.getKey(), finalScore);
								}
								else {
									termsAndScore.put(score.getKey(), 0.0);
								}
							}
							Log.consoleWriteLine("******* estimate " + example.getTitle() + " ** " + example.getUrl());
							Map<Double, List<String>> tagsRanking = reverseKeyAndValue(termsAndScore);
						}
					}	
				}
				if (tasks.size() < 10) {
					hasMore = false;
				}
			}
		}
	}
	
	@RequestMapping("estimateTags2Gram")
	public void estimateTags2Gram() {
				
		int pageIndex = 1;
		int insertedNum = 1;
		int totalNum = 1;
		ExampleAnalyzer exAnalyzer = new ExampleAnalyzer();
		boolean hasMore = true;
		Map<String, Double> termAndDocFreq = null;
		try {
			termAndDocFreq = JSONUtils.jsonToMap(JSONUtils.readFromFile("/home/meli/TermFreq3Grams.json"));
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextAnalyzer analyzer = new TextAnalyzer();
		Map<String, List<String>> estimatedKeyWords = new HashMap<>();
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
					Example example = (Example)exAnalyzer.analyze(task);
					if (example != null) {
						String overview = example.getOverview();
						String description = example.getDescription();
						if (overview != null) {
							List<String> terms = analyzer.analyzeWith2Gram(overview + " " + example.getTitle());
							Map<String, Double> termsAndScore = new TreeMap<>();
							for(String term : terms) {
								
								double count = termsAndScore.getOrDefault(term, 0.0);
								termsAndScore.put(term, count + 1);
							}
							Map<String, Double> finalTermsAndScore = new TreeMap<>();
							for(Map.Entry<String, Double> score : termsAndScore.entrySet()) {
								if (!score.getKey().contains(" ") || (score.getKey().contains(" ") && score.getValue() > 1)) {
									double freqInDocument = termAndDocFreq.getOrDefault(score.getKey(), 1.0);
									double finalScore = score.getValue()/freqInDocument;
									finalTermsAndScore.put(score.getKey(), finalScore);
								}
							}
							Log.consoleWriteLine("******* estimate " + example.getTitle() + " ** " + example.getUrl());
							Map<Double, List<String>> tagsRanking = reverseKeyAndValue(finalTermsAndScore);
						}
					}	
				}
				if (tasks.size() < 10) {
					hasMore = false;
				}
			}
		}
	}

	private <K, V>  Map<V, List<K>> reverseKeyAndValue(Map<K, V> origin) {
		Map<V, List<K>> reversed = new TreeMap<>();
		for(Map.Entry<K, V> entry : origin.entrySet()) {
			V frequency = entry.getValue();
			List<K> terms = reversed.getOrDefault(frequency, new ArrayList<K>());
			terms.add(entry.getKey());
			reversed.put(frequency, terms);
		}
		for(Map.Entry<V, List<K>> entry : reversed.entrySet()) {
			StringBuilder outputResult = new StringBuilder();
			outputResult.append(entry.getKey()).append("  :  ");
			for(K term : entry.getValue()) {
				outputResult.append(term.toString()).append(",  ");
			}
			Log.consoleWriteLine(outputResult.toString());
		}
		return reversed;
	}
	
	private Map<Integer, List<String>> getFreqAndTerms(Map<String, Integer> termFreq) {
		Map<Integer, List<String>> freqAndTerm = new TreeMap<>();
		for(Map.Entry<String, Integer> entry : termFreq.entrySet()) {
			int frequency = entry.getValue();
			List<String> terms = freqAndTerm.getOrDefault(frequency, new ArrayList<String>());
			terms.add(entry.getKey());
			freqAndTerm.put(frequency, terms);
		}
		for(Map.Entry<Integer, List<String>> entry : freqAndTerm.entrySet()) {
			StringBuilder outputResult = new StringBuilder();
			outputResult.append(entry.getKey()).append("  :  ");
			for(String term : entry.getValue()) {
				outputResult.append(term).append(",  ");
			}
			Log.consoleWriteLine(outputResult.toString());
		}
		return freqAndTerm;
	}
	
	@RequestMapping("textAnalyzerTest") 
	public void analyzeTextTest() {
		TextAnalyzer analyzer = new TextAnalyzer();
//		List<String> source = new ArrayList<String>() {{
//				add("a");
//				add("apple");
//				add("is");
//				add("red");
//			}
//		};
//		analyzer.filterStopWords(source);
//		List<String> result = source;
		
		String sentenceWithComma = "This is a \'big\' apples took";
		String lowerCase = analyzer.toLowerCase(sentenceWithComma);
		String withoutPuncuation = analyzer.filterPuncutation(lowerCase);
		String[] splits = analyzer.splitWithWhiteSpace(withoutPuncuation);
		List<String> removeStopWords = analyzer.filterStopWords(splits);
		List<String> stemmed = analyzer.stemWithSnowball(removeStopWords);
		
		Log.consoleWriteLine("finished");
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
		Example example = (Example)exAnalyzer.analyze(task);	
		
	}
	
	@RequestMapping("unzipTest")
	public void unzipTest() {
		ArchiveUtils.unzip("/home/meli/NIWebCache_Ex/attachment/Queue_with_Cluster_2012.zip", "/home/meli/NIWebCache_Ex/attachment/A");
// 		ArchiveUtils.unzip("/home/meli/NIWebCache_Ex/attachment/cRIOWfm_IO%20LV2017.zip", "/home/meli/NIWebCache_Ex/attachment/A"); // can't find file path
//		ArchiveUtils.unzip("/home/meli/NIWebCache_Ex/attachment/TDMS%20Write%20Cluster%20Example%202015.zip", "/home/meli/NIWebCache_Ex/attachment/A");
	}
	
	@RequestMapping("indexTest")
	public void indexTest() {
		String solrServerUrl = "http://localhost:8983/solr/niweb2/";
		SolrClient solr = new HttpSolrClient.Builder(solrServerUrl).build();
		
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "x878");
		document.addField("title", "A title test");
		try {
			UpdateResponse response = solr.add(document);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			solr.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("stemmerTest")
	@ResponseBody
	public String stemmerTest() {
		SnowballStemmer stemmer = new englishStemmer();
		String source = "summary: this is an example of how to make an interaction with 3d picture control. it may be treated as an example of use of pick model, windowtoobjectcoords and setbillboardparams methods related to 3d picture control (which are not covered by any of examples shipped with labview) as well as example of many others methods and properties related to this control includig creating and manipulating of simple 3d objects and meshes. as edited surface is represented by 3d spline, the code may be also treated as an example for spline interpolation routines. function: the application is an interactive editor of 3d surface defined as 2d array of nodes. the space between nodes is filled with 3d mesh calculated by spline iterpolation between the nodes. user edits nodes' coordinates interactively by clicking and dragging directly on 3d picture control. surface edition starts with simple 3x3 flat set of nodes which may be dragged in any direction and the number of nodes may be changed in order to get a complex 3d surface. steps to execute code: unpack the archive anywhere, open and run \"3d surface editor.vi\" click and drag nodes to move them and change the shape of the surface by clicking and dragging anywhere else you may rotate the model - all rules for standard spherical controller applies here nodes are always moved in the plane parallel to the plane of view the number of control nodes (and complexity of the surface) may be changed with \"rows\" and \"columns\"controls current coordinates of all nodes are being updated in the 2d array control screenshots".toLowerCase();
		StringBuilder result = new StringBuilder();
		String[] words = source.split(" ");
		for(String word : words) {
			stemmer.setCurrent(word);
			stemmer.stem();
			result.append(stemmer.getCurrent()).append(" ");
		}		
		// String result =  stemmer.getCurrent();
		return result.toString();
	}
	
	@RequestMapping("stemmerTest2")
	@ResponseBody
	public String stemmerTest2() {
		SnowballStemmer stemmer = new englishStemmer();
		String source = "create creating created creator";
		StringBuilder result = new StringBuilder();
		String[] words = source.split(" ");
		for(String word : words) {
			stemmer.setCurrent(word);
			stemmer.stem();
			result.append(stemmer.getCurrent()).append(" ");
		}		
		// String result =  stemmer.getCurrent();
		return result.toString();
	}
	
	@RequestMapping("KStemTest2")
	@ResponseBody
	public String kStemTest2() {
		KrovetzStemmer stemmer = new KrovetzStemmer();
		String source = "create creating created creator";
		StringBuilder result = new StringBuilder();
		String[] words = source.split(" ");
		for(String word : words) {
			result.append(stemmer.stem(word)).append(" ");
		}
		return result.toString();
	}
	
	@RequestMapping("KStemTest")
	@ResponseBody
	public String kStemTest() {
		KrovetzStemmer stemmer = new KrovetzStemmer();
		String source = "summary: this is an example of how to make an interaction with 3d picture control. it may be treated as an example of use of pick model, windowtoobjectcoords and setbillboardparams methods related to 3d picture control (which are not covered by any of examples shipped with labview) as well as example of many others methods and properties related to this control includig creating and manipulating of simple 3d objects and meshes. as edited surface is represented by 3d spline, the code may be also treated as an example for spline interpolation routines. function: the application is an interactive editor of 3d surface defined as 2d array of nodes. the space between nodes is filled with 3d mesh calculated by spline iterpolation between the nodes. user edits nodes' coordinates interactively by clicking and dragging directly on 3d picture control. surface edition starts with simple 3x3 flat set of nodes which may be dragged in any direction and the number of nodes may be changed in order to get a complex 3d surface. steps to execute code: unpack the archive anywhere, open and run \"3d surface editor.vi\" click and drag nodes to move them and change the shape of the surface by clicking and dragging anywhere else you may rotate the model - all rules for standard spherical controller applies here nodes are always moved in the plane parallel to the plane of view the number of control nodes (and complexity of the surface) may be changed with \"rows\" and \"columns\"controls current coordinates of all nodes are being updated in the 2d array control screenshots".toLowerCase();
		StringBuilder result = new StringBuilder();
		String[] words = source.split(" ");
		for(String word : words) {
			result.append(stemmer.stem(word)).append(" ");
		}
		return result.toString();
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
