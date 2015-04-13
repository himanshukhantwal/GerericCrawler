package com.pramati.crawler.utils.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.pramati.crawler.helpers.UrlFilter;
import com.pramati.crawler.utils.FailureRecovery;
import com.pramati.crawler.utils.FileHandler;

public class FailureRecoveryImpl implements FailureRecovery{

	private FileHandler fileHandler;

	public Set<URL> getDwnlodedUrls(String recoveryDir,UrlFilter urlFilter) {
		File[] fileList=fileHandler.getFileListFrmDir(recoveryDir);
		Set<URL> urlSet=new LinkedHashSet<URL>();
		for(File file:fileList){
			if(file.isFile()){
				try {
					urlSet.add(new URL(file.getName().replaceAll("-or-", "/")));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		List<URL> temp=new ArrayList<URL>();
		temp.addAll(urlSet);
		return urlFilter.filter(temp);
	}
	
	public FileHandler getFileHandler() {
		return fileHandler;
	}

	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}
}
