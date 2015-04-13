package com.pramati.crawler.utils;

import java.net.URL;
import java.util.Set;

import com.pramati.crawler.helpers.UrlFilter;

public interface FailureRecovery {
	Set<URL> getDwnlodedUrls(String recoveryDir,UrlFilter urlFilter);
}
