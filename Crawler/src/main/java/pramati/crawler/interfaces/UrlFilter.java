package pramati.crawler.interfaces;

import java.net.URL;
import java.util.List;
import java.util.Set;

public interface UrlFilter {
	Set<URL> filter(List<URL> hyperlynkUrl);
	boolean isfinal(URL url);
}
