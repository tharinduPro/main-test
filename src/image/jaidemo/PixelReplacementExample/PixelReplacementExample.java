package image.jaidemo.PixelReplacementExample;

/*
 * $RCSfile: PixelReplacementExample.java,v $
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
 * $Date: 2005/02/11 05:20:11 $
 * $State: Exp $
 */

import image.ImageConstants;
import image.jaidemo.common.DisplayJAI;

import java.awt.Dimension;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.media.jai.BorderExtender;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Example of pixel replacement functionality.
 *
 * Usage: java PixelReplacementExample inFile outFile [-s subsampling]
 *
 * A window containing two images should be displayed. The image on the left
 * should have a black border on the right and bottom edges and the image on
 * the right should have a reflected border on the right and bottom edges.
 */
public class PixelReplacementExample {
    public static void main(String[] args) throws Throwable {
//        if(args.length < 2) {
//            throw new IllegalArgumentException
//                ("Usage: java PixelReplacementExample inFile outFile [-s subsampling]");
//        }

        File inFile = new File( ImageConstants.BMP_BAD_FILE );
        File outFile = new File( ImageConstants.JPEG_GOOD_FILE );

        int subsampling = 1;
//        for(int i = 0; i < args.length; i++) {
//            if(args[i].equalsIgnoreCase("-s") && args.length > i + 1) {
//                subsampling = Integer.valueOf(args[i+1]).intValue();
//                if(subsampling < 1) {
//                    throw new IllegalArgumentException("subsampling < 1!");
//                }
//            }
//        }

        RenderedImage source =
            new TiledImage(ImageIO.read(inFile), 32, 32);

        ParameterBlock pbb = (new ParameterBlock()).addSource(source);
        pbb.add(0).add(64).add(0).add(64);
        pbb.add(BorderExtender.createInstance(BorderExtender.BORDER_ZERO));
        RenderedOp border = JAI.create("border", pbb);

        ParameterBlock pb = (new ParameterBlock()).addSource(border);
        pb.set(ImageIO.createImageOutputStream(outFile), 0);
        pb.set(Boolean.TRUE, 5);
        pb.set(new Dimension(32, 32), 6);
        if(subsampling != 1) {
            ImageWriteParam writeParam = new ImageWriteParam(null);
            writeParam.setSourceSubsampling(subsampling, subsampling, 0, 0);
            pb.set(writeParam, 12);
        }
        ImageWriter writer = (ImageWriter)
            (ImageWriter)ImageIO.getImageWritersByFormatName("tiff").next();
        pb.set(writer, 13);
        RenderedOp destNode = JAI.create("ImageWrite", pb);

        PlanarImage destRendering = destNode.getRendering();
        destNode.getSourceImage(0).removeSink(destNode);
        destNode.getSourceImage(0).addSink(destRendering);

        RenderedImage preEventImage = ImageIO.read(outFile);

        pbb.set(BorderExtender.createInstance(BorderExtender.BORDER_REFLECT),
                4);
        border.setParameterBlock(pbb);

        RenderedImage postEventImage = ImageIO.read(outFile);

        JPanel prePanel = new DisplayJAI(preEventImage);
        JPanel postPanel = new DisplayJAI(postEventImage);

        JFrame frame = new JFrame("PixelReplacementExample");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        java.awt.Container pane = frame.getContentPane();
        pane.setLayout(new java.awt.GridLayout(1, 2));
        JScrollPane preScroll = new JScrollPane(prePanel);
        java.awt.Dimension size = new java.awt.Dimension(512, 512);
        preScroll.setMaximumSize(size);
        preScroll.setPreferredSize(size);
        pane.add(preScroll);
        JScrollPane postScroll = new JScrollPane(postPanel);
        postScroll.setMaximumSize(size);
        postScroll.setPreferredSize(size);
        pane.add(postScroll);
        frame.pack();
        frame.show();
    }
}
