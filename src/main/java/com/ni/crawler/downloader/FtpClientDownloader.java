package com.ni.crawler.downloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import com.ni.crawler.model.Page;
import com.ni.crawler.model.Request;
import com.ni.crawler.model.TaskService;
import com.ni.crawler.utils.Log;

public class FtpClientDownloader extends AbstractDownloader {
	
	private  FTPClient ftp = null;
	private TaskService taskService;
	
	public FtpClientDownloader(TaskService taskService) throws Exception {
		//if (ftp == null) {
			this.taskService = taskService;
	        ftp = new FTPClient();
	        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	        int reply;
	        ftp.connect("ftp.ni.com");
	        reply = ftp.getReplyCode();
	        if (!FTPReply.isPositiveCompletion(reply)) {
	            ftp.disconnect();
	            throw new Exception("Exception in connecting to FTP Server");
	        }
	        ftp.login("anonymous", "");
	        ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        ftp.enterLocalPassiveMode();
		//}
    }

    public void downloadFile(String remoteFilePath, String localFilePath) {
        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            this.ftp.retrieveFile(remoteFilePath, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already downloaded from FTP server
            }
        }
    }


	private static final String ATTACHMENT_LOCAL_CACHE_PATH = "/home/meli/NIWebCache/attachment";
//	private static final String ATTACHMENT_LOCAL_CACHE_PATH = "/home/meli";
	@Override
	public Page download(Request request) {
		Log.consoleWriteLine((new StringBuilder("[FTP] Start fetching from ").append(request.getUrl()).append("[").append(Thread.currentThread().getId()).append("]")).toString());
		
		int nameIndex = request.getUrl().lastIndexOf('/');
		String localPath = ATTACHMENT_LOCAL_CACHE_PATH + request.getUrl().substring(nameIndex);
		int ftpNameIndex = request.getUrl().indexOf("pub");
		String remoteName = request.getUrl().substring(ftpNameIndex);
		try (FileOutputStream fos = new FileOutputStream(localPath)) {
			//ftp.connect("ftp.ni.com");
            this.ftp.retrieveFile(remoteName, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	disconnect();
        }
		if (taskService != null) {
			taskService.update(request.getUrl(), 'c', 'b', localPath);
		}
		return new Page("", request.getUrl());
		
//		works
//		int nameIndex = request.getUrl().lastIndexOf('/');
//		String localPath = ATTACHMENT_LOCAL_CACHE_PATH + request.getUrl().substring(nameIndex);
//		int ftpNameIndex = request.getUrl().indexOf("pub");
//		String remoteName = request.getUrl().substring(ftpNameIndex);
//		try (FileOutputStream fos = new FileOutputStream(localPath)) {
//            this.ftp.retrieveFile(remoteName, fos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }		
//		return null;
	}
	
	@Override
	protected void onSuccess(Request requesst) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onError(Request request) {
		// TODO Auto-generated method stub
		
	}

}
