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
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
