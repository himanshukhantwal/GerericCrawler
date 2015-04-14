package com.pramati.crawler.helpers.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pramati.crawler.helpers.DownloaderHelper;

public class MailDownloadHelperImpl implements DownloaderHelper{
	
	private String downloadDir;
	private String subjectRegex;
	private String fromRegex;
	private String dateRegex;

	public String getFileNameFrmUrlCntnt(String fileCntnt) {
			String fileName=null;
			String subjectStr=getStrPartBasedOnRegex(fileCntnt, subjectRegex);
			if(subjectStr.length()>50){
				subjectStr=subjectStr.substring(0, 50);
			}
			String frmStr=getStrPartBasedOnRegex(fileCntnt, fromRegex);
			String dateStr=getStrPartBasedOnRegex(fileCntnt,dateRegex);
			
			fileName=frmStr+" + "+subjectStr+" + "+dateStr;
			return fileName;
	}
	
	public String getDirOfFileFrmUrlCntnt(String fileCntnt) {
		String dir = "";
		String dateStr = getStrPartBasedOnRegex(fileCntnt, dateRegex);
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

	private String getStrPartBasedOnRegex(String fileCntnt, String regex) {
		String returnStr="";
		Pattern pattern=Pattern.compile(regex);
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
	
	public String getSubjectRegex() {
		return subjectRegex;
	}

	public void setSubjectRegex(String subjectRegex) {
		this.subjectRegex = subjectRegex;
	}

	public String getFromRegex() {
		return fromRegex;
	}

	public void setFromRegex(String fromRegex) {
		this.fromRegex = fromRegex;
	}

	public String getDateRegex() {
		return dateRegex;
	}

	public void setDateRegex(String dateRegex) {
		this.dateRegex = dateRegex;
	}


}
