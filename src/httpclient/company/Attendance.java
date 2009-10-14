package httpclient.company;

import image.ImageConstants;
import image.ImageIOHelper;
import image.bmp.BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ocr.ImageFilter;
import ocr.OCR;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import util.Tools;

public class Attendance {
	public static void main(String[] args) throws Exception {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY );

			HttpPost dutyLogin = new HttpPost("http://10.1.1.251/login.asp");
	    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	    nvps.add(new BasicNameValuePair("name", "fangdj"));
	    nvps.add(new BasicNameValuePair("passwd", "1111"));
			nvps.add(new BasicNameValuePair("Submit", "登  录"));
	    dutyLogin.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpclient.execute(dutyLogin);
			dutyLogin.abort();
			
			HttpPost duty = new HttpPost("http://10.1.1.251/ONWork/Record_save.asp");
			List <NameValuePair> dutyNvps = new ArrayList <NameValuePair>();
	    dutyNvps.add(new BasicNameValuePair("EMP_NO", "13028"));
	    dutyNvps.add(new BasicNameValuePair("pwd", "1111"));
			dutyNvps.add(new BasicNameValuePair("cmd2", "Apply"));
			duty.setEntity(new UrlEncodedFormEntity(dutyNvps, HTTP.UTF_8));
			HttpResponse rs = httpclient.execute(duty);
			InputStream inputStream = rs.getEntity().getContent();
		  String resultPage = Tools.InputStreamToString(inputStream);
		  System.out.println( resultPage );
		  
			httpclient.getConnectionManager().shutdown();
	} 
}
