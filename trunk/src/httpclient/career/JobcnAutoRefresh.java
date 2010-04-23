package httpclient.career;

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

public class JobcnAutoRefresh {
	public static void main(String[] args) {
		HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.7) Gecko/20091221 Firefox/3.5.7"); 
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		httpClient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		new JobcnAutoRefresh().refresh51jobResume( httpClient );
		httpClient.getConnectionManager().shutdown();
	}
	
	public void refresh51jobResume( DefaultHttpClient httpClient ) {
		try {
			String queryLoginUrl = "http://www.jobcn.com/default.jsp";
			HttpGet queryLogin  = new HttpGet( queryLoginUrl );
			httpClient.execute( queryLogin );
			queryLogin.abort();
			
			String loginUrl = "http://www.jobcn.com/loginForm";
			HttpPost login = new HttpPost( loginUrl );
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair( "userName", "fangdejia" ) );
			nvps.add(new BasicNameValuePair( "password", "linuxsky" ) );
			nvps.add(new BasicNameValuePair( "MemberType", "1" ) );
			login.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute( login );
			login.abort();
			
			String managerUrl = "http://www.jobcn.com/person/Per_ManagementCenter.jsp";
			HttpGet resumeManager = new HttpGet( managerUrl );
			httpClient.execute( resumeManager );
			resumeManager.abort();
			
			String refreshUrl = "http://www.jobcn.com/person/per_biz/per_contact.jsp";
			HttpPost refresh = new HttpPost( refreshUrl );
			List <NameValuePair> refreshNvps = new ArrayList <NameValuePair>();
			refreshNvps.add(new BasicNameValuePair( "f_task", "confirm" ) );
			refreshNvps.add(new BasicNameValuePair( "key_id", "4056745" ) );
			refreshNvps.add(new BasicNameValuePair( "pername", "方德佳" ) );
			refreshNvps.add(new BasicNameValuePair( "relationphone", "85833401" ) );
			refreshNvps.add(new BasicNameValuePair( "mobilenum", "15999863410" ) );
			refreshNvps.add(new BasicNameValuePair( "bpnumber", "" ) );
			refreshNvps.add(new BasicNameValuePair( "email", "s-k-y@126.com" ) );
			refreshNvps.add(new BasicNameValuePair( "imnum", "228877644" ) );
			refreshNvps.add(new BasicNameValuePair( "address", "东莞市厚街镇寮厦新村十三巷4号" ) );
			refreshNvps.add(new BasicNameValuePair( "zipcode", "523000" ) );
			refresh.setEntity(new UrlEncodedFormEntity(refreshNvps,"GB2312" ));
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
