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

     //public void saveRssToLocale(String urlLink, String filename) throws IOException {

        //URL url = new URL(urlLink);
        //URLConnection urlConn = url.openConnection();
        //InputStream is = urlConn.getInputStream();
        //// 1K的数据缓冲
        //byte[] bs = new byte[1024];
        //// 读取到的数据长度
        //int len;
        //// 输出的文件流
        //OutputStream os = new FileOutputStream(filename);
        //// 开始读取
        //while ((len = is.read(bs)) != -1) {
            //os.write(bs, 0, len);
        //}
        //// 完毕，关闭所有链接
        //os.close();
        //is.close();
    //}

}
