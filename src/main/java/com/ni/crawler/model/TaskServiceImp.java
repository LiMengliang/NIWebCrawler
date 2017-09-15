package com.ni.crawler.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImp implements TaskService {
	
	@Autowired
	private TaskDao taskDao;

	@Override
	public List<Task> getUnfinishedTasks() {
		return taskDao.findByStatusNot('c');
		
	}
	
	@Override
	public void updateStatus(String url, char status) {
		Task existing = taskDao.findByUrl(url);
		if (existing != null) {
			existing.setStatus(status);
			taskDao.save(existing);
		}
		
	}
	
	@Override
	public void addTask(Task task) {
		taskDao.save(task);
	}
	
	@Override
	public void addUniqueTask(Task task) {
		Task existing = taskDao.findByUrl(task.getUrl());
		if (existing == null) {
			taskDao.save(task);
		}
	}
}
