package httpclient.company;

import httpclient.sun0769.WebClient;

import java.io.InputStream;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import util.Tools;

public class Attendance {
	public static void main(String[] args) throws Exception {
		WebClient webClient = new WebClient();
		DefaultHttpClient httpclient = webClient.getHttpClient();

		String loginURL = "http://10.1.1.251/login.asp";
		webClient.doGet(loginURL, null);
        NameValuePair[] loginNvps = new NameValuePair[3];
        loginNvps[0] = new BasicNameValuePair("name", "fangdj");
        loginNvps[1] = new BasicNameValuePair("passwd", "1111");
        loginNvps[2] = new BasicNameValuePair("Submit", "提交");

		webClient.doPost(loginURL,loginNvps, null);
		
		String mainURL = "http://10.1.1.251/login.asp";
		InputStream mainInputStream = webClient.doGet( mainURL, null );
	    System.out.println( Tools.InputStreamToString(mainInputStream) );
//	    try{ 
//	        //发送投票请求
//			String urlVote = "http://vote.sun0769.com/subject/2009/youthnet/action.asp";
//	        NameValuePair[] nvps = new NameValuePair[4];
//	        nvps[0] = new BasicNameValuePair("SurveyCode", "bbb");
//	        nvps[1] = new BasicNameValuePair("ItemID", "640");
//	        nvps[2] = new BasicNameValuePair("sessionId", "youthnet");
//	        nvps[3] = new BasicNameValuePair("moduleId", "");
//		    InputStream inputStream = webClient.doPost(urlVote, nvps, null);
//
//	        //显示返回的网页
//		    System.out.println( Tools.InputStreamToString(inputStream) );
//			System.out.println( "result:" );
//		}
//	    catch( Exception ex ) {
//	    	ex.printStackTrace();
//	    }
		httpclient.getConnectionManager().shutdown();

	} 
	
	
}
