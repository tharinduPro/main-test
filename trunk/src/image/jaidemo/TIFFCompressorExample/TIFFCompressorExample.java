/*
 * $RCSfile: TIFFCompressorExample.java,v $
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
 * $Date: 2005/02/11 05:20:12 $
 * $State: Exp $
 */

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import com.sun.media.imageio.plugins.tiff.*;

/**
 * Illustrates the mechanics of the pluggable TIFF compressor/decompressor
 * structure.
 *
 * Usage: java TIFFCompressorExample inFile outFile
 */
public class TIFFCompressorExample extends Frame {
    public static void main(String[] args) throws Throwable {
        if(args.length < 2) {
            throw new IllegalArgumentException
                ("Usage: java TIFFCompressorExample inFile outFile");
        }
        new TIFFCompressorExample(args[0], args[1]);
    }

    TIFFCompressorExample(String inPath, String outPath) throws Throwable {
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    setEnabled(false);
                    dispose();
                }
            });

        // Read the input file.
        RenderedImage src = ImageIO.read(new File(inPath));


        // Get a TIFF writer and set its output.
        Iterator writers = ImageIO.getImageWritersByFormatName("tiff");
        if(writers == null || !writers.hasNext()) {
            throw new RuntimeException("No writers for "+outPath);
        }
        ImageWriter writer = (ImageWriter)writers.next();
        ImageOutputStream output =
            ImageIO.createImageOutputStream(new File(outPath));
        writer.setOutput(output);

        // Create the write param.
        TIFFImageWriteParam writeParam = new TIFFImageWriteParam(null);
        TIFFCompressor compressor = new TIFFNotCompressor();
        writeParam.setCompressionMode(writeParam.MODE_EXPLICIT);
        writeParam.setTIFFCompressor(compressor);
        writeParam.setCompressionType(compressor.getCompressionType());

        // Write the image.
        writer.write(null, new IIOImage(src, null, null), writeParam);

        // Get a TIFF reader and set its input to the written TIFF image.
        Iterator readers = ImageIO.getImageReadersByFormatName("tiff");
        if(readers == null || !readers.hasNext()) {
            throw new RuntimeException("No readers for "+outPath);
        }
        ImageReader reader = (ImageReader)readers.next();
        ImageInputStream input =
            ImageIO.createImageInputStream(new File(outPath));
        reader.setInput(input);

        // Create the read param.
        TIFFImageReadParam readParam = new TIFFImageReadParam();
        readParam.setTIFFDecompressor(new TIFFNotDecompressor());

        // Read the image.
        RenderedImage image = reader.read(0, readParam);

        // Display the image.
        add(new ImCanvas(image));
        setLocation(100, 100);
        pack();
        setVisible(true);
    }

    /**
     * A compressor which merely "nots" the bytes.
     */
    class TIFFNotCompressor extends TIFFCompressor {
        TIFFNotCompressor() {
            super("Not", 86, true);
        }

        public int encode(byte[] b, int off,
                          int width, int height,
                          int[] bitsPerSample,
                          int scanlineStride) throws IOException {
            byte[] buf = new byte[width];
            for(int j = 0; j < height; j++) {
                int k = off;
                for(int i = 0; i < width; i++) {
                    buf[i] = (byte)(~b[k++]);
                }
                stream.write(buf);
                off += scanlineStride;
            }

            return width*height;
        }
    }

    /**
     * A decompressor which merely "nots" the bytes.
     */
    class TIFFNotDecompressor extends TIFFDecompressor {
        public void decodeRaw(byte[] b,
                              int dstOffset,
                              int bitsPerPixel,
                              int scanlineStride) throws IOException {
            stream.seek(offset);

            byte[] buf = new byte[srcWidth];
            for(int j = 0; j < srcHeight; j++) {
                stream.read(buf);
                int k = dstOffset;
                for(int i = 0; i < srcWidth; i++) {
                    b[k++] = (byte)(~buf[i]);
                }
                dstOffset += scanlineStride;
            }
        }
    }
}
