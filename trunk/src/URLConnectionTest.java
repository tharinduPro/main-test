import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionTest {
    public static void main( String args[] ) throws Exception {
        URL urlmy = new URL( "http://www.times0769.com/do.php?action=click&op=add&clickid=35&id=38&hash=6a011a99d61a19020fa4a445f40ffeae" );
        //URL urlmy = new URL( "http://www.baidu.com" );

        URLConnection con =  urlmy.openConnection();

        con.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));

        String s = "";

        StringBuffer sb = new StringBuffer("");

        while ((s = br.readLine()) != null) {

            sb.append(s+"\r\n");
        }

        System.out.println( sb.toString() + "test"  );

    }
}

