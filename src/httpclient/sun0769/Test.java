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
import java.util.ArrayList;
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
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.UrlEncodedFormEntity;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.PlainSocketFactory;
import org.apache.http.conn.Scheme;
import org.apache.http.conn.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import util.Tools;

public class Test {
	public static void main(String[] args) throws Exception {
		DefaultHttpClient httpclient = getMultiThreadedHttpClient();
		HttpContext localContext = new BasicHttpContext();

		HttpGet httpget = new HttpGet("http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.7034161835174741");
		String result = getImageReslut(httpget, httpclient, localContext );
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
			HttpPost vote = new HttpPost("http://vote.sun0769.com/subject/2009/youthnet/action.asp");
	        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("SurveyCode", "bbb"));
	        nvps.add(new BasicNameValuePair("ItemID", "640"));
	        nvps.add(new BasicNameValuePair("sessionId", "youthnet"));
	        nvps.add(new BasicNameValuePair("moduleId", ""));
	        vote.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
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
	
	private static DefaultHttpClient getMultiThreadedHttpClient() {
        // Create and initialize HTTP parameters
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpProtocolParams.setUserAgent(params, Constants.BROWSER_TYPE);
        HttpProtocolParams.setUseExpectContinue(params, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        
        // Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        DefaultHttpClient httpclient = new DefaultHttpClient(cm, params);
        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(
                    final HttpRequest request, 
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip,deflate");
                }
                request.addHeader( "Keep-Alive", "300" );
                request.addHeader( "Accept-Language", "zh-cn,zh;q=0.5" );
                request.setHeader( "Connection", "keep-alive" );
                request.setHeader( "Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7" );
                request.setHeader( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
            }
        });
        return httpclient;
	}

	private static String getImageReslut( HttpGet httpget, DefaultHttpClient httpclient, HttpContext localContext  ) throws Exception{
		httpget.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget, localContext);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
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
