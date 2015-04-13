package pramati.crawler.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.utils.HyperLinkExtractor;

public class CrawlingWorker implements Runnable {
	private static final Logger log =Logger.getLogger(CrawlingWorker.class);
	private Set<URL> crawledUrl = new LinkedHashSet<URL>();
	private UrlFilter urlFilter;
	private BlockingQueue<URL> sharedQueue;
	private BlockingQueue<URL> urlToBeCrawled;
	private static final URL POISON;
	
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

	public CrawlingWorker(UrlFilter urlFilter, BlockingQueue<URL> sharedQueue,
			BlockingQueue<URL> urlToBeCrawled, Set<URL> crawledUrl) {
		this.urlFilter = urlFilter;
		this.sharedQueue = sharedQueue;
		this.urlToBeCrawled = urlToBeCrawled;
		this.crawledUrl = crawledUrl;
	}

	public void run() {
		Thread.currentThread().setName("Crawling Thread:"+Thread.currentThread().getName());
		while (true) {
			URL url = null;
			try {
				url = urlToBeCrawled.poll(10,TimeUnit.SECONDS);
				if (url == null || this.urlFilter == null) {
					break;
				}
				if(this.crawledUrl.contains(url)){
					continue;
				}
				crawledUrl.add(url);

				if (urlFilter.isfinal(url)) {
					sharedQueue.put(url);
					continue;
				}
				try{
				List<String> hyperlynk = HyperLinkExtractor.getInstance()
						.getAllHyperlinks(url);
				List<URL> hyperlynkUrl = getUrlsFrmHyprlynk(url, hyperlynk);
				Set<URL> filteredUrl = urlFilter.filter(hyperlynkUrl);
				urlToBeCrawled.addAll(filteredUrl);
				}catch(Exception e){
					log.error("Error Reading URL "+url.toString(),e);
				}
			} catch (Exception e) {
				log.error("Error IN crawling "+(url!=null?url.toString():""), e);
			}
		}
			try {
				sharedQueue.put(POISON);
			} catch (InterruptedException e) {
				log.error("InterruptedException", e);
			}
	}

	private List<URL> getUrlsFrmHyprlynk(URL url, List<String> hyperlynk) {
		List<URL> hyperlynkUrlList = new ArrayList<URL>();
		for (String snglHyprlynk : hyperlynk) {
			try {
				URL hyprlynkURL = new URL(url, snglHyprlynk);
				hyperlynkUrlList.add(hyprlynkURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return hyperlynkUrlList;
	}

}
