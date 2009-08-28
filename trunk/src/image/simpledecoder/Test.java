package image.simpledecoder;

import image.ImageConstants;
import image.algorithm.ImageFileHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Test {
	public static void main( String args[] ) throws Exception {
		InputStream badIs = new FileInputStream(new File(ImageConstants.BMP_BAD_FILE));
		byte[] imageInfoHeaderArray = new byte[26];
		badIs.skip(ImageFileHeader.fileHeaderLength);
		badIs.read(imageInfoHeaderArray);
		for (int i=0;i<imageInfoHeaderArray.length;i++) {
			System.out.print(imageInfoHeaderArray[i]+", "); 
		}
        /**
         * 每个象素的位数
         * 1 - 单色位图（实际上可有两种颜色，缺省情况下是黑色和白色。你可以自己定义这两种颜色）
         *
         * 4 - 16 色位图
         *
         * 8 - 256 色位图
         *
         * 16 - 16bit 高彩色位图
         *
         * 24 - 24bit 真彩色位图
         *
         * 32 - 32bit 增强型真彩色位图
         */
        int bitsPerPixel =(((int) imageInfoHeaderArray[1] & 0xff) << 8) | (int) imageInfoHeaderArray[0] & 0xff; 
        System.out.println();
        System.out.println( bitsPerPixel );

        /**
         *压缩说明：
         *
         *0 - 不压缩 (使用BI_RGB表示)
         *
         *1 - RLE 8-使用8位RLE压缩方式(用BI_RLE8表示)
         *
         *2 - RLE 4-使用4位RLE压缩方式(用BI_RLE4表示)
         *
         *3 - Bitfields-位域存放方式(用BI_BITFIELDS表示)
         */
        int compression = (((int) imageInfoHeaderArray[5] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[4] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[3] & 0xff) << 8) | (int) imageInfoHeaderArray[2] & 0xff;
        
	}
}
