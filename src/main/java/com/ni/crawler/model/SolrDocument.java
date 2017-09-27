package com.ni.crawler.model;

import org.apache.solr.common.SolrInputDocument;

public interface SolrDocument {

	SolrInputDocument toSolrInputDocument();
}
