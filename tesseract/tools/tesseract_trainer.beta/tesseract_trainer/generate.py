#!/usr/local/bin/python
#-*- coding:utf8 -*-
#This code generates the training files for tesseract-ocr for bootstrapping a new character set
import file
import train
import os
import sys

import ImageFont, ImageDraw
from PIL import Image


def draw(lang,font_name,font,fsz,alphabets): # language, font file name, font full path, font size, characters
    """ Generates tif images and box files"""
    x=0; y=0
    
    image_dir=lang+"."+"images"
    if(os.path.exists(image_dir)):
        pass
    else:
        os.mkdir(image_dir)
        
    im = Image.new("RGB",(fsz*20,fsz*2),"white")
    #im.show()
    # use a truetype font
    font= ImageFont.truetype(font,fsz)
    filecount=0 #for different filenames for the images
    filename=image_dir+"/"+"image"+font_name.split('.')[0]+str(filecount)+".tif"
    boxfile=image_dir+"/"+"image"+font_name.split('.')[0]+str(filecount)+".box"
    f=open(boxfile,"w")
     
    
    for akshar in alphabets:
        #print akshar
        big_image=ImageDraw.Draw(im)
        sub_im=Image.new("RGB",(fsz*2,fsz*1.5),"white") #first create a small image for just one set of characters, will later paste it onto a larger image im
        draw = ImageDraw.Draw(sub_im)
        draw.text((0,0), unicode(akshar,'UTF-8'), font=font,fill=0) #draw the aksharas
        bbox_sub=sub_im.getbbox() #get the bounding box of the black pixels relative to the small image
        box_im=(x,y,x+fsz*2,y+fsz*1.5)
        bbox_im=(x+bbox_sub[0],y+bbox_sub[1],x+bbox_sub[2],y+bbox_sub[3]) #calculate relative to the big image.
        #the lines below create the box files
        line=akshar+" "+str(bbox_im[0])+" "+str(bbox_im[1])+" "+str(bbox_im[2])+" "+str(bbox_im[3]) #the extra "1"s are to make the boxes a little bigger than the characters themselves.
        f.write(line)
        f.write("\n")
        
        print bbox_im
        #draw.rectangle((0,0,150,70)) #draw the bounding box
        im.paste(sub_im,box_im)
        #big_image.rectangle(bbox_im) #draw the bounding box
        x+=fsz*3 #fsz*(size of the character(S))
        
        if x>900:
        #create next strip
            x=0; y=0
            im.save(filename,"TIFF")#save the strip to a file
            print filename
            filecount+=1 # give a new name to the file to be created
            im = Image.new("RGB",(fsz*20,fsz*1.5),"white") #create a new image
            filename=image_dir+"/"+"image"+font_name.split('.')[0]+str(filecount)+".tif"
            boxfile=image_dir+"/"+"image"+font_name.split('.')[0]+str(filecount)+".box"
            f.close()
            f=open(boxfile,"w") #open new file
            
if(len(sys.argv)!=7):
    print "Usage: python generate.py -fd <font directory> -l <language> -a <input alphabet directory>"
    exit()

if(sys.argv[1]=="-fd"):
    font_dir=sys.argv[2]
else:
    print "Usage: python generate.py -fd <font directory> -l <language> -a <imput alphabet directory>"
    exit()
        
if(sys.argv[3]=="-l"):
    lang=sys.argv[4]
else:
    print "Usage: python generate.py -fd <font directory> -l <language> -a <input alphabet directory>"
    exit()
    
if(sys.argv[5]=="-a"):
    alphabet_dir=sys.argv[6]
else:
    print "Usage: python generate.py -fd <font directory> -l <language> -a <input alphabet directory>"
    exit()
    


       


#begin training    
#font_dir="/usr/share/fonts/truetype/ttf-bengali-fonts/"
for t in os.walk(font_dir):
        for f in t[2]:
            if(f.split('.')[1]=="ttf"):
                font_file=font_dir+f
                print font_file
                draw(lang,f,font_file,40,file.read_file(alphabet_dir))#reads all fonts in the directory font_dir and trains

train.train(lang)
#training ends
