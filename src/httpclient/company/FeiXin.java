package httpclient.company;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FeiXin {
	public static void main(String args[]) throws Exception {
		sendMessage( "15999863410", "tweta1" );
	}
	
	private static boolean sendMessage( String phoneNumber, String content ) {
		URL url;
		HttpURLConnection connection;
		boolean result = false;
		try {
			url = new URL("http://sms.api.bz/fetion.php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
	
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write("username=15999863410&password=1234abcd&sendto=" + phoneNumber +"&message="+content);
			writer.close();
	
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				result = true;
			} else {
				result = false;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
