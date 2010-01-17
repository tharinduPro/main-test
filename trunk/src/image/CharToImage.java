package image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class CharToImage {
	 public static void main(String[] args) throws Exception{
		int width = 28;
		int height = 20;
		BufferedImage image = new BufferedImage( width,   height, BufferedImage.TYPE_BYTE_GRAY );  
		Graphics g = image.getGraphics();  
		g.drawString("GWC", 0, 16);
		g.dispose();
		int i = 0;
		for( int h =0; h < 20; h ++ ) {
			for( int w=0; w< 28; w++ ) {
				int index = image.getData().getDataBuffer().getElem(i);
				if( index == 255 ) {
					System.out.print( "1" );
				}
				else {
					System.out.print( "_" );
				}
				i++;
			}
			System.out.println(  );
		}
		
	}
}
