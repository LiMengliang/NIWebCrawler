package com.ni.crawler.model;

import java.util.List;
import org.springframework.data.domain.Page;

public interface TaskService {
	
	public List<Task> getUnfinishedTasks();
	
	public Task getTaskByUrl(String url);
	
	public char getStatus(String url);
	
	public void updateStatus(String url, char status);
	
	public void addTask(Task task);
	
	public boolean addUniqueTask(Task task);
	
	public void update(String url, char status, char category, String localPath);
	
	public Page<Task> getAllDownloadedTasks(Integer page, Integer size);
}
