package com.ni.analyze;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.FileSystemUtils;

import com.ni.Config;
import com.ni.crawler.model.Example;
import com.ni.crawler.model.ExampleService;
import com.ni.crawler.model.Task;
import com.ni.crawler.utils.ArchiveUtils;
import com.ni.crawler.utils.FileUtils;
import com.ni.crawler.utils.JsoupUtils;
import com.ni.crawler.utils.Log;
import com.ni.crawler.utils.TextAnalysisUtils;

public class ExampleAnalyzer implements PageAnalyzer {
	
	public static String ATTACHMENT_FOLDER = "/home/meli/NIWebCache_Ex/attachment/";
	
	private static final String FORMATTED_FLAG = 
			"*This document has been updated to meet the current required format for the NI Code Exchange.*";

	// ****Longest tag should comes first.
	private static final KeySection OVERVIEW = new KeySection("OVERVIEW", new ArrayList<String>() {{
				add("overview");
			}
	});
	
	// ****Longest tag should comes first.
	private static final KeySection DESCRIPTION = new KeySection("DESCRIPTION",new ArrayList<String>() {{
				add("description");
			}
	});
	
	// ****Longest tag should comes first.
	private static final KeySection REQUIREMENTS = new KeySection("REQUIREMENTS", new ArrayList<String>() {{
				add("hardware and software requirements");
				add("requirements"); 
			}	
	});
	
	// ****Longest tag should comes first.
	private static final KeySection STEPS = new KeySection("STEPS", new ArrayList<String>() {{
				add("steps to implement or execute code");
			}
	});
	
	// ****Longest tag should comes first.
	private static final KeySection ADDITIONALREF = new KeySection("ADDITIONALREF", new ArrayList<String>() {{
				add("additional information or references");
			}
	});
	
	private static enum Section {
		overview,
		description,
		requirements,
		steps,
		additionalInfo,
		content,
		other
	}
	
	@Autowired
	private ExampleService exampleService;
	
	public ExampleAnalyzer() {
	}
	
	// The html of article is badly formatted, here i just use a text matching algorithem, definitely we need a better solution.
	@Override
	public Object analyze(Task task) {
		String localPath = task.getLocalPath();
		localPath = localPath.replace("NIWebCache", "NIWebCache_Ex");
		File file = new File(localPath);
		if (file.exists()) {
			Document doc = JsoupUtils.parseLocalFile(localPath);
			Element body = JsoupUtils.getFirstHitElementByClass(doc, new String[] {
					"lia-message-unread",
					"wp-title"
			});// JsoupUtils.getElementByClass(doc, "lia-message-unread");
			if (body != null) {
				Example example = new Example();
				example.setUrl(task.getUrl());
				
				// analyze article information
				if (task.getUrl().contains("Example-Program-Drafts")) {
					example.setDraft(true);
				}
				else {
					example.setDraft(false);
				}		
				
				Element articleInfo = JsoupUtils.getElementByClass(doc, "byline");
				if (articleInfo != null) {
					Element author = JsoupUtils.getElementByClass(articleInfo, "lia-component-message-view-widget-author-username");
					if (author != null) {
						example.setAuthor(author.text());
					}
					Element postTime = JsoupUtils.getElementByClass(doc, "lia-message-post-date");
					if (postTime != null) {
						Timestamp timeStamp = textToTimeStamp(postTime.text().substring(1));
						example.setCreationTime(timeStamp);
					}
					Element editTime = JsoupUtils.getElementByClass(doc, "ia-message-edit-date");
					if (editTime != null) {
						Timestamp timestamp = textToTimeStamp(editTime.text().substring(1));
						example.setLastEditTime(timestamp);
					}
				}
				Element kudos = JsoupUtils.getElementByClass(doc, "MessageKudosCount");
				if (kudos != null) {
					example.setKudos(Integer.parseInt(kudos.text()));
				}
				
				// analyze attachments
				StringBuilder links = new StringBuilder();
				Elements attachmentLinks = JsoupUtils.getElementsByClass(doc, "attachment-link");
				for(Element attachmentLink : attachmentLinks) {
					
					String rawLink;
					// rawLink = URLDecoder.decode(JsoupUtils.getAttributeValue(attachmentLink, "href"), "UTF-8");
					rawLink = JsoupUtils.getAttributeValue(attachmentLink, "href");
					String fileName = rawLink.substring(rawLink.lastIndexOf('/') + 1);
					// deal with zip archives
					if (rawLink.endsWith(".zip")) {
						String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf(".zip"));
						try {
							String destDirectory = ATTACHMENT_FOLDER + fileNameWithoutExtension;
							ArchiveUtils.unzip(ATTACHMENT_FOLDER + fileName, destDirectory);
							List<Path> subFiles = FileUtils.listFilesInDirectory(destDirectory);
							for(Path filePath : subFiles) {
								links.append(filePath.toString().substring(ATTACHMENT_FOLDER.length())).append("#3#");
							}
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						links.append(fileName).append("#3#");
					}
				}
				// Log.consoleWriteLine(links.toString());
				example.setAttachmentUrls(links.toString());	
				
				// analyze article
				String title = body.text();
				example.setTitle(title);
				Element articleSection  = JsoupUtils.getFirstHitElementByClass(doc, new String[] {
						"lia-message-template-content-zone",
						"lia-message-body-content",
						"tutorial-body"
				});
				if (articleSection != null) {
					String article = articleSection.text();
					String modifiedArticle = TextAnalysisUtils.toLowerCaseAndRemovePunctuation(article);
					List<Segment> segments = Segment.getSegment(modifiedArticle, new ArrayList<KeySection>() {
						{
							add(OVERVIEW);
							add(DESCRIPTION);
							add(REQUIREMENTS);
							add(STEPS);
							add(ADDITIONALREF);
						}
					});
					
					segments.stream().forEach(x -> {
						String url = task.getUrl();
						try {
							String content = modifiedArticle.substring(x.getStart(), x.getEnd());
							if (x.getKeySection() == OVERVIEW) {
								example.setOverview(content);
							} else if (x.getKeySection() == DESCRIPTION) {
								example.setDescription(content);
							} else if (x.getKeySection() == REQUIREMENTS) {
								example.setRequirements(content);
							} else if (x.getKeySection() == STEPS) {
								example.setSteps(content);
							} else if (x.getKeySection() == ADDITIONALREF) {
								example.setAdditionalInfo(content);
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					});
					example.setFullContent(modifiedArticle);
				}	
				return example;
			}		
		}
		
		return null;
	}	

	@Override
	public void toXml(Task task) {
		// TODO Auto-generated method stub
		
	}
	
	private Timestamp textToTimeStamp(String time) {
		String[] subs = time.split(" ");
		String[] days = subs[0].split("-");
		String[] times = subs[1].split(":");
		if (subs[2].equals("PM")) {
			times[0] = Integer.toString(Integer.parseInt(times[0]) + 12);
		}
		// yyyy-mm-dd hh:mm:ss
		StringBuilder textTime = new StringBuilder();
		textTime.append(days[2]).append("-").append(days[0]).append("-").append(days[1]).append(" ").append(times[0]).append(":").append(times[1]).append(":").append("00");
		return Timestamp.valueOf(textTime.toString());
	}	
			
	private Section getCategory(String text) {
		if (TextAnalysisUtils.isSpecifiedTag(text, OVERVIEW.getTags())) {
			return Section.overview;
		} else if (TextAnalysisUtils.isSpecifiedTag(text, DESCRIPTION.getTags())) {
			return Section.description;
		} else if (TextAnalysisUtils.isSpecifiedTag(text, REQUIREMENTS.getTags())) {
			return Section.requirements;
		} else if (TextAnalysisUtils.isSpecifiedTag(text, STEPS.getTags())) {
			return Section.steps;
		} else if (TextAnalysisUtils.isSpecifiedTag(text, ADDITIONALREF.getTags())) {
			return Section.additionalInfo;
		} else {
			return Section.content;
		}	
	}
	
	private Example generateExample(List<String> lines) {
		Example example = new Example();
		Section category = Section.other;
		StringBuilder content = new StringBuilder();
		for(String line : lines) {
			switch(getCategory(line)) {
			case overview:				
				setExampleValue(example, category, content.toString());
				category = Section.overview;
				content = new StringBuilder();
				break;
			case description:
				setExampleValue(example, category, content.toString());
				category = Section.description;
				content = new StringBuilder();
				break;
			case requirements:
				setExampleValue(example, category, content.toString());
				category = Section.requirements;
				content = new StringBuilder();
				break;
			case steps:
				setExampleValue(example, category, content.toString());
				category = Section.steps;
				content = new StringBuilder();
				break;
			case additionalInfo:
				setExampleValue(example, category, content.toString());
				category = Section.additionalInfo;
				content = new StringBuilder();
				break;
			case content:
				content.append(line);
				break;
			default:
				break;
			}
		}
		setExampleValue(example, category, content.toString());
		return example;
	}
	
	private void setExampleValue(Example example, Section section, String content) {
		switch(section) {
		case overview:				
			example.setOverview(content);
			break;
		case description:
			example.setDescription(content);
			break;
		case requirements:
			example.setRequirements(content);
			break;
		case steps:
			example.setSteps(content);
			break;
		case additionalInfo:
			example.setAdditionalInfo(content);
			break;
		case other:
			example.setFullContent(content);
		default:
			break;
		}
	}
	
	static class KeySection {
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getTags() {
			return tags;
		}

		public void setTags(List<String> tags) {
			this.tags = tags;
		}

		private String name;
		
		private List<String> tags;
		
		public KeySection(String name, List<String> tags) {
			this.name = name;
			this.tags = tags;
		}
	}
	
	static class Segment {
		private int start;
		
		private int end;
		
		private KeySection keySection;
		
		private String matchedTag;
		
		public Segment(int start, int end, KeySection keySection, String matchedTag) {
			this.start = start;
			this.end = end;
			this.keySection = keySection;
			this.matchedTag = matchedTag;
		}
		
		public static List<Segment> getSegment(String text, List<KeySection> allSections) {
			
			int textLength = text.length();
			List<Segment> sortedSegments = new ArrayList<>();
			Map<Integer, Segment> starts = new LinkedHashMap<>();
			
			for(KeySection section : allSections) {
				int start = -1;
				String matchedTag = null;
				for(String tag : section.getTags()) {
					start = text.indexOf(tag);
					if (start >= 0) {
						matchedTag = tag;
						break;
					}
				}
				if (matchedTag != null) {
					starts.put(start, new Segment(start, 0, section, matchedTag));
				}
			}
			if (starts.size() > 0) {
				List<Integer> sortedStarts = new ArrayList<>();
				starts.forEach((key, value) -> {
					sortedStarts.add(key);
				});
				Collections.sort(sortedStarts);  //(Integer[]) starts.keySet().toArray().sorted().toArray();
				int start = -1;
				int index = 0;
				for(index = 0; index < sortedStarts.size() - 1; index++) {
					start = sortedStarts.get(index);
					int end = sortedStarts.get(index+1) - 1;
					Segment segment = starts.get(start);
					segment.setEnd(end);
					sortedSegments.add(segment);
				}
				start = sortedStarts.get(index);
				Segment segment = starts.get(start);
				segment.setEnd(textLength - 1);
				sortedSegments.add(segment);
			}
			for(Segment segment : sortedSegments) {
				segment.setStart(Math.min(segment.getStart() + segment.getMatchedTag().length(), textLength - 1));
			}
			
			return sortedSegments;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public KeySection getKeySection() {
			return keySection;
		}

		public void setKeySection(KeySection keySection) {
			this.keySection = keySection;
		}

		public String getMatchedTag() {
			return matchedTag;
		}

		public void setMatchedTag(String matchedTag) {
			this.matchedTag = matchedTag;
		}
	}

}
