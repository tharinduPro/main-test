package htmlunit;

import image.ImageConstants;
import image.ImageIOHelper;
import image.bmp.BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ocr.ImageFilter;
import ocr.OCR;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Test {
	public static void main( String[] args ) throws Exception {
		    final WebClient webClient = new WebClient( BrowserVersion.INTERNET_EXPLORER_7 );

		    String voteURL = "http://vote.sun0769.com/subject/2009/youthnet/action.asp?ItemID=775";
		    final HtmlPage votePage = (HtmlPage)webClient.getPage(voteURL);
		    HtmlImage imgElement = (HtmlImage)votePage.getElementById( "getCode" );
		    final Page imgPage =
		    	webClient.getPage( votePage.getFullyQualifiedUrl( imgElement.getSrcAttribute() ) );
		    String result = getImageReslut( imgPage.getWebResponse().getContentAsStream() );
		    //System.out.println( result );
		    
		    final HtmlForm form = votePage.getFormByName("codechk");

		    final HtmlTextInput textField = form.getInputByName("SurveyCode");
		    textField.setValueAttribute( result );
		    final HtmlSubmitInput button = form.getInputByName("btnSubmit");
		    webClient.waitForBackgroundJavaScript(5000);
		    button.click();
		    System.out.println( "finished" );
//		    System.out.println( pageResult.getWebResponse().getContentAsString() );
	}
	
	private static String getImageReslut( InputStream is  ) throws Exception{
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
}
