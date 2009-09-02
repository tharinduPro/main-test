package ocr;

import image.ImageConstants;
import image.ImageIOHelper;
import image.bmp.BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageCollector {
	public static void main(String[] args) throws Exception {
		URL url = new URL( "http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.07248511577958028");
		Iterator<ImageWriter>  writers=ImageIO.getImageWritersByFormatName("tiff");  
		ImageWriter  writer = writers.next();   
		ImageOutputStream out = ImageIO.createImageOutputStream(new File( ImageConstants.ORC_IMAGE_FILE_PATH + "/scan.tif" ) );  
		writer.setOutput( out );
		for( int i = 0; i< 20; i++ ){
			Image image = new BMP( url.openStream() ).getBMPImage();
			BufferedImage invertImage = ImageIOHelper.invertImage(image);
			IIOImage   iio = new IIOImage(invertImage,null,null); 
			if( i ==0 ) {
				writer.write(null, iio, null );
			}
			else {
				if( writer.canInsertImage( i ) ) {
					writer.writeInsert( i , iio, null);
				}
				else {
					System.out.println( "不能插入" );
				}
			}
			//ImageIOHelper.storeImageToTiff( invertImage, ImageConstants.ORC_IMAGE_FILE_PATH + "/" + i + ".tif" );
		}
	}

}
