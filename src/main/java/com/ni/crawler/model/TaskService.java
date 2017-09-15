package com.ni.crawler.model;

import java.util.List;

public interface TaskService {
	
	public List<Task> getUnfinishedTasks();
	
	public void updateStatus(String url, char status);
	
	public void addTask(Task task);
	
	public void addUniqueTask(Task task);
}