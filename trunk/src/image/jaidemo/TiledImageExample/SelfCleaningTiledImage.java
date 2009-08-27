/*
 * $RCSfile: SelfCleaningTiledImage.java,v $
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

import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.HashSet;
import javax.media.jai.TiledImage;

/**
 * Sample code implementing a TiledImage which discards tiles after a
 * specified number have accumulated.
 */
public class SelfCleaningTiledImage extends TiledImage {
    private int maxTilesToStore;
    private HashSet tilesCreated = new HashSet();

    public SelfCleaningTiledImage(RenderedImage source,
                                  boolean areBuffersShared,
                                  int maxTilesToStore) {
        super(source, areBuffersShared);
        if(maxTilesToStore < 1) {
            throw new IllegalArgumentException("maxTilesToStore < 1");
        }
        this.maxTilesToStore = maxTilesToStore;
    }

    public SelfCleaningTiledImage(RenderedImage source,
                                  int tileWidth,
                                  int tileHeight,
                                  int maxTilesToStore) {
        super(source, tileWidth, tileHeight);
        if(maxTilesToStore < 1) {
            throw new IllegalArgumentException("maxTilesToStore < 1");
        }
        this.maxTilesToStore = maxTilesToStore;
    }

    /**
     * Invokes {@link #clearTiles()} if this tile has not been requested
     * already and <code>maxTilesToStore</code> other tiles have been
     * created and then returns the requested tile.
     *
     * @see {@link TiledImage#getWritableTile(int,int)}
     */
    public synchronized WritableRaster getWritableTile(int tileX, int tileY) {
        // Clear the tiles if this one not among those created already
        // and the number of tiles is at the maximum allowed.
        Point tileIndex = new Point(tileX, tileY);
        if(!tilesCreated.contains(tileIndex) &&
           tilesCreated.size() == maxTilesToStore) {
            clearTiles();
        }

        // Get the tile and save its index.
        WritableRaster tile = super.getWritableTile(tileX, tileY);
        if(tile != null) {
            tilesCreated.add(tileIndex);
        }

        return tile;
    }

    /**
     * Invokes {@link #clearTiles()} if this tile has not been requested
     * already and <code>maxTilesToStore</code> other tiles have been
     * created and then returns the requested tile.
     *
     * @see {@link TiledImage#getWritableTile(int,int)}
     */
    public synchronized Raster getTile(int tileX, int tileY) {
        // Clear the tiles if this one not among those created already
        // and the number of tiles is at the maximum allowed.
        Point tileIndex = new Point(tileX, tileY);
        if(!tilesCreated.contains(tileIndex) &&
           tilesCreated.size() == maxTilesToStore) {
            clearTiles();
        }

        // Get the tile and save its index.
        Raster tile = super.getTile(tileX, tileY);
        if(tile != null) {
            tilesCreated.add(tileIndex);
        }

        return tile;
    }

    /**
     * Sets all non-locked elements of {@link #tiles} to <code>null</code>.
     *
     * @throws IllegalStateException if <code>hasTileWriters()</code>
     * returns <code>true</code>.
     */
    public void clearTiles() {
        if(hasTileWriters()) {
            throw new IllegalStateException("Tile writers present.");
        }

        // Set elements of tiles array to null.
        int maxTileX = minTileX + tilesX;
        int maxTileY = minTileY + tilesY;
        for(int ty = minTileY; ty < maxTileY; ty++) {
            for(int tx = minTileX; tx < maxTileX; tx++) {
                if(!isTileLocked(tx, ty)) {
                    tiles[tx][ty] = null;
                    tilesCreated.remove(new Point(tx, ty));
                }
            }
        }
    }
}
