import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpClientTest {

    private String URL = "http://19.16.8.123/auth.asp?url=/index/index/transform.asp?default=9&threeblockcode=001801&blockcode=YBQAA";
    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
    }

    //public test( File file ) {
        //HttpClient client = new HttpClient();   
        //GetMethod method = new GetMethod( this.URL );
        //client.executeMethod(method);
        //String encoding = method.getResponseCharSet();
        //BufferedReader in = new BufferedReader( new InputStreamReader( method.getResponseBodyAsStream(), encoding ) );
        //String inputLine = null;
        //StringBuilder sb = new StringBuilder();
        //while ((inputLine = in.readLine()) != null) {
            //sb.append( inputLine );
        //}

        //File file = new File( file );
        //file.createNewFile();//创建文件
        //PrintStream printStream = new PrintStream(new FileOutputStream(file), true, encoding );
        //printStream.println( sb.toString() );//将字符串写入文件
        //printStream.close();//写入完成关闭
        //method.releaseConnection();
    //}
}

