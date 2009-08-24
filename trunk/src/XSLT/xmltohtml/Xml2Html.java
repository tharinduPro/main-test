package XSLT.xmltohtml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Xml2Html {
	public static void main(String[] args) throws TransformerException,
			TransformerConfigurationException, FileNotFoundException,
			IOException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(
				"stocks.xsl"));
		transformer.transform(new StreamSource(args[0]), new StreamResult(
				new FileOutputStream(args[1])));
		System.out.println("** The output is written in " + args[1] + " **");
	}
}
