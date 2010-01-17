package net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class Sendmessage {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String requestUrl="http://19.104.8.6/mspf/servlet/SpInterface";
		String requestData = "<?xml version=\"1.0\" encoding=\"GBK\" ?>" +
				"<REQUEST><TRANS_TYPE>SMS_DOWN_REQUEST</TRANS_TYPE>" +
				"<SP_ID>22</SP_ID>" +
				"<PASSWORD>22</PASSWORD>" +				
				"<SEQ_NUM>00100003</SEQ_NUM><MOBILE>13240677386</MOBILE>" +
				"<CONTENT><![CDATA[下行短信内容：你好]]></CONTENT>" +
				"<DATETIME>2008-04-14 15:24:31</DATETIME>" +
				"<PRIORITY>2</PRIORITY><EXTEND_CODE>999</EXTEND_CODE></REQUEST>";	
		
		String respData = "";	
		
		HttpURLConnection httprequestUrlconnection = null;
		try {
			boolean sendOk = false;
			int resendCount = 0;
			int result = 0;	
			URL url = null;
			while (!sendOk && resendCount < 3) {
				url = new URL(requestUrl);
				httprequestUrlconnection = (HttpURLConnection) url.openConnection();
				httprequestUrlconnection.setDoOutput(true);
				httprequestUrlconnection.setRequestMethod("POST");				
				httprequestUrlconnection.getOutputStream().write(new String(requestData).getBytes());
				httprequestUrlconnection.getOutputStream().flush();
				httprequestUrlconnection.getOutputStream().close();
				result = httprequestUrlconnection.getResponseCode();				
				if (result == 200) {
					StringBuffer respStr = new StringBuffer("");
					InputStream is = httprequestUrlconnection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					String currentLine;
					while ((currentLine = reader.readLine()) != null) {
						if (currentLine.length() > 0) {
							respStr.append(currentLine.trim());
						}							
					}
					// 处理响应结果											
					sendOk = true;
					respData = respStr.toString();
				} else {
					System.out.println("远程服务器连接失败,错误代码: " + result);
					resendCount++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("向远程服务器提交请求数据失败: " + e.getMessage());
		} finally {
			if (httprequestUrlconnection != null)
				httprequestUrlconnection.disconnect();
		}
		System.out.println(respData);
	}
	

}
