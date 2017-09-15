package com.ni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ni.crawler.model.TaskDao;

@RestController
@RequestMapping("/create")
public class HelloWorldController {
	
	@RequestMapping(value ="hello", method=RequestMethod.GET)
	public String sayHelloWorld() {
		return "HelloWorld";
	}
}
