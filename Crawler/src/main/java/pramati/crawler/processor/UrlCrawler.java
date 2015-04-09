package pramati.crawler.processor;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import pramati.crawler.interfaces.DownloaderHelper;
import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.utils.FailureRecovery;

public class UrlCrawler {
	private Set<URL> crawledUrl=new LinkedHashSet<URL>();
	private UrlFilter urlFilter;
	private DownloaderHelper fileDownloadHelperForMail;
	private int noOfThreads;
	private BlockingQueue<URL> sharedQueue;
	private String downloadDir;
	private BlockingQueue<URL> urlToBeCrawled;

	public void startUrlCrawling(URL url, UrlFilter urlFilter,
			DownloaderHelper fileDownloadHelperForMail,String downloadDir) throws Exception {
		sharedQueue= new ArrayBlockingQueue<URL>(noOfThreads*100);
		this.urlFilter=urlFilter;
		this.fileDownloadHelperForMail=fileDownloadHelperForMail;
		this.downloadDir=downloadDir;
		
		this.doRecoveryProcess();
		createWorkerForDownld();
		crawling(url);
	}

	private void doRecoveryProcess() {
		crawledUrl.addAll(FailureRecovery.getInstance().getDwnlodedUrls(downloadDir+"/Recovery"));
	}

	private void crawling(URL url) throws Exception{
		urlToBeCrawled=new ArrayBlockingQueue<URL>(noOfThreads*10000);
		Collections.synchronizedSet(crawledUrl);
		urlToBeCrawled.add(url);
		for(int i=0;i<noOfThreads;i++){
			Thread crawlingWorker = new Thread(new CrawlingWorker(urlFilter,
					sharedQueue, urlToBeCrawled, crawledUrl));
			crawlingWorker.start();
		}
	}
	private void createWorkerForDownld() {
		for(int i=0;i<noOfThreads*2;i++){
		Thread downloadWorker=new Thread(new DownloadWorker(sharedQueue, fileDownloadHelperForMail, downloadDir));
		downloadWorker.start();
		}		
	}
	
	public int getNoOfThreads() {
		return noOfThreads;
	}

	public void setNoOfThreads(int noOfThreads) {
		this.noOfThreads = noOfThreads;
	}
}
