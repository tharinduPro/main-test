/*
 * $RCSfile: IIOExampleUtils.java,v $
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

import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

/**
 * This class contains static utility methods for example code.
 */
public class IIOExampleUtils {
    private static List tiffDataFields;

    static {
        tiffDataFields = Arrays.asList(new String[] {
            "TIFFBytes", "TIFFAsciis",
            "TIFFShorts", "TIFFSShorts", "TIFFLongs", "TIFFSLongs",
            "TIFFRationals", "TIFFSRationals",
            "TIFFFloats", "TIFFDoubles", "TIFFUndefined"});
    }

    /**
     * Compare two metadata trees and print the differences, if any.
     * @return whether the two trees are equal.
     */
    public static final boolean compareElements(IIOMetadataNode orig,
                                                IIOMetadataNode copy) {
        boolean areEqual = true;

        String nodeName = orig.getNodeName();

        if(nodeName.endsWith("PaletteEntry") ||
           nodeName.equals("PLTEEntry") ||
           nodeName.equals("hISTEntry") ||
           nodeName.equals("ColorTableEntry")) {
            // getAttribute() returns "" if attribute is not defined.
            if(!orig.getAttribute("index").equals(copy.getAttribute("index"))) {
                return false;
            }
        } else if(nodeName.equals("TextEntry") ||
                  nodeName.equals("tEXtEntry")) {
            if(!orig.getAttribute("keyword").equals(copy.getAttribute("keyword"))) {
                return false;
            }
        } else if(nodeName.equals("TIFFIFD")) {
            String parentTagNameOrig = orig.getAttribute("parentTagName");
            String parentTagNameCopy = copy.getAttribute("parentTagName");
            if(parentTagNameOrig == null ||
               parentTagNameCopy == null ||
               !parentTagNameOrig.equals(parentTagNameCopy)) {
                return false;
            }
        } else if(nodeName.equals("TIFFField")) {
            if(!orig.getAttribute("number").equals(copy.getAttribute("number"))) {
                return false;
            }
        }

        // Compare values.
        String origValue = orig.getNodeValue();
        if(origValue != null) {
            String copyValue = copy.getNodeValue();
            if(copyValue == null || !copyValue.equals(origValue)) {
                System.out.println("\""+nodeName+"\" values differ: "+
                                   origValue+" -> "+copyValue);
                areEqual = false;
            }
        }

        // Compare attributes.
        if(orig.hasAttributes()) {
            NamedNodeMap attrMapOrig = orig.getAttributes();
            NamedNodeMap attrMapCopy = copy.getAttributes();
            int numAttr = attrMapOrig.getLength();
            for(int i = 0; i < numAttr; i++) {
                Node attrOrig = attrMapOrig.item(i);
                String attrName = attrOrig.getNodeName();
                Node attrCopy = attrMapCopy.getNamedItem(attrName);
                if(attrCopy == null) {
                    System.out.println("\""+nodeName+"\" missing "+
                                       "\""+attrName+"\" attribute.");
                    areEqual = false;
                } else {
                    String attrValueOrig = attrOrig.getNodeValue();
                    String attrValueCopy = attrCopy.getNodeValue();
                    if(!attrValueCopy.equals(attrValueOrig)) {
                        System.out.println("\""+nodeName+"\" attribute "+
                                           "\""+attrName+"\" values differ: "+
                                           attrValueOrig+" -> "+attrValueCopy);
                        areEqual = false;
                    }
                }
            }
        }

        // Compare child nodes.
        if(orig.hasChildNodes()) {
            NodeList childNodesOrig = orig.getChildNodes();
            int numChildNodesOrig = childNodesOrig.getLength();
            if(copy.hasChildNodes()) {
                NodeList childNodesCopy = copy.getChildNodes();
                int numChildNodesCopy = childNodesCopy.getLength();
                if(tiffDataFields.contains(nodeName) || // TIFF data array
                   (nodeName.equals("Palette") &&
                    ((IIOMetadataNode)orig.getFirstChild()).getAttribute("index").equals(""))) { // BMP Palette
                    if(numChildNodesCopy != numChildNodesOrig) {
                        System.out.println("\""+nodeName+
                                           "\" does not contain "+
                                           "the same number of children.");
                        areEqual = false;
                    } else {
                        for(int i = 0; i < numChildNodesOrig; i++) {
                            Node childOrig = childNodesOrig.item(i);
                            Node childCopy = childNodesCopy.item(i);
                            compareElements((IIOMetadataNode)childOrig,
                                            (IIOMetadataNode)childCopy);
                        }
                    }
                } else {
                    for(int i = 0; i < numChildNodesOrig; i++) {
                        Node childOrig = childNodesOrig.item(i);
                        String childNameOrig = childOrig.getNodeName();
                        boolean foundChild = false;
                        for(int j = 0; j < numChildNodesCopy; j++) {
                            Node childCopy = childNodesCopy.item(j);
                            String childNameCopy = childCopy.getNodeName();
                            if(childNameCopy.equals(childNameOrig)) {
                                foundChild = true;
                                if(compareElements((IIOMetadataNode)childOrig,
                                                   (IIOMetadataNode)childCopy)) {
                                    break;
                                }
                            }
                        }
                        if(!foundChild) {
                            System.out.println("\""+nodeName+
                                               "\" does not contain \""+
                                               childNameOrig+"\" element");
                            areEqual = false;
                        }
                    }
                }
            } else {
                for(int i = 0; i < numChildNodesOrig; i++) {
                    Node childOrig = childNodesOrig.item(i);
                    String childNameOrig = childOrig.getNodeName();
                    System.out.println("\""+nodeName+"\" does not contain \""+
                                       childNameOrig+"\" element");
                    areEqual = false;
                }
            }
        }

        return areEqual;
    }

    public static boolean compareMetadata(IIOMetadata inMetadata,
                                          IIOMetadata outMetadata) {
        if(inMetadata == null || outMetadata == null) {
            System.out.println("inMetadata == null || outMetadata == null");
            return false;
        }

        // Cannot use real native metadata comparison in next line due to
        // bug in metadata tree comparison in compareElements().
        String format = outMetadata.getNativeMetadataFormatName();
        if(!Arrays.asList(inMetadata.getMetadataFormatNames()).contains(format)) {
            if(inMetadata.isStandardMetadataFormatSupported() &&
               outMetadata.isStandardMetadataFormatSupported()) {
                format = IIOMetadataFormatImpl.standardMetadataFormatName;
            } else {
                System.out.println("No common metadata format.");
                return false;
            }
        }

        System.out.println("format "+format);

        IIOMetadataNode inTree =
            (IIOMetadataNode)inMetadata.getAsTree(format);
        IIOMetadataNode outTree =
            (IIOMetadataNode)outMetadata.getAsTree(format);

        return IIOExampleUtils.compareElements(inTree, outTree);
    }

    private static final int INDENT = 2;

    private static final void indent(int level, int extra) {
        int indent = level*INDENT + extra;
        for(int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
    }

    public static void printMetadata(IIOMetadata metadata,
                                     String metadataFormat) {
        Node tree = metadata.getAsTree(metadataFormat);
        printNode(tree, 0);
    }

    public static void printNode(Node node, int level) {
        indent(level, 0);

        IIOMetadataNode iioNode = (IIOMetadataNode)node;
        System.out.print("<"+iioNode.getNodeName());

        NodeList children = iioNode.getChildNodes();
        int numChildren = children.getLength();

        NamedNodeMap attributes = iioNode.getAttributes();
        int numAttributes = attributes.getLength();
        for(int i = 0; i < numAttributes; i++) {
            Node attribute = attributes.item(i);
            if(i > 0) {
                System.out.println("");
                indent(level, iioNode.getNodeName().length()+1);
            }
            System.out.print(" "+attribute.getNodeName()+"=");
            System.out.print("\""+attribute.getNodeValue()+"\"");
        }

        String nodeValue = iioNode.getNodeValue();

        if(numChildren == 0 && nodeValue == null) {
            System.out.println("/>");
        } else {
            System.out.println(">");
        }

        if(nodeValue != null) {
            indent(level, INDENT);
            System.out.println(nodeValue);
        }

        for(int i = 0; i < numChildren; i++) {
            printNode(children.item(i), level + 1);
        }

        if(numChildren > 0 || nodeValue != null) {
            indent(level, 0);
            System.out.println("</"+iioNode.getNodeName()+">");
        }
    }

    /**
     * Compares two images and returns a String describing any difference.
     * @return <code>"Images are identical"</code> if the images are
     * identical and a string starting with <code>"IMAGES DIFFER"</code> if
     * they are not.
     */
    public static String compareImages(RenderedImage image1,
                                       RenderedImage image2) {
        // Check dimensions.
        if(image1.getWidth() != image2.getWidth()) {
            return "IMAGES DIFFER IN WIDTH: "+
                image1.getWidth()+" -> "+image2.getWidth();
        }
        if(image1.getHeight() != image2.getHeight()) {
            return "IMAGES DIFFER IN HEIGHT: "+
                image1.getHeight()+" -> "+image2.getHeight();
        }

        // Check data type.
        SampleModel sm1 = image1.getSampleModel();
        SampleModel sm2 = image2.getSampleModel();
        if(sm1.getDataType() != sm2.getDataType()) {
            System.out.print("(WARNING: DATA TYPE: "+
                             sm1.getDataType()+" -> "+sm2.getDataType()+")");
        }

        // Check band samples per pixel.
        if(sm1.getNumBands() != sm2.getNumBands()) {
            return "IMAGES DIFFER IN NUMBER OF SAMPLES PER PIXEL: "+
                sm1.getNumBands()+" -> "+sm2.getNumBands();
        }

        // Check bits per sample.
        int[] bitsPerSample1 = sm1.getSampleSize();
        int[] bitsPerSample2 = sm2.getSampleSize();
        boolean differentBitsPerSample = false;
        String bandList = "";
        for(int i = 0; i < bitsPerSample1.length; i++) {
            if(bitsPerSample1[i] != bitsPerSample2[i]) {
                bandList += " "+i;
                differentBitsPerSample = true;
            }
        }
        if(differentBitsPerSample) {
            System.out.print("(WARNING: BITS PER SAMPLE FOR BAND(S) "+
                             bandList+") ");
        }

        // Subtract the images.
        PlanarImage subtract =
            (sm1.getDataType() == DataBuffer.TYPE_FLOAT ||
             sm2.getDataType() == DataBuffer.TYPE_FLOAT) ?
            JAI.create("subtract", image1, image2) :
            JAI.create("subtract",
                       JAI.create("format",
                                  image1,
                                  DataBuffer.TYPE_SHORT),
                       JAI.create("format",
                                  image2,
                                  DataBuffer.TYPE_SHORT));

        // Take the absolute value of the difference clamped to byte range.
        PlanarImage abs = JAI.create("format",
                                     JAI.create("absolute",
                                                subtract),
                                     DataBuffer.TYPE_BYTE);

        // Find the extrema of the absolute difference.
        PlanarImage extrema = JAI.create("extrema", abs);

        Object maxProp = extrema.getProperty("maximum");
        if(maxProp == null ||
           maxProp.equals(java.awt.Image.UndefinedProperty)) {
            throw new RuntimeException("\"MAXIMUM\" PROPERTY IS UNAVAILABLE!");
        } else {
            double[] maximum = (double[])maxProp;
            double maxTotal = 0.0;
            String maxString = "";
            for(int maxBand = 0; maxBand < maximum.length; maxBand++) {
                double maxVal = maximum[maxBand];
                maxString += maxVal+" ";
                maxTotal += maxVal;
            }
            if(maxTotal != 0.0) {
                return "IMAGES DIFFER IN VALUE: "+maxString;
            }
        }

        return "Images are identical";
    }

}
