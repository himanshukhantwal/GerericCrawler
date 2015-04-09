package pramati.crawler;

import java.util.Scanner;

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
    	String[] input=getUserInput();
    	ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
    	WebCrawlerImp crawler=(WebCrawlerImp) context.getBean("webcrawlerimplementation");
    	crawler.startCrawling(input);   	
    }
    private static String[] getUserInput() {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
    	String[] inputStrArray=new String[2];
    	System.out.println("Enter Your URL:");
    	inputStrArray[0]=scanner.nextLine();
    	System.out.println("Enter Year:");
    	inputStrArray[1]=scanner.nextLine();
    	return inputStrArray;
	}
}
