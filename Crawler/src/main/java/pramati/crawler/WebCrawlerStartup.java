package pramati.crawler;

import java.net.MalformedURLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pramati.crawler.interfaces.WebCrawlerImp;

/**
 * Hello world!
 *
 */
public class WebCrawlerStartup
{
    public static void main( String[] args ) throws Exception
    {
    	String[] input=new String[]{"http://mail-archives.apache.org/mod_mbox/maven-users/","2015"};
    	ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
    	WebCrawlerImp crawler=(WebCrawlerImp) context.getBean("webcrawlerimplementation");
    	crawler.startCrawling(input);   	
    }
}
