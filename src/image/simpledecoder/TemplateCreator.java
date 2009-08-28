package image.simpledecoder;

import image.simpledecoder.pub.filter.Filter;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

//模板创建类
class TemplateCreator {
	public static void main(String[] args) throws Exception {
		Set<ImageData> imageDataSet = getImageDataSet();
		System.out.println(imageDataSet.size());
		for (Iterator<ImageData> iter = imageDataSet.iterator(); iter.hasNext();) {
			ImageData ele = iter.next();
			ele.show();
			System.out.print("char:");
			String s = readLine();
			if (s.length() == 1) {
				ele.code = s.charAt(0);
			}
		}
		PrintWriter pw = new PrintWriter(new File("c:/template.data"));
		for (Iterator iter = imageDataSet.iterator(); iter.hasNext();) {
			ImageData ele = (ImageData) iter.next();
			pw.println(ele.encode());
		}
		pw.flush();
		pw.close();
	}
	
	private static Set<ImageData> getImageDataSet() throws Exception{
		URL url = new URL( "http://passport.csdn.net/member/ShowEXPwd.ASPx" );
		String filterClazz = "image.simpledecoder.filter.NotWhiteFilter";
		Filter noWhiteFilter = (Filter) Class.forName(filterClazz).newInstance();
		Set<ImageData> imageDataSet = new HashSet<ImageData>();
		for (int i = 1; i < 10; i++) {
			BufferedImage bufferedImage = ImageIO.read(url);
			ImageData imageData = new ImageData(bufferedImage, noWhiteFilter);
			ImageData[] imageDataArray = imageData.split();
			for (int x = 0; x < imageDataArray.length; x++) {
				ImageData imageArr = imageDataArray[x];
				imageDataSet.add(imageArr);
			}
		}
		return imageDataSet;
	}

	private static BufferedReader reader = 
		new BufferedReader(new InputStreamReader(System.in));

	private static String readLine() {
		try {
			return reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}