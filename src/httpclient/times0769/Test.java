package httpclient.times0769;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import util.Tools;

public class Test {

	public static void main(String[] args) throws Exception {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.7) Gecko/20091221 Firefox/3.5.7"); 
		DefaultHttpClient httpclient = new DefaultHttpClient(params);
		httpclient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY );
		
		HttpGet httpget = new HttpGet("http://www.times0769.com/index.php?action-model-name-coll-itemid-303");
		httpclient.execute(httpget);
		httpget.abort();
		
		HttpPost times0769Reg = new HttpPost("http://www.times0769.com/do.php?action=register&inajax=1");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		Integer userNameLength = Tools.createRandom( 7, 12 );
		String userName = Tools.createRandomWord( userNameLength );
		nvps.add(new BasicNameValuePair("username", userName ));
		
		Integer passwdLength = Tools.createRandom( 6, 10);
		String password = Tools.createRandomPasswd( passwdLength );
		nvps.add(new BasicNameValuePair("password", password ));
		nvps.add(new BasicNameValuePair("password2", password ));
		
		Integer emailIndex = Tools.createRandom( 0, 3 );
		String[] emailArray = { "qq.com", "126.com", "gmail.com", "tom.com" };
		nvps.add(new BasicNameValuePair("email", userName + "@" + emailArray[ emailIndex ] ));
		
		nvps.add(new BasicNameValuePair( "formhash", "2a31600c" ) );
		nvps.add(new BasicNameValuePair("refer", "http://www.times0769.com" ) );
		nvps.add(new BasicNameValuePair("registersubmit", "注册" ) );
		
		times0769Reg.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse rs	= httpclient.execute(times0769Reg);
		InputStream is = rs.getEntity().getContent();
		System.out.println( Tools.InputStreamToString( is , "utf-8" ));
		times0769Reg.abort();
		
		HttpGet httpvote = new HttpGet("http://www.times0769.com/do.php?action=click&op=add&clickid=35&id=303&hash=274c6d5b7de2eb5e04af9b8d1f966d4a");
		HttpResponse rsVote	= httpclient.execute( httpvote );
		InputStream isVote = rsVote.getEntity().getContent();
		System.out.println( Tools.InputStreamToString( isVote , "utf-8" ));
		httpvote.abort();
		
		httpclient.getConnectionManager().shutdown();
	}

}
