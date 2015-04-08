package pramati.crawler.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.intf.DownloaderHelper;
import pramati.crawler.utils.HyperLinkExtractor;
import pramati.crawler.utils.URLHelper;
import pramati.crawler.utils.WCFileHandler;

public class UrlCrawler {
	private Set<URL> crawledUrl=new LinkedHashSet<URL>();
	private UrlFilter urlFilter;
	private DownloaderHelper fileDownloadHelperForMail;

	public void startUrlCrawling(URL url, UrlFilter urlFilter, DownloaderHelper fileDownloadHelperForMail) throws Exception{
		this.urlFilter=urlFilter;
		this.fileDownloadHelperForMail=fileDownloadHelperForMail;
		crawling(url);
	}
	
	private void crawling(URL url) throws Exception{
		if(url==null || this.urlFilter==null || this.crawledUrl.contains(url)){
			return;
		}
		if(urlFilter.isfinal(url)){
			downloadUrlContent(url);
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
	
	private void downloadUrlContent(URL url) throws Exception {
		System.out.println(url.toString());
		String fileContnt=URLHelper.getInstance().getPageContentInTxtFrmt(url);
		String dirName=fileDownloadHelperForMail.getDirOfFileFrmUrlCntnt(fileContnt);
		String fileName=fileDownloadHelperForMail.getFileNameFrmUrlCntnt(fileContnt);
		WCFileHandler.getInstance().createDir(dirName);
		WCFileHandler.getInstance().createFileAndWriteTxt(fileName, dirName, fileContnt);
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
