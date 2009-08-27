/*
 * $RCSfile: MetadataReadExample.java,v $
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
 * $Date: 2005/02/11 05:20:10 $
 * $State: Exp $
 */

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.*;
import javax.media.jai.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import org.w3c.dom.*;

/**
 * Print out all stream metadata associated with the image stream for a given image file
 * as well as all image metadata for an image at a particular image in the stream.
 */
public class MetadataReadExample extends JFrame {
    public static void main(String[] args) throws Throwable {
        if(args.length < 0) {
            throw new IllegalArgumentException
                ("Usage: java MetadataReadExample filename [firstIndex [lastIndex]]");
        }

        //
        // Set file.
        //
        File file = new File(args[0]);
        if(!file.exists()) {
            throw new IllegalArgumentException(file+" does not exist");
        } else if(!file.isFile()) {
            throw new IllegalArgumentException(file+" is not a file");
        } else if(!file.canRead()) {
            throw new IllegalArgumentException(file+" is not readable");
        }

        //
        // Set indices.
        //
        int firstIndex = 0;
        int lastIndex = firstIndex;
        if(args.length > 1) {
            firstIndex = Integer.valueOf(args[1]).intValue();
            if(firstIndex < 0) {
                throw new IllegalArgumentException("firstIndex < 0");
            }
            if(args.length > 2) {
                lastIndex = Integer.valueOf(args[2]).intValue();
                if(lastIndex < firstIndex) {
                    throw new IllegalArgumentException("lastIndex < firstIndex");
                }
            }
        }


        //
        // Get an ImageReader.
        //
        ImageInputStream input = ImageIO.createImageInputStream(file);
        Iterator readers = ImageIO.getImageReaders(input);
        if(readers == null || !readers.hasNext()) {
            throw new RuntimeException("No ImageReaders found");
        }
        ImageReader reader = null;
        while(readers.hasNext() &&
              (reader == null ||
              !reader.getClass().getName().startsWith("com.sun.media"))) {
            reader = (ImageReader)readers.next();
        }
        if(reader == null) {
            throw new RuntimeException("No com.sun.media ImageReader found");
        }
        System.out.println("Using ImageReader "+reader.getClass().getName());
        reader.setInput(input);

        //
        // Read and print the stream metadata.
        //
        IIOMetadata streamMetadata = reader.getStreamMetadata();
        if(streamMetadata != null) {
            String[] metadataFormatNames = streamMetadata.getMetadataFormatNames();
            if(metadataFormatNames != null) {
                for(int j = 0; j < metadataFormatNames.length; j++) {
                    System.out.println("\n--- Stream metadata --- "+
                                       metadataFormatNames[j]+
                                       "\n");
                    IIOExampleUtils.printMetadata(streamMetadata,
                                                  metadataFormatNames[j]);
                }
            }
        }

        //
        // Loop over indices.
        //
        for(int imageIndex = firstIndex; imageIndex <= lastIndex; imageIndex++) {
            //
            // Read and print image metadata.
            //
            IIOMetadata imageMetadata = reader.getImageMetadata(imageIndex);
            if(imageMetadata != null) {
                String[] metadataFormatNames = imageMetadata.getMetadataFormatNames();
                if(metadataFormatNames != null) {
                    for(int j = 0; j < metadataFormatNames.length; j++) {
                        System.out.println("\n--- Image metadata --- "+
                                           metadataFormatNames[j]+
                                           "\n");
                        IIOExampleUtils.printMetadata(imageMetadata,
                                                      metadataFormatNames[j]);
                    }
                }
            }
        }
    }
}
