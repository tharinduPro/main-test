import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpClientTest {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        HttpClient client = new HttpClient();   
        GetMethod method = new GetMethod("http://www.baidu.com");
        client.executeMethod(method);
        System.out.println( method.getResponseCharSet() );
        //BufferedReader in = new BufferedReader( new InputStreamReader( method.getResponseBodyAsStream(), method.getResponseCharSet() ) );
        //String inputLine = null;
        //Pattern p = Pattern.compile("\\s+<input\\stype=\"hidden\"\\sname=\"lt\"\\svalue=\"([-A-Za-z0-9_]+)\"\\s\\/>");
        //while ((inputLine = in.readLine()) != null) {
            //System.out.println( inputLine );
            //Matcher m = p.matcher( inputLine );
            //if( m.matches() ) {
                //System.out.println( m.group(1) );
                //break;
            //}
        //}

        method.releaseConnection();

    }
}

