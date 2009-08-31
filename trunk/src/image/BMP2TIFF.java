package image;

import image.bmp.BMP;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class BMP2TIFF {
	public static void main(String[] args){
		try {
			URL url = new URL( "http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.07248511577958028");
			Image image = new BMP( url.openStream() ).getBMPImage();
			storeImageToTiff( Tools.invertImage(image), ImageConstants.BMP_OUTPUT_FILE );
		}
		catch( IOException ioe ) {
			ioe.printStackTrace();
		}
	}
	
       
    public static void storeImageToTiff(Image image, String filePath) {
    	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
    			image.getHeight(null), BufferedImage.TYPE_INT_RGB);
    	Graphics2D g2 = bufferedImage.createGraphics();
    	g2.drawImage(image, null, null);
    	try {
			OutputStream outputStreamFile = new FileOutputStream(filePath);
			TIFFEncodeParam param = new TIFFEncodeParam();
			ImageEncoder imageEncoder = ImageCodec.createImageEncoder("tiff", outputStreamFile, param);
			imageEncoder.encode(bufferedImage);
			outputStreamFile.close();
    	}
    	catch( IOException ioe ) {
    		ioe.printStackTrace();
    	}
    }
}
