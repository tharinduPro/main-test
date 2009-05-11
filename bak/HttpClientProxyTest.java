import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpClientProxyTest{

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        HttpClient httpclient = new HttpClient();
        httpclient.getHostConfiguration().setProxy("localhost", 80);
        httpclient.getState().setProxyCredentials( new AuthScope("localhost", 80, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials("tesa", "adsf"));
        GetMethod httpget = new GetMethod("http://localhost/uPortal");
        try { 
            httpclient.executeMethod(httpget);
            System.out.println(httpget.getStatusLine());
            System.out.println(httpget.getResponseBodyAsString());
        } finally {
            httpget.releaseConnection();
        }
    }
}
