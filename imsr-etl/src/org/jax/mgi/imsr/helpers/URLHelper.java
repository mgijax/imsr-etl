package org.jax.mgi.imsr.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLHelper {
    public static int getHTTPResponseStatusCode(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)u.openConnection();
        
        return connection.getResponseCode();
    }
}
