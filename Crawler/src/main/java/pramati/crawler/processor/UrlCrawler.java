package pramati.crawler.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.utils.HyperLinkExtractor;

public class UrlCrawler {
	private Set<URL> crawledUrl=new LinkedHashSet<URL>();

	public void startUrlCrawling(URL url, UrlFilter urlFilter) throws Exception{
		if(url==null || urlFilter==null || crawledUrl.contains(url)){
			return;
		}
		if(urlFilter.isfinal(url)){
			System.out.println(url.toString());
			
		}
		crawledUrl.add(url);
		List<String> hyperlynk=HyperLinkExtractor.getInstance().getAllHyperlinks(url);
		List<URL> hyperlynkUrl=getUrlsFrmHyprlynk(url,hyperlynk);
		Set<URL> filteredUrl=urlFilter.filter(hyperlynkUrl);
		for(URL sngle:filteredUrl){
		startUrlCrawling(sngle, urlFilter);
		}
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
}
