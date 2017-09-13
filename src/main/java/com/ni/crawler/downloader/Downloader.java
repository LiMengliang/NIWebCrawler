package com.ni.crawler.downloader;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;

public interface Downloader {

	Page download(Request request);
}
