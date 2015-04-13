package pramati.crawler.impl.mailcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import pramati.crawler.interfaces.DownloaderHelper;
import pramati.crawler.interfaces.UrlFilter;
import pramati.crawler.interfaces.WebCrawlerImp;
import pramati.crawler.processor.UrlCrawler;

public class MailCrawlerStartup implements WebCrawlerImp,ApplicationContextAware{
	private static final Logger log=Logger.getLogger(MailCrawlerStartup.class);
	
	private ApplicationContext context;
	private URL url;
	private static String year="";
	private static String downloadDir;
	
	public void startCrawling(String[] input) throws Exception {
		log.info("starting crawler....");
		this.validateInput(input);
		UrlCrawler urlCrawler=(UrlCrawler) context.getBean("urlcrawler");
		urlCrawler.startUrlCrawling(this.url,URL_FILTER_FOR_MAIL,FILE_DOWNLOAD_HELPER_FOR_MAIL,downloadDir);
	}
	
	private void validateInput(String[] input) throws MalformedURLException {
		try {
			url=new URL(input[0]);
		} catch (MalformedURLException e) {
			log.error("URL_NOT_PROPER", e);
			throw e;
		}
		try{
		Integer.parseInt(input[1]);
		year=input[1];
		}catch(NumberFormatException e){
			year="";
		}
	}
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context=context;
	}
	
	public String getDownloadDir() {
		return downloadDir;
	}
	public void setDownloadDir(String downloadDir) {
		MailCrawlerStartup.downloadDir = downloadDir;
	}
	protected static final UrlFilter URL_FILTER_FOR_MAIL =new UrlFilter(){

		public Set<URL> filter(List<URL> urlList) {
			Set<URL> filteredUrlSet=new HashSet<URL>();
			for(URL url:urlList){
				Pattern pattern=Pattern.compile(year+"([0-9])([0-9]?)"+".mbox");
				Matcher matcher=pattern.matcher(url.toString());
				if (matcher.find()
						&& !((url.toString()).contains(".mbox/date"))
						&& !((url.toString()).contains(".mbox/author"))
						&& !((url.toString()).contains(".mbox/browser"))) {
					filteredUrlSet.add(url);
				}
			}
			
			return filteredUrlSet;
		}

		public boolean isfinal(URL url) throws Exception {
			if(url.toString().contains("raw") && !url.toString().contains("${")){
				Pattern pattern=Pattern.compile("raw/%3c"+"(.*?)"+"%3e"+"(?m)$");
				Matcher matcher=pattern.matcher(url.toString());
				if(matcher.find()){
					return true;
				}
			}
			return false;
		}
	};
	
	protected static final DownloaderHelper FILE_DOWNLOAD_HELPER_FOR_MAIL=new DownloaderHelper() {
		
		public String getFileNameFrmUrlCntnt(String fileCntnt) {
				String fileName=null;
				String subjectStr=getStrPartBasedOnstr(fileCntnt, "Subject: ");
				if(subjectStr.length()>50){
					subjectStr=subjectStr.substring(0, 50);
				}
				String frmStr=getStrPartBasedOnstr(fileCntnt, "From: ");
				String dateStr=getStrPartBasedOnstr(fileCntnt,"apmail-maven-users-archive=maven.apache.org@maven.apache.org ");
				
				fileName=frmStr+" + "+subjectStr+" + "+dateStr;
				return fileName;
		}
		
		public String getDirOfFileFrmUrlCntnt(String fileCntnt) {
			String dir = "";
			String dateStr = getStrPartBasedOnstr(fileCntnt, "apmail-maven-users-archive=maven.apache.org@maven.apache.org ");
			String[] token = dateStr.split(" ");
			int replaceInd = 0;
			for (int i = 0; i < token.length; i++) {
				if (!token[i].equals(""))
					token[replaceInd++] = token[i];
			}
				if (token.length == 5) {
					if (token[4].length() == 4) {
						token[4] = (token[4].trim()).substring(2);
					}
					dir = downloadDir + "/YEAR_" + token[4] + "/" + "MONTH_"
							+ token[1].trim();
				}
			return dir;
		}

		private String getStrPartBasedOnstr(String fileCntnt, String matchStr) {
			String returnStr="";
			Pattern pattern=Pattern.compile("\\s*"+matchStr+"\\s*(.*?)(?m)$");
			Matcher matcher=pattern.matcher(fileCntnt);
			if(matcher.find()){
				returnStr=matcher.group(1);
			}
			return returnStr;
		}
	};
}


