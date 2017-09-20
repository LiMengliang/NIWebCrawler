package com.ni.crawler.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="posts_forum")
public class Post {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int postId;
	
	private String url;
	
	private int mainMessageId;
	
	private char type; //k, q, d
	
	private String title;

	public int getPostId() {
		return postId;
	}
	
	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getMainMessageId() {
		return mainMessageId;
	}
	
	public void setMainMessageId(int mainMessageId) {
		this.mainMessageId = mainMessageId;
	}
	
	public char getType() {
		return type;
	}
	
	public void setType(char type) {
		this.type = type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}