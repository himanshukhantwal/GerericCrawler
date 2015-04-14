package com.pramati.crawler.workers;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.pramati.crawler.helpers.DownloaderHelper;
import com.pramati.crawler.helpers.UrlFilter;
import com.pramati.crawler.utils.FailureRecovery;
import com.pramati.crawler.utils.FileHandler;
import com.pramati.crawler.utils.HyperLinkExtractor;
import com.pramati.crawler.utils.URLHelper;

public abstract class UrlCrawler {
	private static final Logger log = Logger.getLogger(UrlCrawler.class);
	private FailureRecovery failureRecovery;

	private String downloadDir;
	private int noOfThreads;

	private UrlFilter urlFilter;
	private DownloaderHelper fileDownloadHelperForMail;

	private Set<URL> crawledUrl;
	private BlockingQueue<URL> sharedQueue;
	private BlockingQueue<URL> urlToBeCrawled;

	public void startUrlCrawling(URL url, UrlFilter urlFilter,
			DownloaderHelper fileDownloadHelperForMail) throws Exception {
		sharedQueue = new ArrayBlockingQueue<URL>(noOfThreads * 100);
		crawledUrl = new LinkedHashSet<URL>();
		this.urlFilter = urlFilter;
		this.fileDownloadHelperForMail = fileDownloadHelperForMail;

		this.doRecoveryProcess();
		createWorkerForDownld();
		createWorkerForCrawling(url);
	}

	private void doRecoveryProcess() {
		log.info("Recovery From Previous Failures");
		crawledUrl.addAll(failureRecovery.getDwnlodedUrls(downloadDir
				+ "/Recovery", urlFilter));
		log.info("Total " + crawledUrl.size() + " Already Downloaded!!!\n");
	}

	private void createWorkerForCrawling(URL url) throws Exception {
		urlToBeCrawled = new ArrayBlockingQueue<URL>(noOfThreads * 1000000);
		Collections.synchronizedSet(crawledUrl);
		urlToBeCrawled.add(url);

		for (int i = 0; i < noOfThreads * 2; i++) {
			CrawlingWorker crawlingWorker = createCrawlingWrkr();
			crawlingWorker.init(urlFilter, sharedQueue, urlToBeCrawled,
					crawledUrl);
			Thread crwlngWrkrThread = new Thread(crawlingWorker);
			crwlngWrkrThread.start();
		}
	}

	private void createWorkerForDownld() {

		for (int i = 0; i < noOfThreads; i++) {
			DownloadWorker downloadWorker=createDwnldWrkr();
			downloadWorker.init(sharedQueue,fileDownloadHelperForMail);
			Thread dwnldWrkrThread = new Thread(downloadWorker);
			dwnldWrkrThread.start();
		}
	}

	public int getNoOfThreads() {
		return noOfThreads;
	}

	public void setNoOfThreads(int noOfThreads) {
		this.noOfThreads = noOfThreads;
	}

	public FailureRecovery getFailureRecovery() {
		return failureRecovery;
	}

	public void setFailureRecovery(FailureRecovery failureRecovery) {
		this.failureRecovery = failureRecovery;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	protected abstract CrawlingWorker createCrawlingWrkr();

	protected abstract DownloadWorker createDwnldWrkr();
}
