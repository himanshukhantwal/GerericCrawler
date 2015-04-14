package com.pramati.crawler.helpers.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pramati.crawler.helpers.DownloaderHelper;

public class MailDownloadHelperImpl implements DownloaderHelper{
	
	private String downloadDir;

	public String getFileNameFrmUrlCntnt(String fileCntnt) {
			String fileName=null;
			String subjectStr=getStrPartBasedOnstr(fileCntnt, "Subject: ");
			if(subjectStr.length()>50){
				subjectStr=subjectStr.substring(0, 50);
			}
			String frmStr=getStrPartBasedOnstr(fileCntnt, "From: ");
			String dateStr=getStrPartBasedOnstr(fileCntnt,"Date: ");
			
			fileName=frmStr+" + "+subjectStr+" + "+dateStr;
			return fileName;
	}
	
	public String getDirOfFileFrmUrlCntnt(String fileCntnt) {
		String dir = "";
		String dateStr = getStrPartBasedOnstr(fileCntnt, "Date: ");
		String[] token = dateStr.split(" ");
		int replaceInd = 0;
		for (int i = 0; i < token.length; i++) {
			if (!token[i].equals(""))
				token[replaceInd++] = token[i];
		}

		if (dateStr.contains(",")) {
			if (token.length > 3) {
				if (token[3].length() == 4) {
					token[3] = (token[3].trim()).substring(2);
				}
				dir = downloadDir + "/YEAR_" + token[3] + "/" + "MONTH_"
						+ token[2].trim();
			}
		} else {
			if (token.length > 3) {
				if (token[2].length() == 4) {
					token[2] = (token[2].trim()).substring(2);
				}
				dir = downloadDir + "/YEAR_" + token[2] + "/" + "MONTH_"
						+ token[1].trim();
			}
		}
		return dir;
	}

	private String getStrPartBasedOnstr(String fileCntnt, String matchStr) {
		String returnStr="";
		Pattern pattern=Pattern.compile("\\s*"+matchStr+"\\s*(.*?)(?m)$");
		Matcher matcher=pattern.matcher(fileCntnt);
		if(matcher.find()){
			returnStr=matcher.group(1);
		}
		return returnStr;
	}
	
	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}
}
