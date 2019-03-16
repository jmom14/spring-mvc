package com.example.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService implements IUploadFileService {
	
	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass()); 
	
	private final static String UPLOADS_FOLDER = "uploads";

	@Override
	public Resource load(String file) throws MalformedURLException {
		Path pathPhoto = getPath(file);
		Resource resource = null;
			
			resource = new UrlResource(pathPhoto.toUri());
			if(!resource.exists() || !resource.isReadable()) {
				throw new RuntimeException("Cannot load the image");
			}		
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException{
		String name = file.getOriginalFilename();
		Path rootPath = getPath(name);			
		Files.copy(file.getInputStream(), rootPath);			
		
		return name;
	}

	@Override
	public boolean delete(String file) {
		Path rootPath = getPath(file);
		File file1 = rootPath.toFile();
		
		if(file1.exists() && file1.canRead()) {
			if(file1.delete()) {
				return true;
			}
		}		
		return false;
	}
	
	public Path getPath(String file) {
		return Paths.get(UPLOADS_FOLDER).resolve(file).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(UPLOADS_FOLDER));		
	}

}
