package ocr.another;

/* Copyright (c) 2009 the authors listed at the following URL, and/or
 the authors of referenced articles or incorporated external code:
 http://en.literateprograms.org/Floyd-Steinberg_dithering_(Java)?action=history&offset=20080201121723

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 Retrieved from: http://en.literateprograms.org/Floyd-Steinberg_dithering_(Java)?oldid=12476
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class RGBTriple {
	public final byte[] channels;

	public RGBTriple() {
		channels = new byte[3];
	}

	public RGBTriple(int R, int G, int B) {
		channels = new byte[] { (byte) R, (byte) G, (byte) B };
	}
}

public class FloydSteinbergDither {
	private static byte plus_truncate_uchar(byte a, int b) {
		if ((a & 0xff) + b < 0)
			return 0;
		else if ((a & 0xff) + b > 255)
			return (byte) 255;
		else
			return (byte) (a + b);
	}

	private static byte findNearestColor(RGBTriple color, RGBTriple[] palette) {
		int minDistanceSquared = 255 * 255 + 255 * 255 + 255 * 255 + 1;
		byte bestIndex = 0;
		for (byte i = 0; i < palette.length; i++) {
			int Rdiff = (color.channels[0] & 0xff)
					- (palette[i].channels[0] & 0xff);
			int Gdiff = (color.channels[1] & 0xff)
					- (palette[i].channels[1] & 0xff);
			int Bdiff = (color.channels[2] & 0xff)
					- (palette[i].channels[2] & 0xff);
			int distanceSquared = Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
			if (distanceSquared < minDistanceSquared) {
				minDistanceSquared = distanceSquared;
				bestIndex = i;
			}
		}
		return bestIndex;
	}

	public static byte[][] floydSteinbergDither(RGBTriple[][] image,
			RGBTriple[] palette) {
		byte[][] result = new byte[image.length][image[0].length];

		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[y].length; x++) {
				RGBTriple currentPixel = image[y][x];
				byte index = findNearestColor(currentPixel, palette);
				result[y][x] = index;

				for (int i = 0; i < 3; i++) {
					int error = (currentPixel.channels[i] & 0xff)
							- (palette[index].channels[i] & 0xff);
					if (x + 1 < image[0].length) {
						image[y + 0][x + 1].channels[i] = plus_truncate_uchar(
								image[y + 0][x + 1].channels[i],
								(error * 7) >> 4);
					}
					if (y + 1 < image.length) {
						if (x - 1 > 0) {
							image[y + 1][x - 1].channels[i] = plus_truncate_uchar(
									image[y + 1][x - 1].channels[i],
									(error * 3) >> 4);
						}
						image[y + 1][x + 0].channels[i] = plus_truncate_uchar(
								image[y + 1][x + 0].channels[i],
								(error * 5) >> 4);
						if (x + 1 < image[0].length) {
							image[y + 1][x + 1].channels[i] = plus_truncate_uchar(
									image[y + 1][x + 1].channels[i],
									(error * 1) >> 4);
						}
					}

				}

			}
		}

		return result;
	}

	public static void main(String[] args) throws IOException {
		RGBTriple[][] image = new RGBTriple[145][100];

		InputStream raw_in = new BufferedInputStream(new FileInputStream(
				args[0]));
		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[0].length; x++) {
				image[y][x] = new RGBTriple();
				raw_in.read(image[y][x].channels, 0, 3);
			}
		}
		raw_in.close();

		RGBTriple[] palette = { new RGBTriple(149, 91, 110),
				new RGBTriple(176, 116, 137), new RGBTriple(17, 11, 15),
				new RGBTriple(63, 47, 69), new RGBTriple(93, 75, 112),
				new RGBTriple(47, 62, 24), new RGBTriple(76, 90, 55),
				new RGBTriple(190, 212, 115), new RGBTriple(160, 176, 87),
				new RGBTriple(116, 120, 87), new RGBTriple(245, 246, 225),
				new RGBTriple(148, 146, 130), new RGBTriple(200, 195, 180),
				new RGBTriple(36, 32, 27), new RGBTriple(87, 54, 45),
				new RGBTriple(121, 72, 72) };

		byte[][] result = floydSteinbergDither(image, palette);

		OutputStream raw_out = new BufferedOutputStream(new FileOutputStream(
				args[1]));
		for (int y = 0; y < result.length; y++) {
			for (int x = 0; x < result[y].length; x++) {
				raw_out.write(palette[result[y][x]].channels, 0, 3);
			}
		}
		raw_out.close();
	}

}
