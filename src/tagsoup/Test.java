package tagsoup;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


public class Test {
	public static void main( String args[] ) throws Exception {
		// Parse the HTML file using the tagsoup parser
		SAXBuilder builder = new SAXBuilder("org.ccil.cowan.tagsoup.Parser"); 
		Document doc = builder.build( "http://www.dgaic.gov.cn/item.jsp?id=25000061&name=%CA%D8%BA%CF%CD%AC%D6%D8%D0%C5%D3%C3%C6%F3%D2%B5&type=1" );
		
		File f = new File("E:\\TestWork\\Test\\src\\tagsoup\\gongCompany.xml");
		OutputStream ops = new FileOutputStream( f );
		XMLOutputter op=new XMLOutputter();

		op.output( doc, ops);
		
	}
}
