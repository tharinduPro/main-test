package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Tools {

	/**
	 * 把InputStream转换成String输出
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamToString( InputStream is ) throws Exception {
		return InputStreamToString( is, "gb2312" );
	}
	
    /**
     * 把InputStream转换成String输出
     * @param is
     * @return
     * @throws Exception
     */
    public static String InputStreamToString( InputStream is, String encoding ) throws Exception {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding ));
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

	/**
	 * 产生length小于或等于10的单词
	 */
    public static String createRandomWord( Integer length) {
        String consonants = "bcdfghjklmnpqrstvwxyz";
        String  vowels = "aeiou";
        String word = "";
        String[] consonantsArray = consonants.split("");
        String[] vowelsArray = vowels.split("");
        for (int i=0;i<length/2;i++ ) {
            String randConsonant = consonantsArray[new Random().nextInt(consonants.length())];
            String randVowel = vowelsArray[new Random().nextInt(vowels.length())];
            word += ( i==0 ) ? randConsonant.toUpperCase() : randConsonant;
            word += i*2<length-1 ? randVowel : "";
        }
        return word;
    }
    
    
	public static String createRandomPasswd(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '$', '#', '@' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}
	/**
	 * 
	 * @param min
	 * @param max
	 * @return 产生固定范围的随机数
	 */
	public static Integer createRandom( Integer min, Integer max ) {
		return min + new Random().nextInt( max - min + 1);
	}

	public static void runCmd( String cmd ) throws IOException {
		Process process = Runtime.getRuntime().exec("cmd /c " + cmd ); 	
		BufferedReader in = new BufferedReader(  
		new InputStreamReader( process.getInputStream() ) );  
		String line = null;  
		while ((line = in.readLine()) != null) {  
			System.out.println(line);  
		}
	}
	
}
