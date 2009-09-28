package htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Blogbus {
	public static void main( String[] args ) throws Exception {
		    final WebClient webClient = new WebClient( BrowserVersion.INTERNET_EXPLORER_7 );

		    String voteURL = "http://www.blogbus.com/";
		    final HtmlPage loginPage = (HtmlPage)webClient.getPage(voteURL);
		    final HtmlForm form = loginPage.getFormByName("frmLogin");

		    final HtmlTextInput userNameField = form.getInputByName("username");
		    userNameField.setValueAttribute( "fangdejia" );
		    
		    final HtmlPasswordInput passwordField = form.getInputByName("password");
		    passwordField.setValueAttribute( "linuxsky" );
		    
		    final HtmlButtonInput button = form.getInputByName("button");
		    button.click();
//		    System.out.println( pageResult.getWebResponse().getContentAsString() );
	}

}
