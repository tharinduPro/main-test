package ocr;

import image.ImageIOHelper;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 
 * 图像过滤,增强OCR识别成功率
 * 
 */
public class ImageFilter {
	protected transient final Logger logger = LogManager.getLogger(this.getClass());
	private BufferedImage image;

	private int imageWidth, imageHeight;

	//保存从图像中检索的 RGB 像素的整数数组
	private int[] pixArray;

	public ImageFilter(BufferedImage image) {
		this.image = image;
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();
		pixArray = new int[imageWidth * imageHeight];
	}

	/** 图像二值化即变为灰度图片 */
	public BufferedImage changeGrey() {
		int topLeftX = 0;
		int topLeftY = 0;
		int startPixIndex = 0;
		//数组中一行像素到下一行像素之间的距离
		int scanSize = imageWidth;
		PixelGrabber pixGrabber = new PixelGrabber(image.getSource(), topLeftX, topLeftY, 
				imageWidth, imageHeight, pixArray, startPixIndex, scanSize);
		try {
			//请求 Image 或 ImageProducer 开始传递像素，并等待传递完相关矩形中的所有像素。
			pixGrabber.grabPixels();
		} catch (InterruptedException e) {
			logger.error("异常:", e);
			e.printStackTrace();
		}
		
		
		// 设定二值化的域值，默认值为100
		int threshold = 100;
		
		
		// 对图像进行二值化处理，Alpha值保持不变
		ColorModel colorModel = ColorModel.getRGBdefault();
		for (int i = 0; i < imageWidth * imageHeight; i++) {
			int red, green, blue;
			int alpha = colorModel.getAlpha(pixArray[i]);
			if (colorModel.getRed(pixArray[i]) > threshold) {
				red = 255;
			} else {
				red = 0;
			}

			if (colorModel.getGreen(pixArray[i]) > threshold) {
				green = 255;
			} else {
				green = 0;
			}

			if (colorModel.getBlue(pixArray[i]) > threshold) {
				blue = 255;
			} else {
				blue = 0;
			}

			pixArray[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		// 将数组中的象素产生一个图像
		return ImageIOHelper.changeImageProducerToBufferedImage(new MemoryImageSource(imageWidth, imageHeight, pixArray, 0, imageWidth));
	}

	/** 提升清晰度,进行锐化 */
	public BufferedImage sharp( ) {
		PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, imageWidth, imageHeight, pixArray, 0, imageWidth);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			logger.error("异常:", e);
		}

		// 象素的中间变量
		int tempPixels[] = new int[imageWidth * imageHeight];
		for (int i = 0; i < imageWidth * imageHeight; i++) {
			tempPixels[i] = pixArray[i];
		}
		// 对图像进行尖锐化处理，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 1; i < imageHeight - 1; i++) {
			for (int j = 1; j < imageWidth - 1; j++) {
				int alpha = cm.getAlpha(pixArray[i * imageWidth + j]);

				// 对图像进行尖锐化
				int red6 = cm.getRed(pixArray[i * imageWidth + j + 1]);
				int red5 = cm.getRed(pixArray[i * imageWidth + j]);
				int red8 = cm.getRed(pixArray[(i + 1) * imageWidth + j]);
				int sharpRed = Math.abs(red6 - red5) + Math.abs(red8 - red5);

				int green5 = cm.getGreen(pixArray[i * imageWidth + j]);
				int green6 = cm.getGreen(pixArray[i * imageWidth + j + 1]);
				int green8 = cm.getGreen(pixArray[(i + 1) * imageWidth + j]);
				int sharpGreen = Math.abs(green6 - green5) + Math.abs(green8 - green5);

				int blue5 = cm.getBlue(pixArray[i * imageWidth + j]);
				int blue6 = cm.getBlue(pixArray[i * imageWidth + j + 1]);
				int blue8 = cm.getBlue(pixArray[(i + 1) * imageWidth + j]);
				int sharpBlue = Math.abs(blue6 - blue5) + Math.abs(blue8 - blue5);

				if (sharpRed > 255) {
					sharpRed = 255;
				}
				if (sharpGreen > 255) {
					sharpGreen = 255;
				}
				if (sharpBlue > 255) {
					sharpBlue = 255;
				}

				tempPixels[i * imageWidth + j] = alpha << 24 | sharpRed << 16 | sharpGreen << 8 | sharpBlue;
			}
		}

		// 将数组中的象素产生一个图像
		return ImageIOHelper.changeImageProducerToBufferedImage(new MemoryImageSource(imageWidth, imageHeight, tempPixels, 0, imageWidth));
	}

	/** 中值滤波 */
	public BufferedImage median() {
		PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, imageWidth, imageHeight, pixArray, 0, imageWidth);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			logger.error("异常:", e);
		}
		// 对图像进行中值滤波，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 1; i < imageHeight - 1; i++) {
			for (int j = 1; j < imageWidth - 1; j++) {
				int red, green, blue;
				int alpha = cm.getAlpha(pixArray[i * imageWidth + j]);

				// int red2 = cm.getRed(pixels[(i - 1) * iw + j]);
				int red4 = cm.getRed(pixArray[i * imageWidth + j - 1]);
				int red5 = cm.getRed(pixArray[i * imageWidth + j]);
				int red6 = cm.getRed(pixArray[i * imageWidth + j + 1]);
				// int red8 = cm.getRed(pixels[(i + 1) * iw + j]);

				// 水平方向进行中值滤波
				if (red4 >= red5) {
					if (red5 >= red6) {
						red = red5;
					} else {
						if (red4 >= red6) {
							red = red6;
						} else {
							red = red4;
						}
					}
				} else {
					if (red4 > red6) {
						red = red4;
					} else {
						if (red5 > red6) {
							red = red6;
						} else {
							red = red5;
						}
					}
				}

				// int green2 = cm.getGreen(pixels[(i - 1) * iw + j]);
				int green4 = cm.getGreen(pixArray[i * imageWidth + j - 1]);
				int green5 = cm.getGreen(pixArray[i * imageWidth + j]);
				int green6 = cm.getGreen(pixArray[i * imageWidth + j + 1]);
				// int green8 = cm.getGreen(pixels[(i + 1) * iw + j]);

				// 水平方向进行中值滤波
				if (green4 >= green5) {
					if (green5 >= green6) {
						green = green5;
					} else {
						if (green4 >= green6) {
							green = green6;
						} else {
							green = green4;
						}
					}
				} else {
					if (green4 > green6) {
						green = green4;
					} else {
						if (green5 > green6) {
							green = green6;
						} else {
							green = green5;
						}
					}
				}

				// int blue2 = cm.getBlue(pixels[(i - 1) * iw + j]);
				int blue4 = cm.getBlue(pixArray[i * imageWidth + j - 1]);
				int blue5 = cm.getBlue(pixArray[i * imageWidth + j]);
				int blue6 = cm.getBlue(pixArray[i * imageWidth + j + 1]);
				// int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);

				// 水平方向进行中值滤波
				if (blue4 >= blue5) {
					if (blue5 >= blue6) {
						blue = blue5;
					} else {
						if (blue4 >= blue6) {
							blue = blue6;
						} else {
							blue = blue4;
						}
					}
				} else {
					if (blue4 > blue6) {
						blue = blue4;
					} else {
						if (blue5 > blue6) {
							blue = blue6;
						} else {
							blue = blue5;
						}
					}
				}
				pixArray[i * imageWidth + j] = alpha << 24 | red << 16 | green << 8 | blue;
			}
		}

		// 将数组中的象素产生一个图像
		return ImageIOHelper.changeImageProducerToBufferedImage(new MemoryImageSource(imageWidth, imageHeight, pixArray, 0, imageWidth));
	}

	/** 线性灰度变换 */
	public BufferedImage lineGrey() {
		PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, imageWidth, imageHeight, pixArray, 0, imageWidth);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			logger.error("异常:", e);
		}
		// 对图像进行进行线性拉伸，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < imageWidth * imageHeight; i++) {
			int alpha = cm.getAlpha(pixArray[i]);
			int red = cm.getRed(pixArray[i]);
			int green = cm.getGreen(pixArray[i]);
			int blue = cm.getBlue(pixArray[i]);

			// 增加了图像的亮度
			red = (int) (1.1 * red + 30);
			green = (int) (1.1 * green + 30);
			blue = (int) (1.1 * blue + 30);
			if (red >= 255) {
				red = 255;
			}
			if (green >= 255) {
				green = 255;
			}
			if (blue >= 255) {
				blue = 255;
			}
			pixArray[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}

		// 将数组中的象素产生一个图像

		return ImageIOHelper.changeImageProducerToBufferedImage(new MemoryImageSource(imageWidth, imageHeight, pixArray, 0, imageWidth));
	}

	/** 转换为黑白灰度图 */
	public BufferedImage grayFilter() {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		return op.filter(image, null);
	}

	/** 平滑缩放 */
	public BufferedImage scaling(double s) {
		AffineTransform tx = new AffineTransform();
		tx.scale(s, s);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

	public BufferedImage scale(Float s) {
		int srcW = image.getWidth();
		int srcH = image.getHeight();
		int newW = Math.round(srcW * s);
		int newH = Math.round(srcH * s);
		// 先做水平方向上的伸缩变换
		BufferedImage tmp=new BufferedImage(newW, newH, image.getType()); 
		Graphics2D g= tmp.createGraphics(); 
		for (int x = 0; x < newW; x++) {
			g.setClip(x, 0, 1, srcH);
			// 按比例放缩
			g.drawImage(image, x - x * srcW / newW, 0, null);
		}

		// 再做垂直方向上的伸缩变换
		BufferedImage dst = new BufferedImage(newW, newH, image.getType()); 
		g = dst.createGraphics();
		for (int y = 0; y < newH; y++) {
			g.setClip(0, y, newW, 1);
			// 按比例放缩
			g.drawImage(tmp, 0, y - y * srcH / newH, null);
		}
		return dst;
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
