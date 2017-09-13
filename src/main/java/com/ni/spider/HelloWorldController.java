package com.ni.spider;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWorldController {
	
	@RequestMapping(value ="hello", method=RequestMethod.GET)
	public String sayHelloWorld() {
		return "HelloWorld";
	}
}
