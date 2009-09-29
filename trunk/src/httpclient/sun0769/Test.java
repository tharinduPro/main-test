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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import util.Tools;

public class Test {
	public static void main(String[] args) throws Exception {
		for( int voteIndex = 0; voteIndex < 1; voteIndex++ ) {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY );
//			HttpHost proxy = new HttpHost( "163.29.128.103", 8080 );
//			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

			HttpContext localContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet("http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.70161835174741");
			String result = getImageReslut(httpget, httpclient, localContext );
			System.out.println( "====" + result );
			httpget.abort();
	
		        
			//发送投票请求
			Thread.sleep( 4000 );
			HttpPost vote = new HttpPost("http://vote.sun0769.com/subject/2009/youthnet/action.asp");
	        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("SurveyCode", result));
	        nvps.add(new BasicNameValuePair("ItemID", "1054"));
	        nvps.add(new BasicNameValuePair("sessionId", "youthnet"));
	        nvps.add(new BasicNameValuePair("moduleId", ""));
	        vote.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse rs = httpclient.execute(vote,localContext);
		    InputStream inputStream = rs.getEntity().getContent();
		    System.out.println( Tools.InputStreamToString(inputStream) );
			httpclient.getConnectionManager().shutdown();
		}
	} 
	

	private static String getImageReslut( HttpGet httpget, DefaultHttpClient httpclient, HttpContext localContext  ) throws Exception{
		HttpResponse response = httpclient.execute(httpget, localContext);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		BMP bmp = new BMP(is);

		//反相显示图片
		Image image = bmp.getBMPImage();
		BufferedImage invertImage = ImageIOHelper.invertImage(image);
		ImageFilter imageFilter = new ImageFilter(invertImage);
		BufferedImage bi1 = imageFilter.scaling(5.0f);
//		ShowImage si1 = new ShowImage( bi1 );
//		si1.showImage();
		
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
	
	private boolean successDecider( String  output) {
		if( output.indexOf( "成功" ) > -1 ) {
			return true;
		}
		else {
			return false;
		}
	}
}
