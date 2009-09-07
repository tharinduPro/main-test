package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tools {

	/**
	 * 把InputStream转换成String输出
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamToString( InputStream is ) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "gb2312"));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		return sb.toString();
	}
}
