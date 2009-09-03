package httpclient.sun0769;

import image.ImageConstants;
import image.ImageIOHelper;
import image.ShowImage;
import image.bmp.BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ocr.ImageFilter;
import ocr.OCR;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Test {
	public static void main(String[] args) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.7034161835174741");
		HttpResponse response = httpclient.execute(httpget);       
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		BMP bmp = new BMP(is);
        if (entity != null) {
            entity.consumeContent();
        }
		Image image = bmp.getBMPImage();
		BufferedImage invertImage = ImageIOHelper.invertImage(image);
		ImageFilter imageFilter1 = new ImageFilter(invertImage);
		BufferedImage bi1 = imageFilter1.scaling(5.0f);
		ShowImage si1 = new ShowImage( bi1 );
		si1.showImage();
		
		ImageIOHelper.storeImageToTiff( bi1, ImageConstants.BMP_OUTPUT_FILE );
		
		OCR ocr = new OCR();
		String result = ocr.recognizeTiffToText( new File( ImageConstants.BMP_OUTPUT_FILE) );
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher( result );
		if( m.find() ) {
			result = m.group();
			System.out.println( "result:" + result );
			HttpGet vote = new HttpGet("http://vote.sun0769.com/subject/2009/youthnet/action.asp?ItemID=552&SurveyCode=" + result);
		    HttpResponse rs = httpclient.execute(vote);
		    InputStream inputStream = rs.getEntity().getContent();
		    System.out.println( InputStreamToString(inputStream) );
		}
		httpclient.getConnectionManager().shutdown();

	} 
	
	public static String InputStreamToString( InputStream is ) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "gb2312"));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		return sb.toString();
	}

}
