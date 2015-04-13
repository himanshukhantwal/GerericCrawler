package com.pramati.crawler.utils.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.pramati.crawler.utils.FileHandler;

public class FileHandlerImpl implements FileHandler{
	private static final Logger log=Logger.getLogger(FileHandlerImpl.class);

	public void createDir(String dirPath) throws Exception {
		File file=new File(dirPath);
		if(!file.exists()){
		if(file.mkdirs()){
			log.debug("Directory "+dirPath+" created successfully!!!");
		}else{
			//log.error("FAILED_TO_CREATE_DIRECTORY: please check if you have proper permissions");
			}
		}
	}

	public void createFileAndWriteTxt(String fileName, String dirPath,
			String textTosave) {
		fileName=fileName.replaceAll("/", "-or-");
		BufferedWriter bw=null;
		File file=new File(dirPath,fileName);
		try{
		file.createNewFile();
		bw=new BufferedWriter(new FileWriter(file));
		bw.write(textTosave);
		}catch(IOException e){
			log.error("error occured while writing: file {"+fileName+"} in dir {"+dirPath+"}",e);
		}finally{
			try {
				bw.flush();
				bw.close();
			} catch (IOException e) {
				log.error("error occured while flush: file {"+fileName+"} in dir {"+dirPath+"}",e);
			}
		}
	}

	public void createFile(String recFileName, String dirForRecFile) {
		recFileName=recFileName.replaceAll("/", "-or-");
		File file=new File(dirForRecFile,recFileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			log.error("IOException", e);
		}
		
	}

	public File[] getFileListFrmDir(String dirForRec) {
		File file=new File(dirForRec);
		if(file.exists()){
			return file.listFiles();
		}else
			return new File[0];
	}

	public boolean renameFile(String oldFileName, String newFileName) {
		File oldFile=new File(oldFileName);
		File newFile=new File(newFileName);
		return oldFile.renameTo(newFile);				
	}
	
}
