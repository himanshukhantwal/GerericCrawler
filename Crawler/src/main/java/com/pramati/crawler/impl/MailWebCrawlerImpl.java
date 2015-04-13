package com.pramati.crawler.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.pramati.crawler.WebCrawler;
import com.pramati.crawler.helpers.DownloaderHelper;
import com.pramati.crawler.helpers.UrlFilter;
import com.pramati.crawler.helpers.impl.MailDownloadHelperImpl;
import com.pramati.crawler.helpers.impl.MailUrlFilterImpl;
import com.pramati.crawler.workers.UrlCrawler;

public class MailWebCrawlerImpl implements WebCrawler{
	private static final Logger log=Logger.getLogger(MailWebCrawlerImpl.class);
	private URL url;
	private UrlCrawler urlCrawler;
	private UrlFilter mailUrlFilter;
	private DownloaderHelper downloadHelper;
	private static String year="";
	private static String downloadDir;

	public void startCrawling(String[] input) throws Exception {
		log.info("starting crawler....");
		this.validateInput(input);
		setYearAndDwnldDirInhelper();
		urlCrawler.startUrlCrawling(this.url,mailUrlFilter,downloadHelper);
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
	
	private void setYearAndDwnldDirInhelper() {
		if(mailUrlFilter instanceof MailUrlFilterImpl){
			((MailUrlFilterImpl) mailUrlFilter).setYear(year);			
		}
		if(downloadHelper instanceof MailDownloadHelperImpl){
			((MailDownloadHelperImpl) downloadHelper).setDownloadDir(downloadDir);
		}
	}
	
	public UrlCrawler getUrlCrawler() {
		return urlCrawler;
	}

	public void setUrlCrawler(UrlCrawler urlCrawler) {
		this.urlCrawler = urlCrawler;
	}

	public UrlFilter getMailUrlFilter() {
		return mailUrlFilter;
	}

	public void setMailUrlFilter(UrlFilter mailUrlFilter) {
		this.mailUrlFilter = mailUrlFilter;
	}

	public DownloaderHelper getDownloadHelper() {
		return downloadHelper;
	}

	public void setDownloadHelper(DownloaderHelper downloadHelper) {
		this.downloadHelper = downloadHelper;
	}

	public static String getDownloadDir() {
		return downloadDir;
	}

	public static void setDownloadDir(String downloadDir) {
		MailWebCrawlerImpl.downloadDir = downloadDir;
	}
}


