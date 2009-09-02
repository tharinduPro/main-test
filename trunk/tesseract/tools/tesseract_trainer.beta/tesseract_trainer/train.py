import os
import string

def train(lang):
    """Generates normproto, inttemp, Microfeat, unicharset and pffmtable"""
    output_dir=lang+"."+"training_data"
    dir=lang+"."+"images"+"/"
    
    if(os.path.exists(output_dir)):
        pass
    else:
        os.mkdir(output_dir)
   # os.chdir(output_dir)
    print "in train"
    for t in os.walk(dir):
        for f in t[2]:
            img=f.split('.')[0]
            print img
            image=img+".tif"
            box=img+".box"
            exec_string1="tesseract "+dir+image+" junk nobatch box.train"
            #print exec_string1
            exit_code1=os.system(exec_string1) #creation of tr files done
            
    #now begins clustering
    
    exec_string2="mftraining"
    exec_string3="cntraining"
    
    for t in os.walk(dir):
        for f in t[2]:
            if(f.split('.')[1]=="tr"):
                exec_string2+=" "+dir+f+" "
                exec_string3+=" "+dir+f+" "
    exit_code2=os.system(exec_string2)
    exit_code3=os.system(exec_string3)
    #clustering ends
    
    #unicharset_extractor begins
    exec_string4="unicharset_extractor"
    for t in os.walk(dir):
        for f in t[2]:
            if(f.split('.')[1]=="box"):
                exec_string4+=" "+dir+f
                #print exec_string4
    exit_code4=os.system(exec_string4)
    #unicharset_extractor ends
            
            
            
            
    