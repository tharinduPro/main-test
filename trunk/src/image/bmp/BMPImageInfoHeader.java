package image.bmp;

import java.io.IOException;
import java.io.InputStream;

public class BMPImageInfoHeader {
	public final static int infoHeaderLength = 26;
	
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
   private int bitsPerPixel;

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
   private int compression;
    
    //用字节数表示的位图数据的大小。该数必须是4的倍数
   private int bitmapDataSize;
    
    // 用象素/米表示的水平分辨率
   private int hResolution;

    // 用象素/米表示的垂直分辨率
   private int vResolution;
    
    //位图使用的颜色数。
   private int colors;
    
    //指定重要的颜色数。当该域的值等于颜色数时（或者等于0时），表示所有颜色都一样重要
   private int importantColors;
    
	public BMPImageInfoHeader( InputStream inputStream ) {
    	byte[] imageInfoHeaderArray = new byte[infoHeaderLength];
    	try {
    		inputStream.read(imageInfoHeaderArray);
    	}
    	catch( IOException ioe ) {
    		ioe.printStackTrace();
    	}
		this.bitsPerPixel = (((int) imageInfoHeaderArray[1] & 0xff) << 8) | (int) imageInfoHeaderArray[0] & 0xff; 
		this.compression = (((int) imageInfoHeaderArray[5] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[4] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[3] & 0xff) << 8) | (int) imageInfoHeaderArray[2] & 0xff;
		this.bitmapDataSize = (((int) imageInfoHeaderArray[9] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[8] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[7] & 0xff) << 8) | (int) imageInfoHeaderArray[6] & 0xff;
		this.hResolution = (((int) imageInfoHeaderArray[13] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[12] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[11] & 0xff) << 8) | (int) imageInfoHeaderArray[10] & 0xff;
		this.vResolution = (((int) imageInfoHeaderArray[17] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[16] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[15] & 0xff) << 8) | (int) imageInfoHeaderArray[14] & 0xff;
		this.colors = (((int) imageInfoHeaderArray[21] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[20] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[19] & 0xff) << 8) | (int) imageInfoHeaderArray[18] & 0xff;
		this.importantColors = (((int) imageInfoHeaderArray[25] & 0xff) << 24)
            | (((int) imageInfoHeaderArray[24] & 0xff) << 16)
            | (((int) imageInfoHeaderArray[23] & 0xff) << 8) | (int) imageInfoHeaderArray[22] & 0xff;
	}
	
	
	@Override
	public String toString() {
		String toString = "bitsPerPixel:" + bitsPerPixel +
		  ",compression:" + compression + ",bitmapDataSize:" +  bitmapDataSize + 
		  ",hResolution:" + hResolution + ",vResolution:" + vResolution + 
		  ",colors:" + colors + ",importantColors:" + importantColors;
		return toString;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bitmapDataSize;
		result = prime * result + bitsPerPixel;
		result = prime * result + colors;
		result = prime * result + compression;
		result = prime * result + hResolution;
		result = prime * result + importantColors;
		result = prime * result + vResolution;
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BMPImageInfoHeader other = (BMPImageInfoHeader) obj;
		if (bitmapDataSize != other.bitmapDataSize)
			return false;
		if (bitsPerPixel != other.bitsPerPixel)
			return false;
		if (colors != other.colors)
			return false;
		if (compression != other.compression)
			return false;
		if (hResolution != other.hResolution)
			return false;
		if (importantColors != other.importantColors)
			return false;
		if (vResolution != other.vResolution)
			return false;
		return true;
	}


	/**
	 * @return the bitsPerPixel
	 */
	public int getBitsPerPixel() {
		return bitsPerPixel;
	}

	/**
	 * @param bitsPerPixel the bitsPerPixel to set
	 */
	public void setBitsPerPixel(int bitsPerPixel) {
		this.bitsPerPixel = bitsPerPixel;
	}

	/**
	 * @return the compression
	 */
	public int getCompression() {
		return compression;
	}

	/**
	 * @param compression the compression to set
	 */
	public void setCompression(int compression) {
		this.compression = compression;
	}

	/**
	 * @return the bitmapDataSize
	 */
	public int getBitmapDataSize() {
		return bitmapDataSize;
	}

	/**
	 * @param bitmapDataSize the bitmapDataSize to set
	 */
	public void setBitmapDataSize(int bitmapDataSize) {
		this.bitmapDataSize = bitmapDataSize;
	}

	/**
	 * @return the hResolution
	 */
	public int getHResolution() {
		return hResolution;
	}

	/**
	 * @param resolution the hResolution to set
	 */
	public void setHResolution(int resolution) {
		hResolution = resolution;
	}

	/**
	 * @return the vResolution
	 */
	public int getVResolution() {
		return vResolution;
	}

	/**
	 * @param resolution the vResolution to set
	 */
	public void setVResolution(int resolution) {
		vResolution = resolution;
	}

	/**
	 * @return the colors
	 */
	public int getColors() {
		return colors;
	}

	/**
	 * @param colors the colors to set
	 */
	public void setColors(int colors) {
		this.colors = colors;
	}

	/**
	 * @return the importantColors
	 */
	public int getImportantColors() {
		return importantColors;
	}

	/**
	 * @param importantColors the importantColors to set
	 */
	public void setImportantColors(int importantColors) {
		this.importantColors = importantColors;
	}
    
    
}
