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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ocr.ImageFilter;
import ocr.OCR;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import util.Tools;

public class Test {
	public static void main(String[] args) throws Exception {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, Constants.BROWSER_TYPE);
        HttpProtocolParams.setUseExpectContinue(params, true);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
		HttpContext localContext = new BasicHttpContext();

		HttpGet httpget = new HttpGet("http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.7034161835174741");
		httpget.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget, localContext);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		BMP bmp = new BMP(is);
		httpget.abort();
		
		//显示header
        entity = response.getEntity();
        for( Header h: response.getAllHeaders() ) {
        	System.out.println( h );
        }


		//反相显示图片
		Image image = bmp.getBMPImage();
		BufferedImage invertImage = ImageIOHelper.invertImage(image);
		ImageFilter imageFilter = new ImageFilter(invertImage);
		BufferedImage bi1 = imageFilter.scaling(5.0f);
		ShowImage si1 = new ShowImage( bi1 );
		si1.showImage();
		
		ImageIOHelper.storeImageToTiff( bi1, ImageConstants.BMP_OUTPUT_FILE );
		//识别图片
		OCR ocr = new OCR();
		String result = ocr.recognizeTiffToText( new File( ImageConstants.BMP_OUTPUT_FILE) );
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher( result );
		if( m.find() ) {
			//加入Accept-Encoding
	        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
	            
            public void process(
                    final HttpRequest request, 
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip,deflate");
                }
                request.addHeader( "Keep-Alive", "300" );
                request.setHeader( "Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7" );
                request.removeHeaders( "Cookie2" );
                String sessionId = "";
                for( Header h: request.getAllHeaders() ) {
                		if( h.getValue().indexOf( "ASPSESSIONIDSQQBATSD" ) > -1 ) {
                    		sessionId = h.getValue();
                    		break;
                		}
                }
                request.setHeader("cookie", "Lle_visitedfid=82; Lle_sid=x821RU; smile=1D1; " + sessionId );
                for( Header h: request.getAllHeaders() ) {
                	System.out.println( h );
                }
            }

	        });
	        
			result = m.group();
			HttpGet vote = new HttpGet("http://vote.sun0769.com/subject/2009/youthnet/action.asp?ItemID=552&SurveyCode=" + result);
		    HttpResponse rs = httpclient.execute(vote,localContext);
		    InputStream inputStream = rs.getEntity().getContent();
			//显示cookies
	        System.out.println("Initial set of cookies:");
	        List<Cookie> cookiesAfter = httpclient.getCookieStore().getCookies();
	        if (cookiesAfter.isEmpty()) {
	            System.out.println("None");
	        } else {
	            for (int i = 0; i < cookiesAfter.size(); i++) {
	                System.out.println("- " + cookiesAfter.get(i).toString());
	            }
	        }
		    System.out.println( Tools.InputStreamToString(inputStream) );
			System.out.println( "result:" + result );
		}
		httpclient.getConnectionManager().shutdown();

	} 
	
	

}
