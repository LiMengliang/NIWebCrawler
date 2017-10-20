package com.ni.crawler.downloader;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;

import com.ni.crawler.model.TaskService;

public class FtpProtocolCommandListener implements ProtocolCommandListener {

	private TaskService taskService;
	
	private String url;
	
	private String localPath;
	
	public FtpProtocolCommandListener(TaskService taskService, String url, String localPath) {
		
		this.taskService = taskService;
		this.url = url;
		this.localPath = localPath;
	}
	
	@Override
	public void protocolCommandSent(ProtocolCommandEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void protocolReplyReceived(ProtocolCommandEvent event) {
		if (event.getReplyCode() == 226) {
			if (taskService != null) {
				taskService.update(url, 'c', 'b', localPath);
			}
		}
		
	}

}
