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
	private static String downloadDir;

	private static URLHelper urlHelper;
	private static FileHandler fileHandler;
	
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
	
	public DownloadWorker(BlockingQueue<URL> sharedQueue,
			DownloaderHelper fileDownloadHelperForMail, String downloadDir) {
		this.sharedQueue=sharedQueue;
		this.fileDownloadHelperForMail=fileDownloadHelperForMail;
		DownloadWorker.downloadDir=downloadDir;
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
		log.info("downloaded "+fileName);
	}
	
	public static URLHelper getUrlHelper() {
		return urlHelper;
	}

	public static void setUrlHelper(URLHelper urlHelper) {
		DownloadWorker.urlHelper = urlHelper;
	}
	
	public static FileHandler getFileHandler() {
		return fileHandler;
	}

	public static void setFileHandler(FileHandler fileHandler) {
		DownloadWorker.fileHandler = fileHandler;
	}
	
	public static String getDownloadDir() {
		return downloadDir;
	}

	public static void setDownloadDir(String downloadDir) {
		DownloadWorker.downloadDir = downloadDir;
	}

}
