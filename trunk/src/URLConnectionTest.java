import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnectionTest {
    public static void main( String args[] ) throws Exception {
        URL urlmy = new URL( "http://19.16.8.123/index/index/transform.asp?default=9&threeblockcode=001801&blockcode=YBQAA" );
        //URL urlmy = new URL( "http://www.baidu.com" );

        HttpURLConnection con = (HttpURLConnection) urlmy.openConnection();

        con.setFollowRedirects(true);

        con.setInstanceFollowRedirects(false);

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

