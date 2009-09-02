package image;

import image.bmp.BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ocr.ImageFilter;
import ocr.OCR;

public class ShowImage extends JFrame{

	private static final long serialVersionUID = -1002739608560883085L;
	private Image image;
	private URL url;
	public ShowImage( Image image ){
		this.image = image;
		initFromLocal();
    }
	
	private void initFromLocal() {
        JLabel label =new JLabel(new ImageIcon(image));
        add(label);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
	}
	
	public ShowImage( URL url ) {
		this.url = url;
		initFromRemote();
	}

	private void initFromRemote() {
		InputStream is = null;
		Image image = null;
		try {
			is = this.url.openStream();
			image = new BMP(is).getBMPImage();
			image = ImageIOHelper.invertImage(image);
		}
		catch( IOException ioe ) {
			ioe.printStackTrace();
		}
		finally {
			if( is != null ) {
				try {
					is.close();
				}
				catch( IOException ioe ) {
					ioe.printStackTrace();
				}
			}
		}
        JLabel label =new JLabel(new ImageIcon(image));
        add(label);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
	}
	
    public void showImage() {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
            	ShowImage si =  null;
            	if( url == null ) {
            		si = new  ShowImage( image );
            	}
            	else {
            		si = new ShowImage( url );
            	}
            	int centerWidth = ( si.getToolkit().getScreenSize().width - si.getWidth() )/2;
        		int centerHeight = ( si.getToolkit().getScreenSize().height - si.getHeight() )/2;
            	si.setLocation( centerWidth, centerHeight );
            	si.setVisible(true);
            }
        });
    }
    
	
    public static void main(String[] args) throws Exception{
		URL url = new URL( "http://vote.sun0769.com/include/code.asp?s=youthnet&aj=0.07248511577958028");
		Image image = new BMP( url.openStream() ).getBMPImage();
	
		//反相
		BufferedImage invertImage = ImageIOHelper.invertImage(image);
		ImageFilter imageFilter1 = new ImageFilter(invertImage);
		BufferedImage bi1 = imageFilter1.scaling(5.0f);
		ShowImage si1 = new ShowImage( bi1 );
		si1.showImage();
		
		ImageIOHelper.storeImageToTiff( bi1, ImageConstants.BMP_OUTPUT_FILE );
		
		OCR ocr = new OCR();
        String result = ocr.recognizeTiffToText( new File( ImageConstants.BMP_OUTPUT_FILE) );
        
		System.out.println( "result:" + result );
    }
} 
