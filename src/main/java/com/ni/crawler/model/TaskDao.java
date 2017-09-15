package com.ni.crawler.model;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Transactional
public interface TaskDao  extends CrudRepository<Task, Integer> {
	
	// @Query("select * from tasks t where t.url = ?1")
	Task findByUrl(String url);
	
	List<Task> findByStatusNot(char status);
}
