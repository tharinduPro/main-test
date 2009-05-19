package net.dgie.xmltask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;

public class XMLToHTML {

    public String transform(String xml, String xslt) throws Exception {
        FileInputStream sourceXml = new FileInputStream(new File(xml));
        ByteArrayOutputStream resultByte = new ByteArrayOutputStream();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(new File(xslt)));
        StreamResult streamResult = new StreamResult(resultByte);
        transformer.transform(new StreamSource(sourceXml), streamResult);
        System.out.println(resultByte.toString());
        return resultByte.toString();
    }
}
