package com.pramati.crawler.utils.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pramati.crawler.utils.HyperLinkExtractor;
import com.pramati.crawler.utils.URLHelper;

/**
 * singleton class
 * 
 * purpose:- provide the capability to inspect a xml and return all the hyperlinks present in it.   
 * @author himanshuk
 *
 */
public class HyperLinkExtractorImpl implements HyperLinkExtractor{
	
	private URLHelper urlHelper;

	public List<String> getAllHyperlinks(URL url) throws Exception{
		return getHyperLinksFromXMLContent(urlHelper.getPageContentInTxtFrmt(url));
	}

	protected List<String> getHyperLinksFromXMLContent(
			String pageContentInTxtFrmt) {
		String regex="\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
		List<String> hyperlynk=new ArrayList<String>();
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(pageContentInTxtFrmt);
		while(matcher.find()){
			String str=matcher.group(2);
			if(str!=null && str.length()>0){
			hyperlynk.add(str.substring(0, str.length()-1));
			}
		}
		return hyperlynk;
	}

	public URLHelper getUrlHelper() {
		return urlHelper;
	}

	public void setUrlHelper(URLHelper urlHelper) {
		this.urlHelper = urlHelper;
	}
}
