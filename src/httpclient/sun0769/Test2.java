package httpclient.sun0769;

import image.ImageConstants;
import image.ImageIOHelper;
import image.ShowImage;
import image.bmp.BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ocr.ImageFilter;
import ocr.OCR;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import util.Tools;

public class Test2 {
	public static void main(String[] args) throws Exception {
		WebClient webClient = new WebClient();
		DefaultHttpClient httpclient = webClient.getHttpClient();

		String url = "http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.7034161835174741";
		String result = getImageReslut( webClient.doGet(url, null) );
		if( result.length() > 0 ) {
	        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
	            public void process( final HttpRequest request, final HttpContext context) throws HttpException, IOException {
	                String sessionId = "";
	                for( Header h: request.getAllHeaders() ) {
	                		if( h.getValue().indexOf( "ASPSESSIONID" ) > -1 ) {
	                    		sessionId = h.getValue();
	                    		break;
	                		}
	                }
	                request.setHeader("Cookie", "Lle_visitedfid=82; smile=1D1; " + sessionId );
	            }
	        });
	        
	        //发送投票请求
			String urlVote = "http://vote.sun0769.com/subject/2009/youthnet/action.asp";
	        NameValuePair[] nvps = new NameValuePair[4];
	        nvps[0] = new BasicNameValuePair("SurveyCode", "bbb");
	        nvps[1] = new BasicNameValuePair("ItemID", "640");
	        nvps[2] = new BasicNameValuePair("sessionId", "youthnet");
	        nvps[3] = new BasicNameValuePair("moduleId", "");
		    InputStream inputStream = webClient.doPost(urlVote, nvps, null);

	        //显示返回的网页
		    System.out.println( Tools.InputStreamToString(inputStream) );
			System.out.println( "result:" + result );
		}
		httpclient.getConnectionManager().shutdown();

	} 
	
	private static String getImageReslut( InputStream is )throws Exception {
		BMP bmp = new BMP(is);

		//反相显示图片
		Image image = bmp.getBMPImage();
		BufferedImage invertImage = ImageIOHelper.invertImage(image);
		ImageFilter imageFilter = new ImageFilter(invertImage);
		BufferedImage bi1 = imageFilter.scaling(5.0f);
		ShowImage si1 = new ShowImage( bi1 );
		si1.showImage();
		
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
