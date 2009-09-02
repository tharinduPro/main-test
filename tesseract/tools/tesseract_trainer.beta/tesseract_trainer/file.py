#This code reads alphabet characters from certain files and fills up an array so
#it can be used for generating test images

import os

def combine(frest,fc,fpresv,fpostsv):
    """ Creates all possible combinations of consonant+vowel"""
    all_comb=[]
    
    for rest in frest:
        rest=rest.strip()
        all_comb.append(rest)
    
    for c1 in fc:
        c=c1.strip() #remove special characters
        all_comb.append(c)
        for prev in fpresv:
            txt=prev.strip()+c
            all_comb.append(txt)
        for postv in fpostsv:
            txt=c+postv.strip()
            all_comb.append(txt)
    count=0
    for a in all_comb:
        print count,
        count+=1
        print a
        return all_comb



def read_file(alphabet_dir):
    """Reads input alphabet files from alphabet_dir"""
    print "in file.py"
    #file containing vowels
    if(os.path.exists(alphabet_dir+"consonants")):
        f=open(alphabet_dir+"consonants",'r')
        fc=f.readlines()
        f.close()
    else:
        fc=[]
        
    
    #file containing semivowels of the form consonant+semivowel	
    if(os.path.exists(alphabet_dir+"pre_semivowels")):
        f=open(alphabet_dir+"pre_semivowels",'r')
        fpresv=f.readlines()
        f.close()
    else:
        fpresv=[]
        

    #file containing semivowels of the form semivowel+consonant
    if(os.path.exists(alphabet_dir+"post_semivowels")):
        f=open(alphabet_dir+"post_semivowels",'r')
        fpostsv=f.readlines()
        f.close()
    else:
        fpostsv=[]
    
    #file containing everything else
    if(os.path.exists(alphabet_dir+"rest")):
        f=open(alphabet_dir+"rest",'r')
        frest=f.readlines()
        f.close()
    else:
        frest=[]
    return combine(frest,fc,fpresv,fpostsv)
