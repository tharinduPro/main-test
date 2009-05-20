package net.dgie.xmltask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "gb2312");
        transformer.transform(new StreamSource(sourceXml), streamResult);
        String temp = resultByte.toString();
        return temp.substring(temp.indexOf(">") + 1);
    }

     public void saveRssToLocale(String urlLink, File filename) throws IOException {

        URL url = new URL(urlLink);
        URLConnection urlConn = url.openConnection();
        InputStream is = urlConn.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        OutputStream os = new FileOutputStream(filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    public void transformToHtml(String fileName, String xml, String xslt) throws Exception {
        File file = new File(fileName);
        file.createNewFile();//创建文件
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        sb.append("<title></title></head><body>");
        sb.append("<div align='center'>");
        sb.append(transform(xml, xslt));
        sb.append("</div>");
        sb.append("</body></html>");
        PrintStream printStream = new PrintStream(new FileOutputStream(file), true, "UTF-8");//用UTF-8进行编码解决乱码问题
        printStream.println(sb.toString());//将字符串写入文件
        printStream.close();//写入完成关闭
        file = null;
    }
}
