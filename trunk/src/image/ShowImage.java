package image;

import image.bmp.BMP;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ocr.OCR;

public class ShowImage extends JFrame{

	private static final long serialVersionUID = -1002739608560883085L;
	private String imageFile;
	private URL url;
	public ShowImage(String imageFile ){
		this.imageFile = imageFile;
		initFromLocal();
    }
	
	private void initFromLocal() {
		InputStream is = null;
		Image image = null;
		try {
			image = ImageIO.read( new File( imageFile) );
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
            		si = new  ShowImage( imageFile );
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
		ImageIOHelper.storeImageToTiff( ImageIOHelper.invertImage(image), ImageConstants.BMP_OUTPUT_FILE );
		
		ShowImage si = new ShowImage( ImageConstants.BMP_OUTPUT_FILE );
		si.showImage();
		OCR ocr = new OCR();
		String result = ocr.recognizeText( new File( ImageConstants.BMP_OUTPUT_FILE ) );
		System.out.println( "result:" + result );
    }
} 
