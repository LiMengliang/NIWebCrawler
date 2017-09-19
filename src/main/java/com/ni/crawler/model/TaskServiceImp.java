package com.ni.crawler.model;

import java.util.HashSet;
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
	}
	
	@Override
	public void addUniqueTask(Task task) {
		try {
			Task existing = taskDao.findByUrl(task.getUrl());
			if (existing == null) {
				taskDao.save(task);
			}
			else {
				existing.setStatus('a');
				existing.setLocalPath("");
				taskDao.save(existing);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(String url, char status, char category, String localPath) {
		Task existing = taskDao.findByUrl(url);
		if (existing != null) {
			existing.setStatus(status);
			existing.setCategory(category);
			existing.setLocalPath(localPath);
			taskDao.save(existing);
		}		
	}

	@Override
	public char getStatus(String url) {
		Task existing = taskDao.findByUrl(url);
		if (existing != null) {
			return existing.getStatus();
		}
		return 0;
	}

	@Override
	public Task getTaskByUrl(String url) {
		try {
			
		return taskDao.findByUrl(url);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
