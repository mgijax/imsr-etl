package org.jax.informatics.imsr.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLHelper {
    /**
     * Attempts to open a url connection and return the response status code
     * 
     * @param url	- the url to connect to
     * @return		the response status code from attempting to connect to the url
     * @throws IOException
     * @see URL
     * @see HttpURLConnection
     */
    public static int getHTTPResponseStatusCode(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)u.openConnection();
        
// TODO: add connection timeouts
//        connection.setConnectTimeout(10000);
//        connection.setReadTimeout(10000);
        
        return connection.getResponseCode();
    }
}
