package image.bmp;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;

public class BMP {
	private BMPImageFileHeader fileHeader;
	private BMPImageInfoHeader infoHeader;
	private byte[] imageData;
	
	public BMP( InputStream inputStream) {
		fileHeader = new BMPImageFileHeader( inputStream );
		infoHeader = new BMPImageInfoHeader( inputStream );
		imageData = new byte[ infoHeader.getBitmapDataSize() ];
		try {
			inputStream.read(imageData);
		}
		catch( IOException ioe ) {
			ioe.printStackTrace();
		}
	}

	public Image getBMPImage() {
		InputStream fileInputStream = null;
		Image image = null;
		try {

			// 源图宽度
			int nWidth = getFileHeader().getWidth();

			// 源图高度
			int nHeight = getFileHeader().getHeight();
			
			// 位数
			int nBitCount = getInfoHeader().getBitsPerPixel();

			// 源图大小
			int nSizeImage = getInfoHeader().getBitmapDataSize();

			// 对24位BMP进行解析
			if (nBitCount == 24) {
				int nPad = (nSizeImage / nHeight) - nWidth * 3;
				int[] nData = new int[nHeight * nWidth];
				int[] nB = new int[nHeight * nWidth];
				int[] nR = new int[nHeight * nWidth];
				int[] nG = new int[nHeight * nWidth];
				byte bRGB[] = getImageData();
				int nIndex = 0;
				for (int h = 0; h < nHeight; h++) {
					for (int w = 0; w < nWidth; w++) {
						nData[nWidth * (nHeight - h - 1) + w] = (255 & 0xff) << 24
								| (((int) bRGB[nIndex + 2] & 0xff) << 16)
								| (((int) bRGB[nIndex + 1] & 0xff) << 8)
								| (int) bRGB[nIndex] & 0xff;
						nB[nWidth * (nHeight - h - 1) + w] = (int) bRGB[nIndex] & 0xff;
						nG[nWidth * (nHeight - h - 1) + w] = (int) bRGB[nIndex + 1] & 0xff;
						nR[nWidth * (nHeight - h - 1) + w] = (int) bRGB[nIndex + 2] & 0xff;
						nIndex += 3;
					}
					nIndex += nPad;
				}
				 Toolkit kit = Toolkit.getDefaultToolkit();
				 image = kit.createImage(new MemoryImageSource(nWidth, nHeight, nData, 0, nWidth));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				}
				catch( IOException ioe ) {
					ioe.printStackTrace();
				}
			}
		}
		return image;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return fileHeader + "\n" + infoHeader + "\n" + imageData.length;
	}


	/**
	 * @return the fileHeader
	 */
	public BMPImageFileHeader getFileHeader() {
		return fileHeader;
	}

	/**
	 * @param fileHeader the fileHeader to set
	 */
	public void setFileHeader(BMPImageFileHeader fileHeader) {
		this.fileHeader = fileHeader;
	}

	/**
	 * @return the infoHeader
	 */
	public BMPImageInfoHeader getInfoHeader() {
		return infoHeader;
	}

	/**
	 * @param infoHeader the infoHeader to set
	 */
	public void setInfoHeader(BMPImageInfoHeader infoHeader) {
		this.infoHeader = infoHeader;
	}

	/**
	 * @return the imageData
	 */
	public byte[] getImageData() {
		return imageData;
	}

	/**
	 * @param imageData the imageData to set
	 */
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	
}
