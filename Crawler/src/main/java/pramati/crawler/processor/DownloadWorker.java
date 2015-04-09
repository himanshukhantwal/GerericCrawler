package pramati.crawler.processor;

import java.net.URL;
import java.util.concurrent.BlockingQueue;
import pramati.crawler.interfaces.DownloaderHelper;
import pramati.crawler.utils.URLHelper;
import pramati.crawler.utils.WCFileHandler;

public class DownloadWorker implements Runnable{
	private BlockingQueue<URL> sharedQueue;
	private DownloaderHelper fileDownloadHelperForMail;
	private String downloadDir;

	public DownloadWorker(BlockingQueue<URL> sharedQueue,
			DownloaderHelper fileDownloadHelperForMail, String downloadDir) {
		this.sharedQueue=sharedQueue;
		this.fileDownloadHelperForMail=fileDownloadHelperForMail;
		this.downloadDir=downloadDir;
	}

	public void run() {
		while(true){
			try {
				URL url=null;
				synchronized (sharedQueue) {
				url=sharedQueue.take();
				}
				if(null==url){
					break;
				}
				try {
					downloadUrlContent(url);
					createRecoveryForUrl(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createRecoveryForUrl(URL url) throws Exception{
		WCFileHandler.getInstance().createDir(downloadDir+"/Recovery");
		WCFileHandler.getInstance().createFile(url.toString(),downloadDir+"/Recovery");	
	}

	private void downloadUrlContent(URL url) throws Exception {
		System.out.println(Thread.currentThread().getName()+url.toString());
		String fileContnt=URLHelper.getInstance().getPageContentInTxtFrmt(url);
		String dirName=fileDownloadHelperForMail.getDirOfFileFrmUrlCntnt(fileContnt);
		String fileName=fileDownloadHelperForMail.getFileNameFrmUrlCntnt(fileContnt);
		WCFileHandler.getInstance().createDir(dirName);
		WCFileHandler.getInstance().createFileAndWriteTxt(fileName, dirName, fileContnt);
	}

}
