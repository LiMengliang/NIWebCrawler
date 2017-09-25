package com.ni.crawler.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ArchiveUtils {

	 /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    
    // problematic with /home/meli/NIWebCache_Ex/attachment/TDMS%20Write%20Cluster%20Example%202015.zip
    // /home/meli/NIWebCache_Ex/attachment/NI%20IQ%20Sim.zip
    // /home/meli/NIWebCache_Ex/attachment/cRIOWfm_IO%20LV2017.zip
    // /home/meli/NIWebCache_Ex/attachment/Queue_with_Cluster_2012.zip
    public static String unzip(String zipFilePath, String destDirectory) {
    	try {
    		File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
    	} catch(IllegalArgumentException e) {
    		e.printStackTrace();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
        
        return destDirectory;
    }
    
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = null;
        Path parentPath = Paths.get(filePath).getParent();
        createParentsDirectory(parentPath);
		bos = new BufferedOutputStream(new FileOutputStream(filePath));
	    byte[] bytesIn = new byte[BUFFER_SIZE];
	    int read = 0;
	    while ((read = zipIn.read(bytesIn)) != -1) {
	        bos.write(bytesIn, 0, read);
	    }bos.close();
		
    }
    
    private static void createParentsDirectory(Path filePath) {
    	String[] segments = filePath.toString().split("/");
    	StringBuilder sb = new StringBuilder();
    	// sb.append("/");
    	for(String segment : segments) {
    		Path path = Paths.get(sb.append("/").append(segment).toString());
    		File file = path.toFile();
        	if (!file.exists()) {
        		file.mkdir();
        	}
    	}    	
    }
}
