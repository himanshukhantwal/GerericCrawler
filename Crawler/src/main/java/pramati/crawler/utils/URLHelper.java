package pramati.crawler.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

/**
 * singleton class
 * 
 * purpose:- used to manipulate url's and getting url text and other URL information.
 * @author himanshuk
 *
 */
public class URLHelper {
	private static URLHelper instance; 
	private URLHelper(){}
	
	public static URLHelper getInstance() {
		if(instance!=null)
			return instance;
		else
			return instance=new URLHelper();
	}

	public String getPageContentInTxtFrmt(URL url) throws Exception {
		return getPageContent(url);
	}

	private String getPageContent(URL url) throws Exception {
		BufferedInputStream reader=null;
		try {
			reader=new BufferedInputStream(url.openStream());
		} catch (IOException e) {
			throw new Exception("PROBLEM_IN_URL_READING",e);
		}
		
		Writer writer=new StringWriter();
		for(int c=reader.read();c!=-1;c=reader.read()){
			writer.write(c);
		}
		return writer.toString();
	}

}
