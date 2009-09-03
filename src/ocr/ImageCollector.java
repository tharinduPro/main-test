package ocr;

import image.ImageConstants;
import image.ImageIOHelper;
import image.bmp.BMP;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ImageCollector {
	public static void main(String[] args) throws Exception {
		URL url = new URL( "http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.07248511577958028");
		Image image = new BMP( url.openStream() ).getBMPImage();
		BufferedImage sampleImage = ImageIOHelper.changeImageToBufferedImage(image);
		int width = sampleImage.getWidth();
		int height = sampleImage.getHeight();
		BufferedImage resultBufferedImage = new BufferedImage(width*10, height*10, BufferedImage.TYPE_INT_ARGB);
		Graphics g = resultBufferedImage.getGraphics();
		//把25张图片合成一张
		for( int i=0; i<100; i++ ) {
			Image captchaImage = new BMP( url.openStream() ).getBMPImage();
			BufferedImage invertImage = ImageIOHelper.invertImage(captchaImage);
			g.drawImage(invertImage, (i%10)*width, (i/10)*height, width, height, null);
		}
		g.dispose();
		ImageIOHelper.storeImageToTiff(resultBufferedImage, ImageConstants.ORC_IMAGE_FILE_PATH + "/scan.tif");
	}

}
