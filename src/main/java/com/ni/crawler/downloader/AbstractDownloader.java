package com.ni.crawler.downloader;

import java.io.IOException;
import java.net.SocketException;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;

public abstract class AbstractDownloader implements Downloader {

	@Override
	public Page download(Request request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected abstract void onSuccess(Request requesst);
	
	protected abstract void onError(Request request);

}
