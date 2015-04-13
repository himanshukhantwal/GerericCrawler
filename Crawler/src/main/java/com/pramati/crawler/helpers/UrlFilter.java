package com.pramati.crawler.helpers;

import java.net.URL;
import java.util.List;
import java.util.Set;

public interface UrlFilter {
	Set<URL> filter(List<URL> hyperlynkUrl);
	boolean isfinal(URL url) throws Exception;
}
