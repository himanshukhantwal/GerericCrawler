package com.pramati.crawler.helpers.impl;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pramati.crawler.helpers.UrlFilter;

public class MailUrlFilterImpl implements UrlFilter{
	
	private String year;
	private String urlFilterRegex;
	private String endConditionRegex;

	public Set<URL> filter(List<URL> urlList) {
		Set<URL> filteredUrlSet=new HashSet<URL>();
		for(URL url:urlList){
			Pattern pattern=Pattern.compile(year+urlFilterRegex);
			Matcher matcher=pattern.matcher(url.toString());
			if (matcher.find()) {
				filteredUrlSet.add(url);
			}
		}
		
		return filteredUrlSet;
	}

	public boolean isfinal(URL url) throws Exception {
		if(url.toString().contains("raw") && !url.toString().contains("${")){
			Pattern pattern=Pattern.compile(endConditionRegex);
			Matcher matcher=pattern.matcher(url.toString());
			if(matcher.find()){
				return true;
			}
		}
		return false;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getUrlFilterRegex() {
		return urlFilterRegex;
	}

	public void setUrlFilterRegex(String urlFilterRegex) {
		this.urlFilterRegex = urlFilterRegex;
	}

	public String getEndConditionRegex() {
		return endConditionRegex;
	}

	public void setEndConditionRegex(String endConditionRegex) {
		this.endConditionRegex = endConditionRegex;
	}

}
