package com.ni.crawler.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tasks_forum")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int taskId;
	
	@NotNull
	private String url;
	
	private String localPath;
	
	@NotNull
	private char status;
	
	@NotNull
	private char category;
	
	private Timestamp lastModifyTime;
	
	private String md5;
	
	public Task() {
		
	}
	
	public Task(String url, char category, char status) {
		this.url = url;
		this.category = category;
		this.status = status;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public String getLocalPath() {
		return localPath;
	}
	
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	
	public char getStatus() {
		return status;
	}
	
	public void setStatus(char status) {
		this.status = status;
	}
	
	public char getCategory() {
		return category;
	}
	
	public void setCategory(char category) {
		this.category = category;
	}
	
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public String getMd5() {
		return md5;
	}
	
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
}
