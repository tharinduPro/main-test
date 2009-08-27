package image;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class BMP2TIFF {
	public static void main(String[] args) throws Exception {
		String outputFile = "E:/TestWork/Test/img/output/test.tiff";
		OutputStream outputStreamFile = new FileOutputStream(outputFile);
		TIFFEncodeParam param = new TIFFEncodeParam();
		ImageEncoder imageEncoder = ImageCodec.createImageEncoder("tiff", outputStreamFile, param);
		
		String srcFile = "E:/TestWork/Test/img/code1.bmp";
		RenderedOp srcOp = JAI.create("fileload", srcFile);
		imageEncoder.encode(srcOp);
		outputStreamFile.close();

	}
	
	//输出java支持的图片格式。
	public static void showImageFormat() {
		 System.out.println("支持写的图片格式:" +Arrays.toString(ImageIO.getWriterFormatNames()));
	     System.out.println("支持读的图片格式:" +Arrays.toString(ImageIO.getReaderFormatNames()));
	}
}