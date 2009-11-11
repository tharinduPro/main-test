package company;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import company.tools.WebPageCleaner;

public class Test {
	public static void main(String[] args)throws Exception{  
		 File xmlFile = new File( "E:\\Plan\\Attendance\\Users.xml" );
		 List<Staff> ls = new XMLParser().getStaffFromXml(xmlFile);
		 for( final Staff s: ls ) {
			 new Test().attendance(s);
		 }
	} 
	public void attendance( Staff staff ) throws Exception {
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter( ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY );
		
		HttpPost attendanceLogin = new HttpPost("http://10.1.1.251/login.asp");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("name", staff.getUserName()));
		nvps.add(new BasicNameValuePair("passwd", staff.getPassword()));
		nvps.add(new BasicNameValuePair("Submit", "��  ¼"));
		attendanceLogin.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		httpclient.execute(attendanceLogin);
		attendanceLogin.abort();
		
		HttpPost attendance = new HttpPost("http://10.1.1.251/ONWork/default.asp");
		HttpResponse rs = httpclient.execute(attendance);
		HttpEntity entity = rs.getEntity();
		InputStream inputStream = entity.getContent();
		InputStream cleanIs = new WebPageCleaner().clean(inputStream, "GB2312");
		
	    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    domFactory.setNamespaceAware(true); // never forget this!
	    DocumentBuilder builder = domFactory.newDocumentBuilder();
	    Document doc = builder.parse(cleanIs);
	    
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    XPathExpression expr 
	     = xpath.compile("//form/table/tbody/tr[2]/td/text()");


	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    String resultMessage = "";
	    for (int i = 1; i < nodes.getLength(); i++) {
	    	String r = nodes.item(i).getNodeValue();
	        resultMessage += r + " "; 
	    }
	    FeiXin.sendMessage( "15999863410", resultMessage);
		httpclient.getConnectionManager().shutdown();
	}
}
