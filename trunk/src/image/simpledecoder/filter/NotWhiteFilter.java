package image.simpledecoder.filter;

import image.simpledecoder.pub.filter.AbstractFilter;

public class NotWhiteFilter extends AbstractFilter {
	protected int filter(int p) {
		return isNotWhite(p) ? 1 : 0;
	}

	private boolean isNotWhite(int p) {
		boolean b = (p & 0X0ff) == 255 && (p >> 8 & 0X0ff) == 255
				&& (p >> 16 & 0xff) == 255;
		return !b;
	}
}