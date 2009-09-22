package htmlunit;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Attendance {
	public static void main(String[] args) throws Exception {
	    final WebClient webClient = new WebClient( BrowserVersion.INTERNET_EXPLORER_6 );
	    //webClient.setRefreshHandler(new WaitingRefreshHandler()); 
	    final HtmlPage page = webClient.getPage("http://10.1.1.251");
	    final HtmlForm form = page.getFormByName("form1");
	    
	    final HtmlSubmitInput submit = form.getInputByName("Submit");
	    final HtmlTextInput userNameField = form.getInputByName("name");

	    // Change the value of the text field
	    userNameField.setValueAttribute( "fangdj" );

	    final HtmlPasswordInput passwordField = form.getInputByName("passwd");
	    passwordField.setValueAttribute( "1111" );
	    
	    submit.click();
	    
		final HtmlPage mainPage = webClient.getPage( "http://10.1.1.251/ONWork/details.asp?EMP_ID=fangdj" );
	    final HtmlForm mainForm = mainPage.getFormByName("sform");
		final HtmlTextInput mainUserNameField = mainForm.getInputByName("EMP_NO");
	    mainUserNameField.setValueAttribute( "13028" );
	    
	    final HtmlPasswordInput mainPasswordField = mainForm.getInputByName("pwd");
	    mainPasswordField.setValueAttribute( "1111" );

	    DomNodeList<HtmlElement> domNodeList = mainForm.getElementsByTagName( "a" );
	    domNodeList.get( 0 ).click();

	} 
	
	
}
