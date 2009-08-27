package image.simpledecoder.filter;
import image.simpledecoder.pub.filter.AbstractFilter;

//过滤前景色为白色的过滤器
public class WhiteFilter extends AbstractFilter {
	protected int filter(int p) {
		return isWhite(p)?1:0;
	}

	private boolean isWhite(int p) {
		return (p & 0x0ff) > 240 && (p >> 8 & 0x0ff) > 240
				&& (p >> 16 & 0xff) > 240;
	}
}