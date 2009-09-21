package httpclient.company;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


public class Test {
	public static void main(String[] args) throws Exception {
	    final WebClient webClient = new WebClient( BrowserVersion.INTERNET_EXPLORER_6 );
	    webClient.setRefreshHandler(new WaitingRefreshHandler(1)); 
	    final HtmlPage page = webClient.getPage("http://10.1.1.251");
	    final HtmlForm form = page.getFormByName("form1");
	    
	    final HtmlSubmitInput submit = form.getInputByName("Submit");
	    final HtmlTextInput userNameField = form.getInputByName("name");

	    // Change the value of the text field
	    userNameField.setValueAttribute( "fangdj" );

	    final HtmlPasswordInput passwordField = form.getInputByName("passwd");
	    passwordField.setValueAttribute( "1111" );
	    
	    submit.click();
	    System.out.println( page.getTitleText());

	} 

}
