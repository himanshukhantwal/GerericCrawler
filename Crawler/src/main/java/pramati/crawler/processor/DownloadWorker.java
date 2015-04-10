package pramati.crawler.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import pramati.crawler.interfaces.DownloaderHelper;
import pramati.crawler.utils.URLHelper;
import pramati.crawler.utils.WCFileHandler;

public class DownloadWorker implements Runnable{
	private static final Logger log=Logger.getLogger(DownloadWorker.class);
	private BlockingQueue<URL> sharedQueue;
	private DownloaderHelper fileDownloadHelperForMail;
	private String downloadDir;
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
		this.downloadDir=downloadDir;
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
		WCFileHandler.getInstance().createDir(downloadDir+"/Recovery");
		WCFileHandler.getInstance().createFile(url.toString(),downloadDir+"/Recovery");	
	}

	private void downloadUrlContent(URL url) throws Exception {
		String fileContnt=URLHelper.getInstance().getPageContentInTxtFrmt(url);
		String dirName=fileDownloadHelperForMail.getDirOfFileFrmUrlCntnt(fileContnt);
		String fileName=fileDownloadHelperForMail.getFileNameFrmUrlCntnt(fileContnt);
		WCFileHandler.getInstance().createDir(dirName);
		WCFileHandler.getInstance().createFileAndWriteTxt(fileName, dirName, fileContnt);
		log.info("downloaded "+fileName);
	}

}
