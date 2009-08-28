package image.simpledecoder;

import image.simpledecoder.pub.filter.Filter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

//识别类

public class Recognize {
	static String url = "http://passport.csdn.net/member/ShowExPwd.aspx";
	static Filter filter;

	public static void main(String[] args) throws Exception {
		if (args.length >= 1) {
			url = args[0];
		}

		String filterClazz = "image.simpledecoder.filter.NotWhiteFilter";
		if (args.length >= 2) {
			filterClazz = args[1];
		}

		filter = (Filter) Class.forName(filterClazz).newInstance();
		int total = 10;
		int count = 0;
		for (int i = 0; i < total; i++) {
			boolean b = recognize(i);
			if (b)
				count++;
		}
		System.out.println("rate:" + (count * 1.0 / total * 100) + "%100");
	}

	private static boolean recognize(int num) throws IOException {
		BufferedImage bi = ImageIO.read(new URL(url));
		ImageIO.write(bi, "png", new File(num + ".png"));
		ImageData ia2 = new ImageData(bi, filter);
		ImageData[] ii = ia2.split();
		ArrayList list = new ArrayList();
		ImageData[] template = ImageData.decodeFromFile("c:/template.data");
		HashMap map = new HashMap();
		for (int i = 0; i < template.length; i++) {
			map.put(template[i], new Character(template[i].code));
		}
		for (int x = 0; x < ii.length; x++) {
			ImageData imageArr = ii[x];
			if (imageArr.getWidth() > 15)
				continue;
			Character c = (Character) map.get(imageArr);
			if (c != null) {
				list.add(c);
			}
		}
		String s = "";
		System.out.print(num + ":");
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Character c = (Character) iter.next();
			s += c;
			System.out.print(c);
		}
		System.out.println();
		return s.length() != 0;
	}
}