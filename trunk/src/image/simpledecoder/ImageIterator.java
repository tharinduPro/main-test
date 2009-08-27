package image.simpledecoder;

import java.util.Iterator;

public class ImageIterator implements Iterator<ImageData> {
	int x;
	ImageData current;
	ImageData next;

	public ImageIterator(ImageData imageData) {
		this.current = imageData;
	}

	public boolean hasNext() {
		if (next != null) {
			return true;
		}
		next = getNext();
		return next != null;
	}

	public ImageData next() {
		ImageData temp = next;
		next = null;
		return temp;
	}
	
	public void remove() {

	}
	
	ImageData getNext() {
		int x1 = current.skipXEmpty(x, 0);
		if (x1 == -1) {
			return null;
		}
		int x2 = current.skipXEntity(x1, 1);
		if (x2 == -1) {
			x2 = current.getWidth();
		}
		x = x2;
		int y1 = current.skipYEmpty(0, 0);
		if (y1 == -1)
			return null;
		int y2 = current.skipYEntity(y1, 1);
		if (y2 == -1)
			y2 = current.getHeight();
		return current.clone(x1, y1, x2 - x1, y2 - y1);
	}
}
