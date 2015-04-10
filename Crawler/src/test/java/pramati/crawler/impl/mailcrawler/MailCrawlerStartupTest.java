package pramati.crawler.impl.mailcrawler;

import junit.framework.TestCase;

public class MailCrawlerStartupTest extends TestCase {
	
	private String fileCntnt;

	protected void setUp() throws Exception {
		fileCntnt="Received: by GB002EX001.uk.mytravelgroup.net with Internet Mail Service (5.5.2653.19)\n	id <RL118LX5>; Tue, 9 Sep 2003 09:59:24 +0100\nMessage-ID: <8B90BED96552D4119D5100508B694F740357CC82@NTMED-EXC001>\nFrom: \"Bateman, Patrick eMEDIA\" <Patrick.Bateman@mytravel.com>\nTo: 'Maven Users List' <users@maven.apache.org>\nSubject: RE: JUnit Test Converage Reporting\nDate: Tue, 9 Sep 2003 10:00:12 +0100\nMIME-Version: 1.0\nX-Mailer: Internet Mail Service (5.5.2653.19)\nContent-Type: text/plain;\n	charset=\"iso-8859-1\"\nX-Spam-Rating: daedalus.apache.org 1.6.2 0/1000/N\nX-Spam-Rating: minotaur-2.apache.org 1.6.2 0/1000/N\n";
		
	}

	public void testMailFileDwnldHelperImpForDirName() {
		String expected="null/YEAR_03/MONTH_Sep";
		String actual=MailCrawlerStartup.FILE_DOWNLOAD_HELPER_FOR_MAIL.getDirOfFileFrmUrlCntnt(fileCntnt);
		
		assertEquals(expected, actual);
	}
	
	public void testMailFileDwnldHelperImpForFileName(){
		String expected="\"Bateman, Patrick eMEDIA\" <Patrick.Bateman@mytravel.com> + RE: JUnit Test Converage Reporting + Tue, 9 Sep 2003 10:00:12 +0100";
		String actual=MailCrawlerStartup.FILE_DOWNLOAD_HELPER_FOR_MAIL.getFileNameFrmUrlCntnt(fileCntnt);
		
		assertEquals(expected, actual);
	}
	

}
