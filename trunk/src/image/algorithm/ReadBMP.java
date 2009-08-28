package image.algorithm;

import java.io.FileInputStream;

//获取待检测图像 ，
//数据保存在数组
//nData[]，nB[] ，nG[] ，nR[]中
public class ReadBMP {
	public void getBMPImage(String source) throws Exception {

		clearNData(); // 清除数据保存区
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(source);
			int bfLen = 14;
			byte bf[] = new byte[bfLen];
			fileInputStream.read(bf, 0, bfLen); // 读取14字节BMP文件头
			int biLen = 40;
			byte bi[] = new byte[biLen];
			fileInputStream.read(bi, 0, biLen); // 读取40字节BMP信息头

			// 源图宽度
			int nWidth = (((int) bi[7] & 0xff) << 24)
					| (((int) bi[6] & 0xff) << 16)
					| (((int) bi[5] & 0xff) << 8) | (int) bi[4] & 0xff;

			// 源图高度
			int nHeight = (((int) bi[11] & 0xff) << 24)
					| (((int) bi[10] & 0xff) << 16)
					| (((int) bi[9] & 0xff) << 8) | (int) bi[8] & 0xff;

			// 位数
			nBitCount = (((int) bi[15] & 0xff) << 8) | (int) bi[14] & 0xff;

			// 源图大小
			int nSizeImage = (((int) bi[23] & 0xff) << 24)
					| (((int) bi[22] & 0xff) << 16)
					| (((int) bi[21] & 0xff) << 8) | (int) bi[20] & 0xff;

			// 对24位BMP进行解析
			if (nBitCount == 24) {
				int nPad = (nSizeImage / nHeight) - nWidth * 3;
				nData = new int[nHeight * nWidth];
				nB = new int[nHeight * nWidth];
				nR = new int[nHeight * nWidth];
				nG = new int[nHeight * nWidth];
				byte bRGB[] = new byte[(nWidth + nPad) * 3 * nHeight];
				fileInputStream.read(bRGB, 0, (nWidth + nPad) * 3 * nHeight);
				int nIndex = 0;
				for (int j = 0; j < nHeight; j++) {
					for (int i = 0; i < nWidth; i++) {
						nData[nWidth * (nHeight - j - 1) + i] = (255 & 0xff) << 24
								| (((int) bRGB[nIndex + 2] & 0xff) << 16)
								| (((int) bRGB[nIndex + 1] & 0xff) << 8)
								| (int) bRGB[nIndex] & 0xff;
						nB[nWidth * (nHeight - j - 1) + i] = (int) bRGB[nIndex] & 0xff;
						nG[nWidth * (nHeight - j - 1) + i] = (int) bRGB[nIndex + 1] & 0xff;
						nR[nWidth * (nHeight - j - 1) + i] = (int) bRGB[nIndex + 2] & 0xff;
						nIndex += 3;
					}
					nIndex += nPad;
				}
				// Toolkit kit = Toolkit.getDefaultToolkit();
				// image = kit.createImage(new MemoryImageSource(nWidth,
				// nHeight,
				// nData, 0, nWidth));

				/*
				 * //调试数据的读取
				 * 
				 * FileWriter fw = newFileWriter(
				 * "C:Documents and SettingsAdministratorMy DocumentsnDataRaw.txt"
				 * );//创建新文件 PrintWriter out = new PrintWriter(fw); for(int
				 * j=0;j<nHeight;j++){ for(int i=0;i<nWidth;i++){
				 * out.print((65536256+nData[nWidth (nHeight - j - 1) + i])+"_"
				 * +nR[nWidth (nHeight - j - 1) + i]+"_" +nG[nWidth (nHeight - j
				 * - 1) + i]+"_" +nB[nWidth (nHeight - j - 1) + i]+" ");
				 * 
				 * } out.println(""); } out.close();
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
		// return image;
	}
}