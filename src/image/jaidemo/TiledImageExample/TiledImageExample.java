package image.jaidemo.TiledImageExample;

/*
 * $RCSfile: TiledImageExample.java,v $
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
 * $Date: 2005/02/11 05:20:13 $
 * $State: Exp $
 */

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.*;


/**
 * Example of one way to use TiledImage to write graphics to a large image.
 * The image is read, written out, and every time the TiledImage is updated
 * the corresponding output file tile is replaced. This code illustrates
 * the use of the example class SelfCleaningTiledImage as well as pixel
 * replacement in an ImageWriter. The only implementation of pixel
 * replacement that is currently available (22 October 2004) is for
 * uncompressed TIFF files.
 *
 * Usage: java TiledImageExample inputRGBFile outputTIFFFile
 */
public class TiledImageExample implements TileObserver {
    // Size of tiles.
    private static int TILE_SIZE = 32;

    // Number of tiles between gc() calls.
    private static int MAX_TILES = 10;

    // TIFF writer.
    ImageWriter writer = null;

    public static void main(String[] args) throws Throwable {
        if(args.length < 2) {
            throw new IllegalArgumentException
                ("Usage: java TiledImageExample inputRGBFile outputTIFFFile");
        }
        new TiledImageExample(args[0], args[1]);
    }

    TiledImageExample(String inputFile, String outputFile) throws IOException {
        // Read the image. If the image is a tiled TIFF then 'image' is tiled.
        ImageInputStream input =
            ImageIO.createImageInputStream(new File(inputFile));
        ImageReader reader =
            (ImageReader)ImageIO.getImageReaders(input).next();
        reader.setInput(input);
        RenderedImage image = reader.readAsRenderedImage(0, null);

        // Set tile flag and dimensions.
        int tileWidth = TILE_SIZE;
        int tileHeight = TILE_SIZE;
        boolean isImageTiled =
            image.getNumXTiles() > 1 || image.getNumYTiles() > 1;
        if(isImageTiled) {
            tileWidth = image.getTileWidth();
            tileHeight = image.getTileHeight();
        }

        // Write the image to an uncompressed tiled TIFF file.
        writer =
            (ImageWriter)ImageIO.getImageWritersByFormatName("TIFF").next();
        ImageOutputStream output =
            ImageIO.createImageOutputStream(new File(outputFile));
        writer.setOutput(output);
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
        param.setTiling(tileWidth, tileHeight, 0, 0);
        writer.write(null,
                     new IIOImage(image, null, reader.getImageMetadata(0)),
                     param);

        // Read the image just written. This is necessary so that the
        // modified image backs itself with its own data. If the original
        // images backed the modified image then data could be reloaded from
        // the original image the second time a tile was edited thereby
        // destroying all previous edits.
        input = ImageIO.createImageInputStream(new File(outputFile));
        reader = (ImageReader)ImageIO.getImageReaders(input).next();
        reader.setInput(input);
        image = reader.readAsRenderedImage(0, null);

        // Create a TiledImage.
        SelfCleaningTiledImage ti =
            new SelfCleaningTiledImage(image, true, MAX_TILES);

        // Make this class know when tiles have been modified.
        ti.addTileObserver(this);

        // Get the graphics object.
        Graphics2D g2d = ti.createGraphics();

        // Overwrite each tile with the value at its center.
        int numTiles = 0;
        int numBands = image.getSampleModel().getNumBands();
        int y = ti.getMinY();
        for(int ty = 0; ty < ti.getNumYTiles(); ty++, y += tileHeight) {
            int x = ti.getMinX();
            for(int tx = 0; tx < ti.getNumXTiles(); tx++, x += tileWidth) {
                int r;
                int g;
                int b;
                if(numBands == 1) {
                    r = g = b =
                        ti.getTile(tx, ty).getSample(x + tileWidth/2,
                                                     y + tileHeight/2, 0);
                } else {
                    Raster tile = ti.getTile(tx, ty);
                    r = tile.getSample(x + tileWidth/2,
                                       y + tileHeight/2, 0);
                    g = tile.getSample(x + tileWidth/2,
                                       y + tileHeight/2, 1);
                    b = tile.getSample(x + tileWidth/2,
                                       y + tileHeight/2, 2);
                }
                g2d.setColor(new Color(r, g, b));
                g2d.fill(ti.getTileRect(tx, ty));
            }
        }

        // Replace center of center tile with red.
        Rectangle rect =
            ti.getTileRect(ti.getNumXTiles()/2, ti.getNumYTiles()/2);
        rect.x += rect.width/4;
        rect.y += rect.height/4;
        rect.width -= rect.width/2;
        rect.height -= rect.height/2;
        g2d.setColor(Color.red);
        g2d.fill(rect);
    }

    /**
     * Replaces tile on disk when <code>willBeWritable</code> is
     * <code>true</code>. This happens in the calls to <code>Graphics2D</code>
     * methods which write to the image.
     */
    public synchronized void tileUpdate(WritableRenderedImage source,
                                        int tileX, int tileY,
                                        boolean willBeWritable) {
        if(!willBeWritable) {
            Raster tile = source.getTile(tileX, tileY);
            try {
                writer.prepareReplacePixels(0, tile.getBounds());
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setSourceRegion(tile.getBounds());
                param.setDestinationOffset(new Point(tile.getMinX(),
                                                     tile.getMinY()));
                writer.replacePixels(source, param);
                writer.endReplacePixels();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
