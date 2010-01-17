package XSLT.xqj;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.sf.saxon.xqj.SaxonXQDataSource;

public class HelloWorld  {
	public static void main( String args[] )throws XQException {
		XQDataSource ds = new SaxonXQDataSource();
		XQConnection conn = ds.getConnection();
		String book = "E:/WorkPlace/dg-credit/WebRoot/media/xslt/book.xml";
		XQPreparedExpression exp = conn.prepareExpression("doc(\"" + book + "\")/inventory/book/title");
		XQResultSequence result = exp.executeQuery();
		while( result.next() ) {
			System.out.println( result.getItemAsString( null ) );
		}
	}
}
