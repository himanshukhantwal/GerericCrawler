package com.pramati.crawler.utils.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class HyperLinkExtractorImplTest extends TestCase {

	private HyperLinkExtractorImpl xmlHyperlinkExtractor;
	private String strWithMnthHyperlynk;
	private String strWithMsgUrls;

	protected void setUp() throws Exception {
		xmlHyperlinkExtractor=new HyperLinkExtractorImpl();
		strWithMnthHyperlynk="<td class=\"date\">some Date 1992</td>\n<td class=\"links\"><span class=\"links\" id=\"201503\"><a href=\"201503.mbox/thread\">Thread</a> &middot; <a href=\"201503.mbox/date\">Date</a> &middot; <a href=\"201503.mbox/author\">Author</a></span></td>";
		strWithMsgUrls="<tr>\n		    <td class=\"author\">Philipp Kraus</td>\n		     <td class=\"subject\"><a href=\"%3c383A5304-FEA1-4032-B712-F771525B3EF6@tu-clausthal.de%3e\">write developers &amp; contributors into Jar</a>     </td>\n		    <td class=\"date\">Sun, 01 Mar, 11:41</td>\n		   </tr>";
	}

	public void testGetAllHyperlinks() {
		List<String> expected=new ArrayList<String>();
		expected.add("201503.mbox/thread");
		expected.add("201503.mbox/date");
		expected.add("201503.mbox/author");
		List<String> actual=xmlHyperlinkExtractor.getHyperLinksFromXMLContent(strWithMnthHyperlynk);
		
		assertEquals(expected, actual);	
	}
	
	public void testGetAllHyperlinks2(){
		List<String> expected=new ArrayList<String>();
		expected.add("%3c383A5304-FEA1-4032-B712-F771525B3EF6@tu-clausthal.de%3e");
		List<String> actual=xmlHyperlinkExtractor.getHyperLinksFromXMLContent(strWithMsgUrls);
		
		assertEquals(expected, actual);	
	}

}
