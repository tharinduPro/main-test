package ocr;

import java.awt.image.BufferedImage;
import java.io.File;

import com.jhlabs.image.ThresholdFilter;

public class Test {
	public static void main( String args[] ) {
		ThresholdFilter thresholdFilter = new ThresholdFilter(); 
		BufferedImage inImage = ImageIOHelper.getImage( new File("E:/TestWork/Test/img/(8).jpeg") );
		BufferedImage outImage = ImageIOHelper.getImage( new File("E:/TestWork/Test/img/(8).tiff") );
		BufferedImage newBufferedImage = thresholdFilter.filter( inImage,  outImage );
		System.out.println( thresholdFilter.getUpperThreshold() ); 
		ImageIOHelper.createImage( newBufferedImage, new File("E:/TestWork/Test/img/(8).tiff") );
	}
}
