package ocr;

import image.ImageConstants;
import image.ImageIOHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.util.OS;

public class OCR {
	protected transient final Logger logger = LogManager.getLogger(this.getClass());
	private final String LANG_OPTION = "-l";
	//换行符
	private final String EOL = System.getProperty("line.separator");
	private String tessPath = new File("tesseract").getAbsolutePath();

	//辨认图片格式为tif的图片，后缀必须为.tif
	public String recognizeTiffToText(File imageFile) throws Exception {
		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer stringBuffer = new StringBuffer();

		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(tessPath + "\\tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(tessPath + "/\tesseract");
		}
		cmd.add("");
		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
		cmd.add("digital");

		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());

		cmd.set(1, imageFile.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();

		int w = process.waitFor();

		if (w == 0) {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));

			String str;
			while ((str = in.readLine()) != null) {
				stringBuffer.append(str).append(EOL);
			}
			in.close();
		} else {
			String msg;
			switch (w) {
				case 1:
					msg = "Errors accessing files. There may be spaces in your image's filename.";
					break;
				case 29:
					msg = "Cannot recognize the image or its selected region.";
					break;
				case 31:
					msg = "Unsupported image format.";
					break;
				default:
					msg = "Errors occurred.";
			}
			throw new RuntimeException(msg);
		}

		new File(outputFile.getAbsolutePath() + ".txt").delete();
		logger.trace("图像识别结果:{}" + stringBuffer);
		return stringBuffer.toString();
	}
    public static void main( String args[] ) throws Exception{
       OCR ocr = new OCR();
	   File tempImage = ImageIOHelper.changeToTiff( new File( ImageConstants.BMP_OUTPUT_FILE) );
       String result = ocr.recognizeTiffToText( tempImage );
       System.out.println( "result:" + result );
    }
}
