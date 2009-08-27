package image.jaidemo.common;

/*
 * $RCSfile: DisplayJAI.java,v $
 *
 * 
 * Copyright (c) 2005 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 
 * 
 * - Redistribution of source code must retain the above copyright 
 *   notice, this  list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in 
 *   the documentation and/or other materials provided with the
 *   distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of 
 * contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any 
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND 
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL 
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF 
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR 
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES. 
 * 
 * You acknowledge that this software is not designed or intended for 
 * use in the design, construction, operation or maintenance of any 
 * nuclear facility. 
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 05:20:14 $
 * $State: Exp $
 */

/**
 *  Example display class Swing Component that is able
 *  to contain an image.  The size of the image
 *  and size of the container can be different.
 *  The image can be positioned within the
 *  container.  This class extends JPanel in order
 *  to support layout management.  Tiling is supported
 *  as of JDK1.3 via drawRenderedImage().
 *
 *  @see javax.swing.JPanel
 *  @see javax.swing.JComponent
 *  @see java.awt.image.RenderedImage
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import javax.swing.JPanel;


public class DisplayJAI extends JPanel
                        implements MouseListener,
                                   MouseMotionListener {

    /** image to display */
    protected RenderedImage source = null;

    /** image origin relative to panel origin */
    protected int originX = 0;
    protected int originY = 0;


    /** default constructor */
    public DisplayJAI() {
        super();
        setLayout(null);
    }

    /** constructor with given image */
    public DisplayJAI(RenderedImage image) {
        super();
        setLayout(null);
        source = image;

        // Swing geometry
        int w = source.getWidth();
        int h = source.getHeight();
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right,
                                      h + insets.top + insets.bottom);


        setPreferredSize(dim);
    }

    /** move image within it's container */
    public void setOrigin(int x, int y) {
        originX = x;
        originY = y;
        repaint();
    }

    /** get the image origin */
    public Point getOrigin() {
        return new Point(originX, originY);
    }

    /** use to display a new image */
    public void set(RenderedImage im) {
        source = im;

        // Swing geometry
        int w = source.getWidth();
        int h = source.getHeight();
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right,
                                      h + insets.top + insets.bottom);


        setPreferredSize(dim);
        revalidate();
        repaint();
    }

    /** use to display a new image, with origins */
    public void set(RenderedImage im, int x, int y) {
        source = im;

        // Swing geometry
        int w = source.getWidth();
        int h = source.getHeight();
        Insets insets = getInsets();
        Dimension dim = new Dimension(w + insets.left + insets.right,
                                      h + insets.top + insets.bottom);

        setPreferredSize(dim);

        originX = x;
        originY = y;

        revalidate();
        repaint();
    }

    /** paint routine */
    public synchronized void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        // empty component (no image)
        if ( source == null ) {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        // account for borders
        Insets insets = getInsets();
        int tx = insets.left + originX;
        int ty = insets.top  + originY;

        // clear damaged component area
        Rectangle clipBounds = g2d.getClipBounds();
        g2d.setColor(getBackground());
        g2d.fillRect(clipBounds.x,
                     clipBounds.y,
                     clipBounds.width,
                     clipBounds.height);

        /**
            Translation moves the entire image within the container
        */
        try {
            g2d.drawRenderedImage(source,
                                  AffineTransform.getTranslateInstance(tx, ty));
        } catch( OutOfMemoryError e ) {
        }
    }

    /** mouse event handlers */
    public void mousePressed(MouseEvent e)  { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseMoved(MouseEvent e)    { }
    public void mouseDragged(MouseEvent e)  { }
    public void mouseEntered(MouseEvent e)  { }
    public void mouseExited(MouseEvent e)   { }
    public void mouseClicked(MouseEvent e)  { }
}


