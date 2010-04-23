package httpclient.career;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import util.Tools;

public class AutoRefresh {
	public static void main(String[] args) {
		HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.7) Gecko/20091221 Firefox/3.5.7"); 
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		httpClient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		new AutoRefresh().refresh51jobResume( httpClient );
		httpClient.getConnectionManager().shutdown();
	}
	
	public void refresh51jobResume( DefaultHttpClient httpClient ) {
		try {
			String queryLoginUrl = "http://my.51job.com/my/My_SignIn.php";
			HttpGet queryLogin  = new HttpGet( queryLoginUrl );
			httpClient.execute( queryLogin );
			queryLogin.abort();
			
			String postUrl = "http://my.51job.com/my/My_Pmc.php";
			HttpPost login = new HttpPost( postUrl );
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair( "username", "fangdejia" ) );
			nvps.add(new BasicNameValuePair( "userpwd", "linuxsky" ) );
			nvps.add(new BasicNameValuePair( "url", "/my/My_Pmc.php" ) );
			nvps.add(new BasicNameValuePair( "x", "0" ) );
			nvps.add(new BasicNameValuePair( "y", "0" ) );
			login.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute( login );
			login.abort();
			
			HttpGet resumeManager = new HttpGet( postUrl );
			httpClient.execute( resumeManager );
			resumeManager.abort();
			
			String refreshUrl = "http://my.51job.com/cv/CResume/RefreshResume.php?isJSON=1&Read=0&isenglish=0&rand=0.6564442810733783&jsoncallback=jsonp1270776271270&_=1270776292773&ReSumeID=68348747&MPNation=0769&Mobile=15999863410&HPNation=086&HPCity=0769&HPNumber=85833401&FPNation=086&FPCity=%E5%8C%BA%E5%8F%B7&FPNumber=%E6%80%BB%E6%9C%BA%E5%8F%B7%E7%A0%81&FPExtension=%E5%88%86%E6%9C%BA&current_situation=0&EmailAdd=s-k-y%40126.com&isAlert=1";
			HttpGet refresh = new HttpGet( refreshUrl );
			HttpResponse hs = httpClient.execute( refresh );
			InputStream is = hs.getEntity().getContent();
			System.out.println( Tools.InputStreamToString( is ) );
			refresh.abort();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
