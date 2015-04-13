package com.pramati.crawler.utils;

import java.net.URL;
import java.util.List;

public interface HyperLinkExtractor {
	List<String> getAllHyperlinks(URL url) throws Exception;
}
