package pramati.crawler.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pramati.crawler.interfaces.DownloaderHelper;
import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.utils.HyperLinkExtractor;
import pramati.crawler.utils.URLHelper;
import pramati.crawler.utils.WCFileHandler;

public class UrlCrawler {
	private Set<URL> crawledUrl=new LinkedHashSet<URL>();
	private UrlFilter urlFilter;
	private DownloaderHelper fileDownloadHelperForMail;
	private ExecutorService executor;
	private int noOfThreads;
	private BlockingQueue<URL> sharedQueue;
	private boolean crawlingOver=false;

	public void startUrlCrawling(URL url, UrlFilter urlFilter,
			DownloaderHelper fileDownloadHelperForMail) throws Exception {
		sharedQueue= new ArrayBlockingQueue<URL>(noOfThreads*100);
		this.urlFilter=urlFilter;
		this.fileDownloadHelperForMail=fileDownloadHelperForMail;
		createProcessForWorker();
		crawling(url);
		crawlingOver=true;
	}

	private void crawling(URL url) throws Exception{
		if(url==null || this.urlFilter==null || this.crawledUrl.contains(url)){
			return;
		}
		if(urlFilter.isfinal(url)){
			sharedQueue.put(url);
			return;
		}
		crawledUrl.add(url);
		List<String> hyperlynk=HyperLinkExtractor.getInstance().getAllHyperlinks(url);
		List<URL> hyperlynkUrl=getUrlsFrmHyprlynk(url,hyperlynk);
		Set<URL> filteredUrl=urlFilter.filter(hyperlynkUrl);
		for(URL sngle:filteredUrl){
			crawling(sngle);
		}
	}
	private void createProcessForWorker() {
		Thread workerThread=new Thread(new Runnable() {			
			public void run() {
				startExecutorWorkers();				
			}
			private void startExecutorWorkers() {
				executor=Executors.newFixedThreadPool(noOfThreads);
				for(int i=0;i<noOfThreads*2;i++){
				DownloadWorker downloadWorker=new DownloadWorker(sharedQueue,fileDownloadHelperForMail);
				executor.execute(downloadWorker);
				}
				finishWorkers();
			}
			
			private void finishWorkers() {
				executor.shutdown();
				try {
					executor.awaitTermination(Integer.MAX_VALUE,TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!crawlingOver){
					startExecutorWorkers();
				}
				System.out.println("---DOWNLOAD COMPLETES---");
			}
		});
		workerThread.start();		
	}

	private List<URL> getUrlsFrmHyprlynk(URL url, List<String> hyperlynk) {
		List<URL> hyperlynkUrlList=new ArrayList<URL>();
		for(String snglHyprlynk:hyperlynk){
			try {
				URL hyprlynkURL=new URL(url, snglHyprlynk);
				hyperlynkUrlList.add(hyprlynkURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return hyperlynkUrlList;
	}
	public int getNoOfThreads() {
		return noOfThreads;
	}

	public void setNoOfThreads(int noOfThreads) {
		this.noOfThreads = noOfThreads;
	}
}
