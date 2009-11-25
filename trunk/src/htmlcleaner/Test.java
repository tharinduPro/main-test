package htmlcleaner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Arrays;

public class Test {
	public static void main( String[] args ) {
		try {
            //创建一个进程示例
            ProcessBuilder pb = new ProcessBuilder("cmd.exe");
            //获取系统参数并打印显示
//            Map<String, String> env = pb.environment();
//            Iterator it=env.keySet().iterator();
//            while(it.hasNext()) {
//            	String attribute = (String)it.next();
//                System.out.println("System Attribute:"+(attribute)+"="+env.get(attribute));
//            }
            //设置工作目录
            pb.directory(new File("E:\\TestWork\\Test\\src\\htmlcleaner"));
            Process p = pb.start();
            //将要执行的Windows命令写入
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            //'\r\n'是必须写入的     
//            bw.write("test.bat \r\n");
            String cmd = "html2xhtml.exe xydt.xml -o a.xml \r\n";
            bw.write(cmd);
            //flush()方法是必须调用的
            bw.flush();
            //将执行结果打印显示
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String line;

            System.out.printf("Output of running %s is:", Arrays.toString(args));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
}
