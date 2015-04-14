package com.pramati.crawler.workers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.pramati.crawler.helpers.DownloaderHelper;
import com.pramati.crawler.utils.FileHandler;
import com.pramati.crawler.utils.URLHelper;

public class DownloadWorker implements Runnable{
	private static final Logger log=Logger.getLogger(DownloadWorker.class);
	private String downloadDir;
	private URLHelper urlHelper;
	private FileHandler fileHandler;
	
	private BlockingQueue<URL> sharedQueue;
	private DownloaderHelper fileDownloadHelperForMail;

	private static URL POISON;

	static{
		URL temp;
		try {
			temp=new URL("http://www.example.com");
		} catch (MalformedURLException e) {
			temp=null;
			log.error("Error Creation Terminating condition For Downloader Thread", e);
		}
		POISON=temp;
	}
	
	public void init(BlockingQueue<URL> sharedQueue,
			DownloaderHelper fileDownloadHelperForMail) {
		this.sharedQueue=sharedQueue;
		this.fileDownloadHelperForMail=fileDownloadHelperForMail;
	}

	public void run() {
		Thread.currentThread().setName("DownLoader Thread:"+Thread.currentThread().getName());
		while(true){
			try {
				URL url=null;
				synchronized (sharedQueue) {
				url=sharedQueue.take();
				}
				if(POISON.toString().equals(url.toString())){
					break;
				}
				try {
					downloadUrlContent(url);
					createRecoveryForUrl(url);
				} catch (Exception e) {
					log.error("Exception in downloading "+url.toString(), e);
				}
			} catch (InterruptedException e) {
				log.error("InterruptedException",e);
			}
		}
	}
	
	private void createRecoveryForUrl(URL url) throws Exception{
		fileHandler.createDir(downloadDir+"/Recovery");
		fileHandler.createFile(url.toString(),downloadDir+"/Recovery");	
	}

	private void downloadUrlContent(URL url) throws Exception {
		String fileContnt=urlHelper.getPageContentInTxtFrmt(url);
		String dirName=fileDownloadHelperForMail.getDirOfFileFrmUrlCntnt(fileContnt);
		String fileName=fileDownloadHelperForMail.getFileNameFrmUrlCntnt(fileContnt);
		fileHandler.createDir(dirName);
		fileHandler.createFileAndWriteTxt(fileName, dirName, fileContnt);
		log.info("downloaded {"+fileName+"} successfully");
	}
	
	public URLHelper getUrlHelper() {
		return urlHelper;
	}

	public void setUrlHelper(URLHelper urlHelper) {
		this.urlHelper = urlHelper;
	}
	
	public FileHandler getFileHandler() {
		return fileHandler;
	}

	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}
	
	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

}
