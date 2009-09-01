package image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.jhlabs.image.InvertFilter;
import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class ImageIOHelper {
	public static File changeImageFileToTiff(File imageFile) {
		File tempFile = makeTempTifFile(imageFile);
		Image image = null;
		try {
			image = ImageIO.read( imageFile );
		}
		catch( IOException ioe ) {
			ioe.printStackTrace();
		}
		storeImageToTiff(image, tempFile.getPath());
		return tempFile;
	}

	public static File createImage(BufferedImage bi) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("tempImageFile", ".tif");
			createImage( bi, tempFile );
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return tempFile;
	}
	
	public static File createImage(BufferedImage bi, File file) {
		try {
			file.deleteOnExit();
			TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.US);
			tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);

			// Get tif writer and set output to file
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");
			ImageWriter writer = writers.next();

			IIOImage image = new IIOImage(bi, null, null);
			file = makeTempTifFile(file);
			ImageOutputStream ios = ImageIO.createImageOutputStream(file);
			writer.setOutput(ios);
			writer.write(null, image, tiffWriteParam);
			ios.close();
			writer.dispose();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return file;
	}

	/**
	 * 
	 * @param imageFile
	 * @return 一个临时文件名xx0.tif
	 */
	private static File makeTempTifFile(File imageFile) {
		String path = imageFile.getPath();
		StringBuffer strB = new StringBuffer(path);
		strB.insert(path.lastIndexOf('.'), 0);
		return new File(strB.toString().replaceFirst("(?<=\\.)(\\w+)$", "tif"));
	}

	/**
	 * 把Image转换成BufferedImage
	 * @param image
	 * @return bufferedImage
	 */
	public static BufferedImage changeImageToBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		return bufferedImage;
	}

	/**
	 * 把imageProducer转换成bufferedImage
	 * @param ImageProducer
	 * @return BufferedImage
	 */
	public static BufferedImage changeImageProducerToBufferedImage(ImageProducer imageProducer) {
		return changeImageToBufferedImage(Toolkit.getDefaultToolkit().createImage(imageProducer));
	}

	/**
	 * 把BufferedImage转换成byte[]
	 * @param bufferedImage
	 * @return byte[]
	 */
	public static byte[] changeBufferedImageToByteArray(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		return buffer.getData();
	}
	
	/**
	 * 把Image反相
	 * @param Image
	 * @return BufferedImage
	 */
	public static BufferedImage invertImage( Image image ) {
    	BufferedImage bufferedImage = ImageIOHelper.changeImageToBufferedImage( image );
		BufferedImage invertBufferdImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		InvertFilter invertFilter = new InvertFilter();
		invertFilter.filter(bufferedImage, invertBufferdImage);
		return invertBufferdImage;
	}
	
	/**
	 * 把Image保存为tiff格式的图像
	 * @param image
	 * @param filePath 保存路径
	 */
    public static void storeImageToTiff(Image image, String filePath) {
    	BufferedImage bufferedImage = ImageIOHelper.changeImageToBufferedImage( image );
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
