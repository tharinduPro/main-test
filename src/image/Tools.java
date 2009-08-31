package image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.jhlabs.image.InvertFilter;

public class Tools {
	public static BufferedImage invertImage( Image image ) {
		BufferedImage invert = null;
    	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
    			image.getHeight(null), BufferedImage.TYPE_INT_RGB);
    	Graphics2D g2 = bufferedImage.createGraphics();
    	g2.drawImage(image, null, null);
		int w = bufferedImage.getWidth();
		int h = bufferedImage.getHeight();
		invert = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		InvertFilter ifilter = new InvertFilter();
		ifilter.filter(bufferedImage, invert);
		return invert;
	}
}
