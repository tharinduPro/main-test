package image.embed;

import image.ImageConstants;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.RandomAccessFile;

import javax.swing.JFrame;

public class ShowImageFrame extends JFrame {

	private static final long serialVersionUID = 7408957633428496773L;
	
	private Image img;

	public ShowImageFrame() {
		
		try { 
			RandomAccessFile raf=new RandomAccessFile(ImageConstants.BMP_BAD_FILE,"r");
			int imageLength = (int)raf.length();
			byte[] map = new byte[imageLength]; 
			raf.read(map,0,imageLength);
			for (int i=0;i<map.length;i++) {
				System.out.print(map[i]+", "); if ((i%10)==0) System.out.println();
			}
			img = Toolkit.getDefaultToolkit().createImage(map);
		} catch (Exception ex) { System.out.println(ex);
		
		}
	}

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, 600, 600, this);
	}

	public static void main(String[] args) {
		ShowImageFrame imageFrame = new ShowImageFrame();
		imageFrame.setSize(600, 600);
		imageFrame.setVisible(true);
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}
	
}
