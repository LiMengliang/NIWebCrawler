package com.ni.crawler.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

@Service
public class TaskServiceImp implements TaskService {
	
	private ReentrantLock lock = new ReentrantLock();
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
	public boolean addUniqueTask(Task task) {
		// try {
		boolean taskUnprocessed = false;
		lock.lock();
		try {
			Task existing = taskDao.findByUrl(task.getUrl());
			if (existing == null) {
				taskDao.save(task);
				taskUnprocessed = true;
			}
			else if (existing.getStatus() != 'c') {
				existing.setStatus('a');
				existing.setLocalPath("");
				taskDao.save(existing);
				taskUnprocessed = true;
			}
		} finally {
			lock.unlock();
		}
		return taskUnprocessed;
			
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void update(String url, char status, char category, String localPath) {
		lock.lock();
		try {
			
		Task existing = taskDao.findByUrl(url);
			if (existing != null) {
				existing.setStatus(status);
				existing.setCategory(category);
				existing.setLocalPath(localPath);
				taskDao.save(existing);
			}
		} finally {
			lock.unlock();
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

	@Override
	public Page<Task> getAllDownloadedTasks(Integer page, Integer size) {	
		
		PageRequest request = new PageRequest(page - 1, size, null);
		Specification<Task> specification = new Specification<Task>() {
			@Override
			public Predicate toPredicate(Root<Task> root,
					CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Path<Character> $status = root.get("status");
				Predicate statusPredicate = criteriaBuilder.equal($status, 'c');
				return criteriaBuilder.and(statusPredicate);
			}
		};
		Page<Task> tasks = this.taskDao.findAll(specification,request);
		return tasks;
		
		// return taskDao.findByStatus('c');
//		 Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");  
//		 Page<Task> taskPage = taskDao.findAll(new Specification<Task>(){  
//	            @Override  
//	            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {  
//	                List<Predicate> list = new ArrayList<Predicate>();  
//	                if('c' ==task.getStatus()){  
//	                    list.add(criteriaBuilder.equal(root.get("status").as(String.class), task.getStatus()));  
//	                }  
//	                Predicate[] p = new Predicate[list.size()];  
//	                return criteriaBuilder.and(list.toArray(p));  
//	            }  
//	        },pageable);  
//	        return taskPage;  
	}	
}
