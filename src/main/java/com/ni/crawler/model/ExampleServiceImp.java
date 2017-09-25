package com.ni.crawler.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.ni.Config;

@Service
public class ExampleServiceImp implements ExampleService {

	@Autowired
	private ExampleDao exampleDao;
	
//	public ExampleServiceImp() {
//		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//		exampleDao = context.getBean(ExampleDao.class);
//	}

	@Override
	public void saveExample(Example example) {
		try {			
			Example existing = exampleDao.findByUrl(example.getUrl());
			if (existing == null) {
				exampleDao.save(example);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
