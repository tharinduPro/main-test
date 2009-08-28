package image.algorithm;

public class ImageFileHeader {
	public final static int fileHeaderLength = 28;
	private String fileFormat;		//显示文件标识
    private int fileSize;		//整个文件的大小
    private int bitmapDataOffset; //从文件开始到位图数据开始之间的数据(bitmap data)之间的偏移量
    private int bitmapHeaderSize; //位图信息头(Bitmap Info Header)的长度
    private int width; //位图的宽度，以象素为单位
    private int height;//位图的高度，以象素为单位
    private int planes;//位图的位面数（注：该值将总是1）
    
    public ImageFileHeader( byte[] fileHeaderArray ) {
    	fileFormat = new String (fileHeaderArray, 0,2 );
        fileSize = (((int) fileHeaderArray[5] & 0xff) << 24)
            | (((int) fileHeaderArray[4] & 0xff) << 16)
            | (((int) fileHeaderArray[3] & 0xff) << 8) | (int) fileHeaderArray[2] & 0xff;
        bitmapDataOffset = (((int) fileHeaderArray[13] & 0xff) << 24)
            | (((int) fileHeaderArray[12] & 0xff) << 16)
            | (((int) fileHeaderArray[11] & 0xff) << 8) | (int) fileHeaderArray[10] & 0xff;
        bitmapHeaderSize = (((int) fileHeaderArray[17] & 0xff) << 24)
            | (((int) fileHeaderArray[16] & 0xff) << 16)
            | (((int) fileHeaderArray[15] & 0xff) << 8) | (int) fileHeaderArray[14] & 0xff;
        width = (((int) fileHeaderArray[21] & 0xff) << 24)
            | (((int) fileHeaderArray[20] & 0xff) << 16)
            | (((int) fileHeaderArray[19] & 0xff) << 8) | (int) fileHeaderArray[18] & 0xff;
        height = (((int) fileHeaderArray[25] & 0xff) << 24)
            | (((int) fileHeaderArray[24] & 0xff) << 16)
            | (((int) fileHeaderArray[23] & 0xff) << 8) | (int) fileHeaderArray[22] & 0xff;
        planes = (((int) fileHeaderArray[27] & 0xff) << 8) | (int) fileHeaderArray[26] & 0xff;
    }
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public int getBitmapDataOffset() {
		return bitmapDataOffset;
	}
	public void setBitmapDataOffset(int bitmapDataOffset) {
		this.bitmapDataOffset = bitmapDataOffset;
	}
	public int getBitmapHeaderSize() {
		return bitmapHeaderSize;
	}
	public void setBitmapHeaderSize(int bitmapHeaderSize) {
		this.bitmapHeaderSize = bitmapHeaderSize;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getPlanes() {
		return planes;
	}
	public void setPlanes(int planes) {
		this.planes = planes;
	}

    @Override
    public String toString() {
        return 
            "fileFormat:" + fileFormat + "," +
            "fileSize:" + fileSize + "," +
            "bitmapDataOffset:" + bitmapDataOffset + "," +
            "bitmapHeaderSize:" + bitmapHeaderSize + "," +
            "width:" + width + "," +
            "height:" + height + "," +
            "planes:" + planes;
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bitmapDataOffset;
		result = prime * result + bitmapHeaderSize;
		result = prime * result
				+ ((fileFormat == null) ? 0 : fileFormat.hashCode());
		result = prime * result + fileSize;
		result = prime * result + height;
		result = prime * result + planes;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageFileHeader other = (ImageFileHeader) obj;
		if (bitmapDataOffset != other.bitmapDataOffset)
			return false;
		if (bitmapHeaderSize != other.bitmapHeaderSize)
			return false;
		if (fileFormat == null) {
			if (other.fileFormat != null)
				return false;
		} else if (!fileFormat.equals(other.fileFormat))
			return false;
		if (fileSize != other.fileSize)
			return false;
		if (height != other.height)
			return false;
		if (planes != other.planes)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
    
}
