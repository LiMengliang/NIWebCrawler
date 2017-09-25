package com.ni.crawler.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="examples")
public class Example {
	
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
	
	public void toSolrSchema() {
		
	}
}
