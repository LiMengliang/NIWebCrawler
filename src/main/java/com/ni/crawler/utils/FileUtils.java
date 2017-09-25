package com.ni.crawler.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

	// try-with-resources pattern recommended in API guide. It ensures that no matter circumstances the stream will be closed.
	public static List<Path> listFilesInDirectory(String directory) throws IOException {
		
		List<Path> files = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(x -> {
		        	files.add(x);
		        });
		} 
		return files;
	}
}
