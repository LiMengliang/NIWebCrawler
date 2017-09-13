package com.ni.crawler.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.CharArrayBuffer;

public class HttpClientUtilities {
	
	public final static String TEXT_MIMETYPE = "text/html";

	public static String getMimeType(HttpResponse response) {
		String contentType = response.getEntity().getContentType().getValue(); // response.getFirstHeader("Content-Type").getValue();
		return contentType.split(";")[0];
	}
	
	public static boolean isTextHtmlType(HttpResponse response) {
		return getMimeType(response).equals(TEXT_MIMETYPE);
	}
	
	public static void cacheText(String text, String localCachePath) {
		
		if (!localCachePath.isEmpty() && !localCachePath.equals("")) {
			byte data[] = text.getBytes();
			try {
				FileOutputStream out = new FileOutputStream(localCachePath);
				out.write(data);	
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.consoleWriteLine(localCachePath);
				e.printStackTrace();
			} 
		}
	}
	
	public static void cacheBinary(HttpResponse response, String localCachePath) {
		
		try {			
			if (!localCachePath.isEmpty() && !localCachePath.equals("")) {				
				HttpEntity entity = response.getEntity();
				InputStream instream = entity.getContent();
				BufferedInputStream bis = new BufferedInputStream(instream);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(localCachePath)));
				int inByte;
				while((inByte = bis.read())!=-1) {
					bos.write(inByte);
				}
				bis.close();
				bos.close();
			}			
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
