package pramati.crawler.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class FailureRecovery {
	private static FailureRecovery instance;
	private FailureRecovery(){}
		
	public static FailureRecovery getInstance(){
		if(instance!=null)
			return instance;
		else
			return instance=new FailureRecovery();
	}


	public Set<URL> getDwnlodedUrls(String recoveryDir) {
		File[] fileList=WCFileHandler.getInstance().getFileListFrmDir(recoveryDir);
		Set<URL> urlSet=new LinkedHashSet<URL>();
		for(File file:fileList){
			if(file.isFile()){
				try {
					urlSet.add(new URL(file.getName().replaceAll("-or-", "/")));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return urlSet;
	}
}
