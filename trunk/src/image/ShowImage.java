package image;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ShowImage extends JFrame{

	private static final long serialVersionUID = -1002739608560883085L;
	private String imageFile;
	public ShowImage(String imageFile ){
		this.imageFile = imageFile;
		init();
    }
	
	private void init() {
        Image image = null;
        try{
        	File file = new File( imageFile );
            image=ImageIO.read(file);
        }catch(IOException ex){
        	ex.printStackTrace();
        }
        JLabel label =new JLabel(new ImageIcon(image));
        add(label);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
	}

    public void showImage() {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
            	ShowImage si =  new ShowImage( imageFile );
            	int centerWidth = ( si.getToolkit().getScreenSize().width - si.getWidth() )/2;
        		int centerHeight = ( si.getToolkit().getScreenSize().height - si.getHeight() )/2;
            	si.setLocation( centerWidth, centerHeight );
            	si.setVisible(true);
            }
        });
    }
    
	
    public static void main(String[] args){
		final String srcFile = "E:/TestWork/Test/img/code.bmp";
		ShowImage si = new ShowImage( srcFile );
		si.showImage();
    }
} 
