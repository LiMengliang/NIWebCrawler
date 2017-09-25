package com.ni.crawler.model;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface ExampleDao extends CrudRepository<Example, Integer>, JpaSpecificationExecutor<Example>{

	Example findByUrl(String url);
}
