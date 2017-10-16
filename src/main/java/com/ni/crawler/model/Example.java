package com.ni.crawler.model;

import static org.assertj.core.api.Assertions.setMaxElementsForPrinting;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.solr.common.SolrInputDocument;
import org.assertj.core.internal.Strings;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;

import com.ni.crawler.utils.TwoTuple;
import com.ni.crawler.utils.XmlUtils;

@Entity
@Table(name="examples_new")
public class Example implements SolrDocument {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String url;
	
	@NotNull
	private String title;
	
	private Timestamp creationTime;
	
	private Timestamp lastEditTime;
	
	private String author;
	
	private int kudos;
	
	private String tags;
	
	private String overview;
	
	private String description;
	
	private String requirements;
	
	private String steps;
	
	private String additionalInfo;	
	
	private String fullContent;
	
	private boolean draft;
	
	private String attachmentUrls;// workaround, mysql does not support array type, here just use ' ' as seperator.

	public Example() {
		
	}

	public Example(String title, String overview, String description, String requirements, String steps,
			String additionalInfo) {
		super();
		this.title = title;
		this.overview = overview;
		this.description = description;
		this.requirements = requirements;
		this.steps = steps;
		this.additionalInfo = additionalInfo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getFullContent() {
		return fullContent;
	}

	public void setFullContent(String fullContent) {
		this.fullContent = fullContent;
	}

	public String getAttachmentUrls() {
		return attachmentUrls;
	}

	public void setAttachmentUrls(String attachmentUrls) {
		this.attachmentUrls = attachmentUrls;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getKudos() {
		return kudos;
	}

	public void setKudos(int kudos) {
		this.kudos = kudos;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Timestamp getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}
	
	public void toSolrSchema(String path) {
		//<add>
		//  <doc> ...  </doc>
		//</add>
		Document document = XmlUtils.createDocument();
		Element add = XmlUtils.createElement("add");
		Element doc = XmlUtils.createElement("doc");
		document.add(add);
		add.add(doc);
		
		// add fields

		Element idField = XmlUtils.createElement("field", Integer.toString(id), new TwoTuple<QName, String>(new QName("name"), "id"));
		XmlUtils.addToElement(doc, idField);
		
		Element urlField = XmlUtils.createElement("field", url, new TwoTuple<QName, String>(new QName("name"), "url"));
		XmlUtils.addToElement(doc, urlField);
				
		if (title != null) {
			Element titleField = XmlUtils.createElement("field", title, new TwoTuple<QName, String>(new QName("name"), "title"));
			XmlUtils.addToElement(doc, titleField);			
		}
		
		if (creationTime != null) {
			String creationTimeString = creationTime.toString();
			creationTimeString = creationTimeString.replace(' ', 'T').replace(".0", "Z/MONTH");
			Element creationTimeField = XmlUtils.createElement("field", creationTimeString, 
					new TwoTuple<QName, String>(new QName("name"), "creationTime"));
			XmlUtils.addToElement(doc, creationTimeField);			
		}
		
		if (lastEditTime != null) {
			String lastEditTimeString = lastEditTime.toString();
			lastEditTimeString = lastEditTimeString.replace(' ', 'T').replace(".0", "Z/MONTH");
			Element lastEditTimeField = XmlUtils.createElement("field", lastEditTimeString,
					new TwoTuple<QName, String>(new QName("name"), "lastEditTime"));
			XmlUtils.addToElement(doc, lastEditTimeField);			
		}
		
		if (author != null) {
			Element authorField = XmlUtils.createElement("field", author, new TwoTuple<QName, String>(new QName("name"), "author"));
			XmlUtils.addToElement(doc, authorField);			
		}
		
		Element kudosField = XmlUtils.createElement("field", Integer.toString(kudos), new TwoTuple<QName, String>(new QName("name"), "kudos"));
		XmlUtils.addToElement(doc, kudosField);
		
		if (tags != null) {
			Element tagsField = XmlUtils.createElement("field", tags, new TwoTuple<QName, String>(new QName("name"), "tags"));
			XmlUtils.addToElement(doc, tagsField);			
		}
		
		if (overview != null) {
			Element overviewField = XmlUtils.createElement("field", overview, new TwoTuple<QName, String>(new QName("name"), "overview"));
			XmlUtils.addToElement(doc, overviewField);			
		}
		
		if (description != null) {
			Element descriptionField = XmlUtils.createElement("field", description, new TwoTuple<QName, String>(new QName("name"), "description"));
			XmlUtils.addToElement(doc, descriptionField);
		}
		
		if (requirements != null) {
			Element requirementsField = XmlUtils.createElement("field", requirements, new TwoTuple<QName, String>(new QName("name"), "requirements"));
			XmlUtils.addToElement(doc, requirementsField);			
		}
		
		if (steps != null) {
			Element stepsField = XmlUtils.createElement("field", steps, new TwoTuple<QName, String>(new QName("name"), "steps"));
			XmlUtils.addToElement(doc, stepsField);			
		}
		
		if (additionalInfo != null) {
			Element additionalInfoField = XmlUtils.createElement("field", additionalInfo, new TwoTuple<QName, String>(new QName("name"), "additionalInfo"));
			XmlUtils.addToElement(doc, additionalInfoField);
		}
		
		if (fullContent != null) {
			Element fullContentField = XmlUtils.createElement("field", fullContent, new TwoTuple<QName, String>(new QName("name"), "fullContent"));
			XmlUtils.addToElement(doc, fullContentField);
		}
		
		Element draftField = XmlUtils.createElement("field", Boolean.toString(draft), new TwoTuple<QName, String>(new QName("name"), "draft"));
		XmlUtils.addToElement(doc, draftField);
		
		if (attachmentUrls != null) {
			String[] urls = attachmentUrls.split("#3#");
			for(String url : urls) {
				Element attachmentUrlField = XmlUtils.createElement("field", url, new TwoTuple<QName, String>(new QName("name"), "attachmentUrl"));
				XmlUtils.addToElement(doc, attachmentUrlField);
			}
		}
		
		// save to file
		try {
			XmlUtils.save(document, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", Integer.toString(id));
		document.addField("url", url);
		if (title != null) {
			document.addField("title", title);
		}
		if (creationTime != null) {
			String creationTimeString = creationTime.toString();
			creationTimeString = creationTimeString.replace(' ', 'T').replace(".0", "Z/MONTH");
			document.addField("creationTime", creationTimeString);			
		}
		if (lastEditTime != null) {
			String lastEditTimeString = lastEditTime.toString();
			lastEditTimeString = lastEditTimeString.replace(' ', 'T').replace(".0", "Z/MONTH");
			document.addField("lastEditTime", lastEditTimeString);
		}
		if (author != null) {
			document.addField("author", author);
		}
		document.addField("kudos", Integer.toString(kudos));
		if (tags != null) {
			document.addField("tags", tags);
		}
		if (overview != null) {
			document.addField("overview", overview);
		}
		if (description != null) {
			document.addField("description", description);
		}
		if (requirements != null) {
			document.addField("requirements", requirements);
		}
		if (steps != null) {
			document.addField("steps", steps);
		}
		if (additionalInfo != null) {
			document.addField("additionalInfo", additionalInfo);
		}
		if (fullContent != null) {
			document.addField("fullContent", fullContent);
		}
		document.addField("draft", Boolean.toString(draft));
		if (attachmentUrls != null) {
			String[] urls = attachmentUrls.split("#3#");
			for(String url : urls) {
				document.addField("attachmentUrl", url);
			}
		}
		return document;
	}
}
