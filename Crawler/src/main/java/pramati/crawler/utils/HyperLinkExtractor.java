package pramati.crawler.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * singleton class
 * 
 * purpose:- provide the capability to inspect a xml and return all the hyperlinks present in it.   
 * @author himanshuk
 *
 */
public class HyperLinkExtractor {
	private static HyperLinkExtractor instance;
	private HyperLinkExtractor(){}
	
	public static HyperLinkExtractor getInstance() {
		if(instance!=null)
			return instance;
		else
			return instance=new HyperLinkExtractor();
	}
	
	public List<String> getAllHyperlinks(URL url) throws Exception{
		return getHyperLinksFromXMLContent(URLHelper.getInstance().getPageContentInTxtFrmt(url));
	}

	private List<String> getHyperLinksFromXMLContent(
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
}
