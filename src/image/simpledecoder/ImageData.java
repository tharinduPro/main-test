package image.simpledecoder;

import image.simpledecoder.filter.WhiteFilter;
import image.simpledecoder.pub.filter.Filter;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author admin
 * 用于存储图像的像素的值和图像的长宽
 */
public class ImageData {
	public int[][] data;
	private int width;
	private int height;
	public char code;

	public ImageData() {

	}
	
	public ImageData(BufferedImage bufferedImage) {
		this(bufferedImage, new WhiteFilter());
	}

	public ImageData(BufferedImage bufferedImage, Filter filter) {
		height = bufferedImage.getHeight();
		width = bufferedImage.getWidth();
		data = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pix = bufferedImage.getRGB(j, i);
				data[i][j] = pix;
			}
		}
		filter.doFilter(data);
	}

	public ImageData[] split() {
		List<ImageData> list = new ArrayList<ImageData>();
		ImageIterator imageIterator = new ImageIterator(this);
		while (imageIterator.hasNext()) {
			list.add(imageIterator.next());
		}
		return (ImageData[]) list.toArray(new ImageData[0]);
	}

	//返回第一个值不等于指定value的点的位置X轴位置
	public int skipXEmpty(int begin, int value) {
		for (int w = begin; w < width; w++) {
			for (int h = 0; h < height; h++) {
				if (data[h][w] != value) {
					return w;
				}
			}
		}
		return -1;
	}
	//返回第一个值不等于指定value的点的位置Y轴位置
	public int skipYEmpty(int begin, int value) {
		for (int h = begin; h < height; h++) {
			for (int w = 0; w < width; w++) {
				if (data[h][w] != value) {
					return h;
				}
			}
		}
		return -1;
	}
	
	//返回Y轴上总是找不到指定value值的特定X轴位置
	public int skipXEntity(int begin, int value) {
		for (int w = begin; w < width; w++) {
			for (int h = 0; h < height; h++) {
				if (data[h][w] == value) {
					break;
				}
				if (h == height - 1) {
					return w;
				}
			}
		}
		return -1;
	}
	//返回X轴上总是找不到指定value值的特定Y轴位置
	public int skipYEntity(int begin, int value) {
		for (int h = begin; h < height; h++) {
			for (int w = 0; w < width; w++) {
				if (data[h][w] == value) {
					break;
				}
				if (w == width - 1)
					return h;
			}
		}
		return -1;
	}
	//从某点开始复制数组
	ImageData clone(int x, int y, int width, int height) {
		ImageData imageData = new ImageData();
		imageData.width = width;
		imageData.height = height;
		imageData.data = new int[imageData.height][imageData.width];
		for (int h = 0; h < imageData.height; h++) {
			for (int w = 0; w < imageData.width; w++) {
				//不会越界？
				imageData.data[h][w] = data[h + y][w + x];
			}
		}
		return imageData;
	}

	//显示图片的数字样式
	public void show() {
		System.out.println();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print((data[i][j] == 1 ? "1" : " ") + "");
			}
			System.out.println();
		}
		System.out.println();
	}


	public static ImageData[] decodeFromFile(String path) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
		String line;
		List<ImageData> list = new ArrayList<ImageData>();
		while ((line = bufferedReader.readLine()) != null) {
			ImageData ia = decode(line);
			if (ia != null) {
				list.add(ia);
			}
		}
		return (ImageData[]) list.toArray(new ImageData[0]);
	}

	public static ImageData decode(String imageLine) {
		String[] splitString = imageLine.split("\\,", 4);
		if (splitString.length != 4)
			return null;
		if (splitString[0].length() != 1)
			return null;
		ImageData ia = new ImageData();
		ia.code = splitString[0].charAt(0);
		ia.width = Integer.parseInt(splitString[1]);
		ia.height = Integer.parseInt(splitString[2]);
		if (splitString[3].length() != ia.width * ia.height) {
			return null;
		}
		ia.data = new int[ia.height][ia.width];
		for (int i = 0; i < ia.height; i++) {
			for (int j = 0; j < ia.width; j++) {
				if (splitString[3].charAt(i * ia.width + j) == '1') {
					ia.data[i][j] = 1;
				} else {
					ia.data[i][j] = 0;
				}
			}
		}
		return ia;
	}

	//把图像的数组值转换成字符串，其中包括code, 和长宽
	public String encode() {
		StringBuffer sb = new StringBuffer();
		sb.append(code).append(",");
		sb.append(width).append(",");
		sb.append(height).append(",");
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (data[i][j] == 1) {
					sb.append('1');
				} else {
					sb.append('0');
				}
			}
		}
		return sb.toString();
	}
	
	public int hashCode() {
		int code = width ^ height;
		int count = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (data[i][j] == 1)
					count++;
			}
		}
		code ^= count;
		return code;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ImageData) {
			ImageData o = (ImageData) obj;
			if (o.height != height)
				return false;
			if (o.width != width)
				return false;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (o.data[i][j] != data[i][j])
						return false;
				}
			}
			return true;
		} else {
			return false;
		}
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
}