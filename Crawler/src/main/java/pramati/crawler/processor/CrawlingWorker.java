package pramati.crawler.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.utils.HyperLinkExtractor;

public class CrawlingWorker implements Runnable {
	private Set<URL> crawledUrl = new LinkedHashSet<URL>();
	private UrlFilter urlFilter;
	private BlockingQueue<URL> sharedQueue;
	private BlockingQueue<URL> urlToBeCrawled;

	public CrawlingWorker(UrlFilter urlFilter, BlockingQueue<URL> sharedQueue,
			BlockingQueue<URL> urlToBeCrawled, Set<URL> crawledUrl) {
		this.urlFilter = urlFilter;
		this.sharedQueue = sharedQueue;
		this.urlToBeCrawled = urlToBeCrawled;
		this.crawledUrl = crawledUrl;
	}

	public void run() {
		while (true) {
			URL url = null;
			try {
				url = urlToBeCrawled.take();
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
				List<String> hyperlynk = HyperLinkExtractor.getInstance()
						.getAllHyperlinks(url);
				List<URL> hyperlynkUrl = getUrlsFrmHyprlynk(url, hyperlynk);
				Set<URL> filteredUrl = urlFilter.filter(hyperlynkUrl);
				urlToBeCrawled.addAll(filteredUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
