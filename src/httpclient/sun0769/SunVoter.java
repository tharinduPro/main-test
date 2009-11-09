package httpclient.sun0769;

import image.ImageConstants;
import image.ImageIOHelper;
import image.ShowImage;
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
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import util.Tools;

public class SunVoter {
	public static void main(String[] args) throws Exception {
		int successCounter = 0;
		int sleepCounter = 0;
		for( int voteIndex = 0; voteIndex < 3000; voteIndex++ ) {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			//HttpHost proxy = new HttpHost("59.36.98.154", 80);
			//httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			
			httpclient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY );
			
			HttpGet httpget = new HttpGet("http://vote.sun0769.com/include/code.asp?s=ncflash&aj=0.569978840983635");
			String result = getImageReslut(httpget, httpclient );
			httpget.abort();
	
		        
			//发送投票请求
			Thread.sleep( 4000 );
			HttpPost vote = new HttpPost("http://vote.sun0769.com/subject/2009/ncflash/action.asp");
	        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("SurveyCode", result));
	        nvps.add(new BasicNameValuePair("ItemID", "243"));
	        nvps.add(new BasicNameValuePair("sessionId", "ncflash"));
	        nvps.add(new BasicNameValuePair("moduleId", ""));
	        vote.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse rs = httpclient.execute(vote);
		    InputStream inputStream = rs.getEntity().getContent();
		    String resultPage = Tools.InputStreamToString(inputStream);
		    if( resultPage.indexOf( "未能成功" ) > -1 ) {
		    	System.out.println( "failed!" );
		    }else if( resultPage.indexOf( "成功" ) > -1 ) {
		    	successCounter ++;
		    	System.out.println( "success:" +  successCounter);
		    }
		    if( resultPage.indexOf( "重复" ) > -1 ) {
		    	sleepCounter ++;
		    	System.out.println( "睡眠30分钟第" + sleepCounter + "次" );
		    	Thread.sleep( 1800000 );
		    }
			httpclient.getConnectionManager().shutdown();
		}
	} 
	

	private static String getImageReslut( HttpGet httpget, DefaultHttpClient httpclient ) throws Exception{
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		BMP bmp = new BMP(is);

		//反相显示图片
		Image image = bmp.getBMPImage();
		BufferedImage invertImage = ImageIOHelper.invertImage(image);
		ImageFilter imageFilter = new ImageFilter(invertImage);
		BufferedImage bi1 = imageFilter.scaling(5.0f);
		
		
		//保存图片以供识别
		ImageIOHelper.storeImageToTiff( bi1, ImageConstants.BMP_OUTPUT_FILE );
		
		//识别图片
		OCR ocr = new OCR();
		String result = ocr.recognizeTiffToText( new File( ImageConstants.BMP_OUTPUT_FILE) );
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher( result );
		if( m.find() ){
			return m.group();
		}
		else {
			return "";
		}
	}
}
