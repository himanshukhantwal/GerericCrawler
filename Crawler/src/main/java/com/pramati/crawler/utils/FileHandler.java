package com.pramati.crawler.utils;

import java.io.File;

public interface FileHandler {
	void createDir(String dirPath) throws Exception ;
	void createFileAndWriteTxt(String fileName, String dirPath,String textTosave);
	void createFile(String recFileName, String dirForRecFile);
	File[] getFileListFrmDir(String dirForRec);
	boolean renameFile(String oldFileName, String newFileName);
}
