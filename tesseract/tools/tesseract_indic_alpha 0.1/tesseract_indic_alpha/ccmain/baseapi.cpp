/**********************************************************************
 * File:        baseapi.cpp
 * Description: Simple API for calling tesseract.
 * Author:      Ray Smith
 * Created:     Fri Oct 06 15:35:01 PDT 2006
 *
 * (C) Copyright 2006, Google Inc.
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 ** http://www.apache.org/licenses/LICENSE-2.0
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 *
 **********************************************************************/

#include "baseapi.h"
#include <iostream>
#include <math.h>


using namespace std;

#define min(a, b)  (((a) < (b)) ? (a) : (b)) 
#define max(a, b)  (((a) > (b)) ? (a) : (b))

// Include automatically generated configuration file if running autoconf.
#ifdef HAVE_CONFIG_H
#include "config_auto.h"
#endif

#ifdef HAVE_LIBLEPT
// Include leptonica library only if autoconf (or makefile etc) tell us to.
#include "allheaders.h"
#endif

#include "tessedit.h"
#include "ocrclass.h"
#include "pageres.h"
#include "tessvars.h"
#include "control.h"
#include "applybox.h"
#include "pgedit.h"
#include "varabled.h"
#include "variables.h"
#include "output.h"
#include "globals.h"
#include "adaptmatch.h"
#include "edgblob.h"
#include "tessbox.h"
#include "tordvars.h"
#include "imgs.h"
#include "makerow.h"
#include "tstruct.h"
#include "tessout.h"
#include "tface.h"
#include "permute.h"

BOOL_VAR(tessedit_resegment_from_boxes, FALSE,
         "Take segmentation and labeling from box file");
BOOL_VAR(tessedit_train_from_boxes, FALSE,
         "Generate training data from boxed chars");

// Minimum sensible image size to be worth running tesseract.
const int kMinRectSize = 10;

extern bool connected_script;

static STRING input_file = "noname.tif";

// Set the value of an internal "variable" (of either old or new types).
// Supply the name of the variable and the value as a string, just as
// you would in a config file.
// Returns false if the name lookup failed.
bool TessBaseAPI::SetVariable(const char* variable, const char* value) {
  if (set_new_style_variable(variable, value))
    return true;
  return set_old_style_variable(variable, value);
}

void TessBaseAPI::SimpleInit(const char* datapath,
                             const char* language,
                             bool numeric_mode) {
  InitWithLanguage(datapath, NULL, language, NULL, numeric_mode, 0, NULL);
}

// Start tesseract.
// The datapath must be the name of the data directory or some other file
// in which the data directory resides (for instance argv[0].)
// The configfile is the name of a file in the tessconfigs directory
// (eg batch) or NULL to run on defaults.
// Outputbase may also be NULL, and is the basename of various output files.
// If the output of any of these files is enabled, then a name nmust be given.
// If numeric_mode is true, only possible digits and roman numbers are
// returned. Returns 0 if successful. Crashes if not.
// The argc and argv may be 0 and NULL respectively. They are used for
// providing config files for debug/display purposes.
// TODO(rays) get the facts straight. Is it OK to call
// it more than once? Make it properly check for errors and return them.
int TessBaseAPI::Init(const char* datapath, const char* outputbase,
                              const char* configfile, bool numeric_mode,
                              int argc, char* argv[]) {
  return InitWithLanguage(datapath, outputbase, NULL, configfile,
                          numeric_mode, argc, argv);
}

// Start tesseract.
// Similar to Init() except that it is possible to specify the language.
// Language is the code of the language for which the data will be loaded.
// (Codes follow ISO 639-3.) If it is NULL, english (eng) will be loaded.
int TessBaseAPI::InitWithLanguage(const char* datapath, const char* outputbase,
                              const char* language, const char* configfile,
                              bool numeric_mode, int argc, char* argv[]) {
  int result = init_tesseract(datapath, outputbase, language,
      configfile, argc, argv);

  bln_numericmode.set_value(numeric_mode);
  return result;
}

// Init the lang model component of Tesseract
int TessBaseAPI::InitLangMod(const char* datapath, const char* outputbase,
                                const char* language, const char* configfile,
                                bool numeric_mode, int argc, char* argv[]) {
  return init_tesseract_lm(datapath, outputbase, language,
      configfile, argc, argv);
}

// Set the name of the input file. Needed only for training and
// loading a UNLV zone file.
void TessBaseAPI::SetInputName(const char* name) {
  input_file = name;
}

// Recognize a rectangle from an image and return the result as a string.
// May be called many times for a single Init.
// Currently has no error checking.
// Greyscale of 8 and color of 24 or 32 bits per pixel may be given.
// Palette color images will not work properly and must be converted to
// 24 bit.
// Binary images of 1 bit per pixel may also be given but they must be
// byte packed with the MSB of the first byte being the first pixel, and a
// one pixel is WHITE. For binary images set bytes_per_pixel=0.
// The recognized text is returned as a char* which (in future will be coded
// as UTF8 and) must be freed with the delete [] operator.
char* TessBaseAPI::TesseractRect(const unsigned char* imagedata,
                                 int bytes_per_pixel,
                                 int bytes_per_line,
                                 int left, int top,
                                 int width, int height) {
  if (width < kMinRectSize || height < kMinRectSize)
    return NULL;  // Nothing worth doing.

  // Copy/Threshold the image to the tesseract global page_image.
  CopyImageToTesseract(imagedata, bytes_per_pixel, bytes_per_line,
                       left, top, width, height);

  return RecognizeToString();
}

// As TesseractRect but produces a box file as output.
char* TessBaseAPI::TesseractRectBoxes(const unsigned char* imagedata,
                                      int bytes_per_pixel,
                                      int bytes_per_line,
                                      int left, int top,
                                      int width, int height,
                                      int imageheight) {
  if (width < kMinRectSize || height < kMinRectSize)
  return NULL;  // Nothing worth doing.

  // Copy/Threshold the image to the tesseract global page_image.
  CopyImageToTesseract(imagedata, bytes_per_pixel, bytes_per_line,
                       left, top, width, height);

  BLOCK_LIST    block_list;

  FindLines(&block_list);

  // Now run the main recognition.
  PAGE_RES* page_res = Recognize(&block_list, NULL);

  return TesseractToBoxText(page_res, left, imageheight - (top + height));
}

char* TessBaseAPI::TesseractRectUNLV(const unsigned char* imagedata,
                                     int bytes_per_pixel,
                                     int bytes_per_line,
                                     int left, int top,
                                     int width, int height) {
  if (width < kMinRectSize || height < kMinRectSize)
    return NULL;  // Nothing worth doing.

  // Copy/Threshold the image to the tesseract global page_image.
  CopyImageToTesseract(imagedata, bytes_per_pixel, bytes_per_line,
                       left, top, width, height);

  BLOCK_LIST    block_list;

  FindLines(&block_list);

  // Now run the main recognition.
  PAGE_RES* page_res = Recognize(&block_list, NULL);

  return TesseractToUNLV(page_res);
}

// Call between pages or documents etc to free up memory and forget
// adaptive data.
void TessBaseAPI::ClearAdaptiveClassifier() {
  ResetAdaptiveClassifier();
}

// Close down tesseract and free up memory.
void TessBaseAPI::End() {
  ResetAdaptiveClassifier();
  end_tesseract();
}

// Dump the internal binary image to a PGM file.
void TessBaseAPI::DumpPGM(const char* filename) {
  IMAGELINE line;
  line.init(page_image.get_xsize());
  FILE *fp = fopen(filename, "w");
  fprintf(fp, "P5 " INT32FORMAT " " INT32FORMAT " 255\n", page_image.get_xsize(),
          page_image.get_ysize());
  for (int j = page_image.get_ysize()-1; j >= 0 ; --j) {
    page_image.get_line(0, j, page_image.get_xsize(), &line, 0);
    for (int i = 0; i < page_image.get_xsize(); ++i) {
      uinT8 b = line.pixels[i] ? 255 : 0;
      fwrite(&b, 1, 1, fp);
    }
  }
  fclose(fp);
}

#ifdef HAVE_LIBLEPT
// ONLY available if you have Leptonica installed.
// Get a copy of the thresholded global image from Tesseract.
Pix* TessBaseAPI::GetTesseractImage() {
  return page_image.ToPix();
}
#endif  // HAVE_LIBLEPT

// Copy the given image rectangle to Tesseract, with adaptive thresholding
// if the image is not already binary.
void TessBaseAPI::CopyImageToTesseract(const unsigned char* imagedata,
                                       int bytes_per_pixel,
                                       int bytes_per_line,
                                       int left, int top,
                                       int width, int height) {
  if (bytes_per_pixel > 0) {
    // Threshold grey or color.
    int* thresholds = new int[bytes_per_pixel];
    int* hi_values = new int[bytes_per_pixel];

    // Compute the thresholds.
    OtsuThreshold(imagedata, bytes_per_pixel, bytes_per_line,
                  left, top, left + width, top + height,
                  thresholds, hi_values);

    // Threshold the image to the tesseract global page_image.
    ThresholdRect(imagedata, bytes_per_pixel, bytes_per_line,
                  left, top, width, height,
                  thresholds, hi_values);

    delete [] thresholds;
    delete [] hi_values;
  } else {
    CopyBinaryRect(imagedata, bytes_per_line, left, top, width, height);
  }
}

// Compute the Otsu threshold(s) for the given image rectangle, making one
// for each channel. Each channel is always one byte per pixel.
// Returns an array of threshold values and an array of hi_values, such
// that a pixel value >threshold[channel] is considered foreground if
// hi_values[channel] is 0 or background if 1. A hi_value of -1 indicates
// that there is no apparent foreground. At least one hi_value will not be -1.
// thresholds and hi_values are assumed to be of bytes_per_pixel size.
void TessBaseAPI::OtsuThreshold(const unsigned char* imagedata,
                                int bytes_per_pixel,
                                int bytes_per_line,
                                int left, int top, int right, int bottom,
                                int* thresholds,
                                int* hi_values) {
  // Of all channels with no good hi_value, keep the best so we can always
  // produce at least one answer.
  int best_hi_value = 0;
  int best_hi_index = 0;
  bool any_good_hivalue = false;
  double best_hi_dist = 0.0;

  for (int ch = 0; ch < bytes_per_pixel; ++ch) {
    thresholds[ch] = 0;
    hi_values[ch] = -1;
    // Compute the histogram of the image rectangle.
    int histogram[256];
    HistogramRect(imagedata + ch, bytes_per_pixel, bytes_per_line,
                  left, top, right, bottom, histogram);
    int H;
    int best_omega_0;
    int best_t = OtsuStats(histogram, &H, &best_omega_0);
    if (best_omega_0 == 0 || best_omega_0 == H) {
       // This channel is empty.
       continue;
     }
    // To be a convincing foreground we must have a small fraction of H
    // or to be a convincing background we must have a large fraction of H.
    // In between we assume this channel contains no thresholding information.
    int hi_value = best_omega_0 < H * 0.5;
    thresholds[ch] = best_t;
    if (best_omega_0 > H * 0.75) {
      any_good_hivalue = true;
      hi_values[ch] = 0;
    }
    else if (best_omega_0 < H * 0.25) {
      any_good_hivalue = true;
      hi_values[ch] = 1;
    }
    else {
      // In case all channels are like this, keep the best of the bad lot.
      double hi_dist = hi_value ? (H - best_omega_0) : best_omega_0;
      if (hi_dist > best_hi_dist) {
        best_hi_dist = hi_dist;
        best_hi_value = hi_value;
        best_hi_index = ch;
      }
    }
  }
  if (!any_good_hivalue) {
    // Use the best of the ones that were not good enough.
    hi_values[best_hi_index] = best_hi_value;
  }
}

// Compute the histogram for the given image rectangle, and the given
// channel. (Channel pointed to by imagedata.) Each channel is always
// one byte per pixel.
// Bytes per pixel is used to skip channels not being
// counted with this call in a multi-channel (pixel-major) image.
// Histogram is always a 256 element array to count occurrences of
// each pixel value.
void TessBaseAPI::HistogramRect(const unsigned char* imagedata,
                                int bytes_per_pixel,
                                int bytes_per_line,
                                int left, int top, int right, int bottom,
                                int* histogram) {
  int width = right - left;
  memset(histogram, 0, sizeof(*histogram) * 256);
  const unsigned char* pixels = imagedata +
                                top*bytes_per_line +
                                left*bytes_per_pixel;
  for (int y = top; y < bottom; ++y) {
    for (int x = 0; x < width; ++x) {
      ++histogram[pixels[x * bytes_per_pixel]];
    }
    pixels += bytes_per_line;
  }

}

// Compute the Otsu threshold(s) for the given histogram.
// Also returns H = total count in histogram, and
// omega0 = count of histogram below threshold.
int TessBaseAPI::OtsuStats(const int* histogram,
                           int* H_out,
                           int* omega0_out) {
  int H = 0;
  double mu_T = 0.0;
  for (int i = 0; i < 256; ++i) {
    H += histogram[i];
    mu_T += i * histogram[i];
  }

  // Now maximize sig_sq_B over t.
  // http://www.ctie.monash.edu.au/hargreave/Cornall_Terry_328.pdf
  int best_t = -1;
  int omega_0, omega_1;
  int best_omega_0 = 0;
  double best_sig_sq_B = 0.0;
  double mu_0, mu_1, mu_t;
  omega_0 = 0;
  mu_t = 0.0;
  for (int t = 0; t < 255; ++t) {
    omega_0 += histogram[t];
    mu_t += t * static_cast<double>(histogram[t]);
    if (omega_0 == 0)
      continue;
    omega_1 = H - omega_0;
    mu_0 = mu_t / omega_0;
    mu_1 = (mu_T - mu_t) / omega_1;
    double sig_sq_B = mu_1 - mu_0;
    sig_sq_B *= sig_sq_B * omega_0 * omega_1;
    if (best_t < 0 || sig_sq_B > best_sig_sq_B) {
      best_sig_sq_B = sig_sq_B;
      best_t = t;
      best_omega_0 = omega_0;
    }
  }
  if (H_out != NULL) *H_out = H;
  if (omega0_out != NULL) *omega0_out = best_omega_0;
  return best_t;
}


////////////DEBAYAN//Deskew begins//////////////////////
void deskew(float angle,int srcheight, int srcwidth)
{
//angle=4;        //45° for example 
IMAGE tempimage;


IMAGELINE line;
//Convert degrees to radians 
float radians=(2*3.1416*angle)/360; 

float cosine=(float)cos(radians); 
float sine=(float)sin(radians); 

float Point1x=(srcheight*sine); 
float Point1y=(srcheight*cosine); 
float Point2x=(srcwidth*cosine-srcheight*sine); 
float Point2y=(srcheight*cosine+srcwidth*sine); 
float Point3x=(srcwidth*cosine); 
float Point3y=(srcwidth*sine); 

float minx=min(0,min(Point1x,min(Point2x,Point3x))); 
float miny=min(0,min(Point1y,min(Point2y,Point3y))); 
float maxx=max(Point1x,max(Point2x,Point3x)); 
float maxy=max(Point1y,max(Point2y,Point3y)); 

int DestWidth=(int)ceil(fabs(maxx)-minx); 
int DestHeight=(int)ceil(fabs(maxy)-miny); 

tempimage.create(DestWidth,DestHeight,1);
line.init(DestWidth);

for(int i=0;i<DestWidth;i++){ //A white line of length=DestWidth
line.pixels[i]=1;
}

for(int y=0;y<DestHeight;y++){ //Fill the Destination image with white, else clipmatra wont work
tempimage.put_line(0,y,DestWidth,&line,0);
}
line.init(DestWidth);



for(int y=0;y<DestHeight;y++) //Start filling the destination image pixels with corresponding source image pixels
{ 
  for(int x=0;x<DestWidth;x++) 
  { 
    int Srcx=(int)((x+minx)*cosine+(y+miny)*sine); 
    int Srcy=(int)((y+miny)*cosine-(x+minx)*sine); 
    if(Srcx>=0&&Srcx<srcwidth&&Srcy>=0&& 
         Srcy<srcheight) 
    { 
      line.pixels[x]= 
          page_image.pixel(Srcx,Srcy); 
    } 
  } 
   tempimage.put_line(0,y,DestWidth,&line,0);	
} 
 
//tempimage.write("tempimage.tif");
page_image=tempimage;//Copy deskewed image to global page image, so it can be worked on further
tempimage.destroy(); 
//page_image.write("page_image.tif");

}
/////////////DEBAYAN//Deskew ends/////////////////////

////////////DEBAYAN//Find skew begins/////////////////
float findskew(int height, int width)
{
int topx=0,topy=0,sign,count=0,offset=1,ifcounter=0;
float slope=-999,avg=0;
IMAGELINE line;
line.init(1);
line.pixels[0]=0;
///////Find the top most point of the page: begins///////////
for(int y=height-1;y>0;y--){  
  for(int x=width-1;x>0;x--){
    if(page_image.pixel(x,y)==0){
      topx=x;topy=y;
      break;
    }
    
  }  
  
  if(topx>0){break;};     
}
///////Find the top most point of the page: ends///////////


///////To find pages with no skew: begins//////////////
int c1,c2=0;
for(int x=1;x<.25*width;x++){
  while(page_image.pixel((width/2)+x,c1++)==1){ }
  while(page_image.pixel((width/2)-x,c2++)==1){ }
  if(c1==c2){cout<<"0 ANGLE\n";return (0);}
  c1=c2=0;
}
///////To find pages with no skew: ends//////////////

cout<<"width="<<width;
if(topx>0 && topx<.5*width){
  sign=1;
}
if(topx>0 && topx>.5*width){
  sign=-1;
}


if(sign==-1){
  while((topx-offset)>width/2){  
    while(page_image.pixel(topx-offset,topy-count)==1){
    //page_image.put_line(topx-offset,topy-count,1,&line,0);
    count++;
    }
    
    if((180/3.142)*atan((float)count/offset)<10){
    slope=(float)count/offset;
    ifcounter++;
    avg=(avg+slope);
    }
    count=0;
    offset++;
  }
    avg=(float)avg/ifcounter;
    //cout<<"avg="<<avg<<"\n";
    page_image.write("findskew.tif");
    //cout<<"(180/3.142)*atan((float)(count/offset)="<<(180/3.142)*atan(avg)<<"\n";
    return (sign*(180/3.142)*atan(avg));

}
if(sign==1){
  while((topx+offset)<width/2){  
    while(page_image.pixel(topx+offset,topy-count)==1){
    //page_image.put_line(topx+offset,topy-count,1,&line,0);
    count++;
    }
    
    if((180/3.142)*atan((float)count/offset)<10){
    slope=(float)count/offset;
    ifcounter++;
    avg=(avg+slope);
    }
    count=0;
    offset++;
  }
    avg=(float)avg/ifcounter;
    //cout<<"avg="<<avg<<"\n";
    page_image.write("findskew.tif");
    //cout<<"(180/3.142)*atan((float)(count/offset)="<<(180/3.142)*atan(avg)<<"\n";
    return (sign*(180/3.142)*atan(avg));

}

if(sign==0)
{return 0;}
cout<<"SHIT";
return (0);
}
////////////DEBAYAN//Find skew ends///////////////////

//Works on the global image page containing devnagri script.
//Clips the maatraas and then makes the global image ready for the Tesseract engine.
//Will be executed for all images during training, but during normal operation, will be
//used only if the language belongs to devnagri, eg, ben, hin etc.
void TessBaseAPI::ClipMaatraa(int height, int width)
{
IMAGELINE line;
line.init(width);
int count,count1=0,blackpixels[height-1][2],arr_row=0,maxbp=0,maxy=0,matras[100][3],char_height;
//cout<<"Connected Script="<<connected_script<<"\n";
	
	for(int y=0; y<height-1;y++){
	  count=0;	  
	  for(int x=0;x<width-1;x++){
            if(page_image.pixel(x,y)==0)
	      {count++;}
          }
          
	  if(count>=.05*width){
	    
            blackpixels[arr_row][0]=y;
            blackpixels[arr_row][1]=count;
	    arr_row++;
	  }
	}
	blackpixels[arr_row][0]=blackpixels[arr_row][1]='\0';
	
	for(int x=0;x<width-1;x++){  //Black Line
              line.pixels[x]=0;
        }

	////////////line_through_matra() begins//////////////////////
	count=1; 
	//cout<<"\nHeight="<<height<<" arr_row="<<arr_row<<"\n";
	char_height=blackpixels[0][0]; //max character height per sentence
	//cout<<"Char Height Init="<<char_height;
	while(count<=arr_row){
          //if(count==0){max=blackpixels[count][0];}
	  if((blackpixels[count][0]-blackpixels[count-1][0]==1) && (blackpixels[count][1]>=maxbp)){
            maxbp=blackpixels[count][1];
	    maxy=blackpixels[count][0];
	    //cout<<"\nMax="<<maxy<<" bpc="<<maxbp;
	  }
          
          if((blackpixels[count][0]-blackpixels[count-1][0])!=1){
            /////////////drawline(max)//////////////////////
            
        //      cout<<"\nmax="<<maxy<<" bpc="<<maxbp;
	//      page_image.put_line(0,maxy,width,&line,0);

	      char_height=blackpixels[count-1][0]-char_height;
              matras[count1][0]=maxy; matras[count1][1]=maxbp; matras[count1][2]=char_height; count1++;
	      char_height=blackpixels[count][0];
	      
            //////////// drawline(max)/////////////////////
            maxbp=blackpixels[count][1];
          } 
	  count++;
        }
	matras[count1][0]=matras[count1][1]=matras[count1][2]='\0';
	
	//delete blackpixels;	
	////////////line_through_matra() ends//////////////////////
        
        ////////////clip_matras() begins///////////////////////////
        for(int i=0;i<100;i++){ //where 100=max number of sentences per page
	  if(matras[i][0]=='\0'){break;}
	  //cout<<"\nY="<<matras[i][0]<<" bpc="<<matras[i][1]<<" chheight="<<matras[i][2];
	  count=i;
	}
	
	for(int i=0;i<=count;i++){
	  
	  for(int x=0;x<width-1;x++){
	    if(page_image.pixel(x,matras[i][0])==0){
 	      count1=0;	    
	      for(int y=0;y<matras[i][2];y++){
  	        if(page_image.pixel(x,matras[i][0]-y)==1){count1++;
                }
	       }
	      //cout<<"\nWPR @ "<<x<<","<<matras[i][0]<<"="<<count1;  
	      if(count1>.8*matras[i][2]){
	        line.init(matras[i][2]+5);
	        for(int j=0;j<matras[i][2]+5;j++){line.pixels[j]=1;}
	        page_image.put_column(x,matras[i][0]-matras[i][2],matras[i][2]+5,&line,0);
	      }
	    }  
 	  }
	  
	}
	
page_image.write("bentest.tif");

	////////////clip_matras() ends/////////////////////////////

/////////DEBAYAN/////////////////


}


// Threshold the given grey or color image into the tesseract global
// image ready for recognition. Requires thresholds and hi_value
// produced by OtsuThreshold above.
void TessBaseAPI::ThresholdRect(const unsigned char* imagedata,
                                int bytes_per_pixel,
                                int bytes_per_line,
                                int left, int top,
                                int width, int height,
                                const int* thresholds,
                                const int* hi_values) {

  IMAGELINE line;
  page_image.create(width, height, 1);
  line.init(width);
  
  // For each line in the image, fill the IMAGELINE class and put it into the
  // Tesseract global page_image. Note that Tesseract stores images with the
  // bottom at y=0 and 0 is black, so we need 2 kinds of inversion.
  const unsigned char* data = imagedata + top*bytes_per_line +
                              left*bytes_per_pixel;
  for (int y = height - 1 ; y >= 0; --y) {
    const unsigned char* pix = data;
    for (int x = 0; x < width; ++x, pix += bytes_per_pixel) {
      line.pixels[x] = 1;
      for (int ch = 0; ch < bytes_per_pixel; ++ch) {
        if (hi_values[ch] >= 0 &&
            (pix[ch] > thresholds[ch]) == (hi_values[ch] == 0)) {
          line.pixels[x] = 0;
          break;
        }
      }
    }
    page_image.put_line(0, y, width, &line, 0);
    data += bytes_per_line;
  }
page_image.write("benth.tif");
float angle=findskew(height,width);
//cout<<"SKEW ANGLE="<<angle<<"\n";
if(angle!=0){
deskew(angle,height,width);
}
ClipMaatraa(height,width);
}

// Cut out the requested rectangle of the binary image to the
// tesseract global image ready for recognition.
void TessBaseAPI::CopyBinaryRect(const unsigned char* imagedata,
                                 int bytes_per_line,
                                 int left, int top,
                                 int width, int height) {
  // Copy binary image, cutting out the required rectangle.
  IMAGE image;
  image.capture(const_cast<unsigned char*>(imagedata),
                bytes_per_line*8, top + height, 1);
  page_image.create(width, height, 1);

  copy_sub_image(&image, left, 0, width, height, &page_image, 0, 0, false);
image.write("bentest.tif");
}

// Low-level function to recognize the current global image to a string.
char* TessBaseAPI::RecognizeToString() {
  BLOCK_LIST    block_list;

  FindLines(&block_list);

  // Now run the main recognition.
  PAGE_RES* page_res = Recognize(&block_list, NULL);

  return TesseractToText(page_res);

}

// Find lines from the image making the BLOCK_LIST.
void TessBaseAPI::FindLines(BLOCK_LIST* block_list) {
  // The following call creates a full-page block and then runs connected
  // component analysis and text line creation.
  pgeditor_read_file(input_file, block_list);
}

// Recognize the tesseract global image and return the result as Tesseract
// internal structures.
PAGE_RES* TessBaseAPI::Recognize(BLOCK_LIST* block_list, ETEXT_DESC* monitor) {
  if (tessedit_resegment_from_boxes)
    apply_boxes(block_list);

  PAGE_RES* page_res = new PAGE_RES(block_list);
  if (interactive_mode) {
    pgeditor_main(block_list);                  //pgeditor user I/F
  } else if (tessedit_train_from_boxes) {
    apply_box_training(block_list);
  } else {
    // Now run the main recognition.
    recog_all_words(page_res, monitor);
  }
  return page_res;
}

// Return the maximum length that the output text string might occupy.
int TessBaseAPI::TextLength(PAGE_RES* page_res) {
  PAGE_RES_IT   page_res_it(page_res);
  int total_length = 2;
  // Iterate over the data structures to extract the recognition result.
  for (page_res_it.restart_page(); page_res_it.word () != NULL;
       page_res_it.forward()) {
    WERD_RES *word = page_res_it.word();
    WERD_CHOICE* choice = word->best_choice;
    if (choice != NULL) {
      total_length += choice->string().length() + 1;
      for (int i = 0; i < word->reject_map.length(); ++i) {
        if (word->reject_map[i].rejected())
          ++total_length;
      }
    }
  }
  return total_length;
}

// Returns an array of all word confidences, terminated by -1.
int* TessBaseAPI::AllTextConfidences(PAGE_RES* page_res) {
  if (!page_res) return NULL;
  int n_word = 0;
  PAGE_RES_IT res_it(page_res);
  for (res_it.restart_page(); res_it.word () != NULL; res_it.forward())
    n_word++;

  int* conf = new int[n_word+1];
  n_word = 0;
  for (res_it.restart_page(); res_it.word () != NULL; res_it.forward()) {
    WERD_RES *word = res_it.word();
    WERD_CHOICE* choice = word->best_choice;
    int w_conf = static_cast<int>(100 + 5 * choice->certainty());
                 // This is the eq for converting Tesseract confidence to 1..100
    if (w_conf < 0) w_conf = 0;
    if (w_conf > 100) w_conf = 100;
    conf[n_word++] = w_conf;
  }
  conf[n_word] = -1;
  return conf;
}

// Returns the average word confidence for Tesseract page result.
int TessBaseAPI::TextConf(PAGE_RES* page_res) {
  int* conf = AllTextConfidences(page_res);
  if (!conf) return 0;
  int sum = 0;
  int *pt = conf;
  while (*pt >= 0) sum += *pt++;
  if (pt != conf) sum /= pt - conf;
  delete [] conf;
  return sum;
}

// Make a text string from the internal data structures.
// The input page_res is deleted.
char* TessBaseAPI::TesseractToText(PAGE_RES* page_res) {
  if (page_res != NULL) {
    int total_length = TextLength(page_res);
    PAGE_RES_IT   page_res_it(page_res);
    char* result = new char[total_length];
    char* ptr = result;
    for (page_res_it.restart_page(); page_res_it.word () != NULL;
         page_res_it.forward()) {
      WERD_RES *word = page_res_it.word();
      WERD_CHOICE* choice = word->best_choice;
      if (choice != NULL) {
        strcpy(ptr, choice->string().string());
        ptr += strlen(ptr);
        if (word->word->flag(W_EOL))
          *ptr++ = '\n';
        else
          *ptr++ = ' ';
      }
    }
    *ptr++ = '\n';
    *ptr = '\0';
    delete page_res;
    return result;
  }
  return NULL;
}

static int ConvertWordToBoxText(WERD_RES *word,
                                ROW_RES* row,
                                int left,
                                int bottom,
                                char* word_str) {
  // Copy the output word and denormalize it back to image coords.
  WERD copy_outword;
  copy_outword = *(word->outword);
  copy_outword.baseline_denormalise(&word->denorm);
  PBLOB_IT blob_it;
  blob_it.set_to_list(copy_outword.blob_list());
  int length = copy_outword.blob_list()->length();
  int output_size = 0;

  if (length > 0) {
    for (int index = 0, offset = 0; index < length;
         offset += word->best_choice->lengths()[index++], blob_it.forward()) {
      PBLOB* blob = blob_it.data();
      TBOX blob_box = blob->bounding_box();
      if (word->tess_failed ||
          blob_box.left() < 0 ||
          blob_box.right() > page_image.get_xsize() ||
          blob_box.bottom() < 0 ||
          blob_box.top() > page_image.get_ysize()) {
        // Bounding boxes can be illegal when tess fails on a word.
        blob_box = word->word->bounding_box();  // Use original word as backup.
        tprintf("Using substitute bounding box at (%d,%d)->(%d,%d)\n",
                blob_box.left(), blob_box.bottom(),
                blob_box.right(), blob_box.top());
      }

      // A single classification unit can be composed of several UTF-8
      // characters. Append each of them to the result.
      for (int sub = 0; sub < word->best_choice->lengths()[index]; ++sub) {
        char ch = word->best_choice->string()[offset + sub];
        // Tesseract uses space for recognition failure. Fix to a reject
        // character, '~' so we don't create illegal box files.
        if (ch == ' ')
          ch = '~';
        word_str[output_size++] = ch;
      }
      sprintf(word_str + output_size, " %d %d %d %d\n",
              blob_box.left() + left, blob_box.bottom() + bottom,
              blob_box.right() + left, blob_box.top() + bottom);
      output_size += strlen(word_str + output_size);
    }
  }
  return output_size;
}

// Multiplier for textlength assumes 4 numbers @ 5 digits and a space
// plus the newline and the orginial character = 4*(5+1)+2
const int kMaxCharsPerChar = 26;

// Make a text string from the internal data structures.
// The input page_res is deleted.
// The text string takes the form of a box file as needed for training.
char* TessBaseAPI::TesseractToBoxText(PAGE_RES* page_res,
                                      int left, int bottom) {
  if (page_res != NULL) {
    int total_length = TextLength(page_res) * kMaxCharsPerChar;
    PAGE_RES_IT   page_res_it(page_res);
    char* result = new char[total_length];
    char* ptr = result;
    for (page_res_it.restart_page(); page_res_it.word () != NULL;
         page_res_it.forward()) {
      WERD_RES *word = page_res_it.word();
      ptr += ConvertWordToBoxText(word,page_res_it.row(),left, bottom, ptr);
    }
    *ptr = '\0';
    delete page_res;
    return result;
  }
  return NULL;
}

// Make a text string from the internal data structures.
// The input page_res is deleted. The text string is converted
// to UNLV-format: Latin-1 with specific reject and suspect codes.
const char kUnrecognized = '~';
// Conversion table for non-latin characters.
// Maps characters out of the latin set into the latin set.
// TODO(rays) incorporate this translation into unicharset.
const int kUniChs[] = {
  0x20ac, 0x201c, 0x201d, 0x2018, 0x2019, 0x2022, 0x2014, 0
};
// Latin chars corresponding to the unicode chars above.
const int kLatinChs[] = {
  0x00a2, 0x0022, 0x0022, 0x0027, 0x0027, 0x00b7, 0x002d, 0
};

char* TessBaseAPI::TesseractToUNLV(PAGE_RES* page_res) {
  bool tilde_crunch_written = false;
  bool last_char_was_newline = true;
  bool last_char_was_tilde = false;

  if (page_res != NULL) {
    int total_length = TextLength(page_res);
    PAGE_RES_IT   page_res_it(page_res);
    char* result = new char[total_length];
    char* ptr = result;
    for (page_res_it.restart_page(); page_res_it.word () != NULL;
         page_res_it.forward()) {
      WERD_RES *word = page_res_it.word();
      // Process the current word.
      if (word->unlv_crunch_mode != CR_NONE) {
        if (word->unlv_crunch_mode != CR_DELETE &&
            (!tilde_crunch_written ||
             (word->unlv_crunch_mode == CR_KEEP_SPACE &&
              word->word->space () > 0 &&
              !word->word->flag (W_FUZZY_NON) &&
              !word->word->flag (W_FUZZY_SP)))) {
          if (!word->word->flag (W_BOL) &&
              word->word->space () > 0 &&
              !word->word->flag (W_FUZZY_NON) &&
              !word->word->flag (W_FUZZY_SP)) {
            /* Write a space to separate from preceeding good text */
            *ptr++ = ' ';
            last_char_was_tilde = false;
          }
          if (!last_char_was_tilde) {
            // Write a reject char.
            last_char_was_tilde = true;
            *ptr++ = kUnrecognized;
            tilde_crunch_written = true;
            last_char_was_newline = false;
          }
        }
      } else {
        // NORMAL PROCESSING of non tilde crunched words.
        tilde_crunch_written = false;

        if (last_char_was_tilde &&
            word->word->space () == 0 &&
            (word->best_choice->string ()[0] == ' ')) {
          /* Prevent adjacent tilde across words - we know that adjacent tildes within
             words have been removed */
          char* p = (char *) word->best_choice->string().string ();
          strcpy (p, p + 1);       //shuffle up
          p = (char *) word->best_choice->lengths().string ();
          strcpy (p, p + 1);       //shuffle up
          word->reject_map.remove_pos (0);
          PBLOB_IT blob_it = word->outword->blob_list ();
          delete blob_it.extract ();   //get rid of reject blob
        }

        if (word->word->flag(W_REP_CHAR) && tessedit_consistent_reps)
          ensure_rep_chars_are_consistent(word);

        set_unlv_suspects(word);
        const char* wordstr = word->best_choice->string().string();
        if (wordstr[0] != 0) {
          if (!last_char_was_newline)
            *ptr++ = ' ';
          else
            last_char_was_newline = false;
          int offset = 0;
          const STRING& lengths = word->best_choice->lengths();
          int length = lengths.length();
          for (int i = 0; i < length; offset += lengths[i++]) {
            if (wordstr[offset] == ' ' ||
                wordstr[offset] == '~' ||
                wordstr[offset] == '|') {
              *ptr++ = kUnrecognized;
              last_char_was_tilde = true;
            } else {
              if (word->reject_map[i].rejected())
                *ptr++ = '^';
              UNICHAR ch(wordstr + offset, lengths[i]);
              int uni_ch = ch.first_uni();
              for (int j = 0; kUniChs[j] != 0; ++j) {
                if (kUniChs[j] == uni_ch) {
                  uni_ch = kLatinChs[j];
                  break;
                }
              }
              if (uni_ch <= 0xff) {
                *ptr++ = static_cast<char>(uni_ch);
                last_char_was_tilde = false;
              } else {
                *ptr++ = kUnrecognized;
                last_char_was_tilde = true;
              }
            }
          }
        }
      }
      if (word->word->flag(W_EOL) && !last_char_was_newline) {
        /* Add a new line output */
        *ptr++ = '\n';
        tilde_crunch_written = false;
        last_char_was_newline = true;
        last_char_was_tilde = false;
      }
    }
    *ptr++ = '\n';
    *ptr = '\0';
    delete page_res;
    return result;
  }
  return NULL;
}
// ____________________________________________________________________________
// Ocropus add-ons.

// Find lines from the image making the BLOCK_LIST.
BLOCK_LIST* TessBaseAPI::FindLinesCreateBlockList() {
  BLOCK_LIST *block_list = new BLOCK_LIST();
  FindLines(block_list);
  return block_list;
}

// Delete a block list.
// This is to keep BLOCK_LIST pointer opaque
// and let go of including the other headers.
void TessBaseAPI::DeleteBlockList(BLOCK_LIST *block_list) {
  delete block_list;
}


static ROW *make_tess_ocrrow(float baseline,
                             float xheight,
                             float descender,
                             float ascender) {
  inT32 xstarts[] = {-32000};
  double quad_coeffs[] = {0,0,baseline};
  return new ROW(1,
                 xstarts,
                 quad_coeffs,
                 xheight,
                 ascender - (baseline + xheight),
                 descender - baseline,
                 0,
                 0);
}

// Almost a copy of make_tess_row() from ccmain/tstruct.cpp.
static void fill_dummy_row(float baseline, float xheight,
                           float descender, float ascender,
                           TEXTROW* tessrow) {
  tessrow->baseline.segments = 1;
  tessrow->baseline.xstarts[0] = -32767;
  tessrow->baseline.xstarts[1] = 32767;
  tessrow->baseline.quads[0].a = 0;
  tessrow->baseline.quads[0].b = 0;
  tessrow->baseline.quads[0].c = bln_baseline_offset;
  tessrow->xheight.segments = 1;
  tessrow->xheight.xstarts[0] = -32767;
  tessrow->xheight.xstarts[1] = 32767;
  tessrow->xheight.quads[0].a = 0;
  tessrow->xheight.quads[0].b = 0;
  tessrow->xheight.quads[0].c = bln_baseline_offset + bln_x_height;
  tessrow->lineheight = bln_x_height;
  tessrow->ascrise = bln_x_height * (ascender - (xheight + baseline)) / xheight;
  tessrow->descdrop = bln_x_height * (descender - baseline) / xheight;
}


/// Return a TBLOB * from the whole page_image.
/// To be freed later with free_blob().
TBLOB *make_tesseract_blob(float baseline, float xheight, float descender, float ascender) {
  BLOCK *block = new BLOCK ("a character",
                            TRUE,
                            0, 0,
                            0, 0,
                            page_image.get_xsize(),
                            page_image.get_ysize());

  // Create C_BLOBs from the page
  extract_edges(NULL, &page_image, &page_image,
                ICOORD(page_image.get_xsize(), page_image.get_ysize()),
                block);

  // Create one PBLOB from all C_BLOBs
  C_BLOB_LIST *list = block->blob_list();
  C_BLOB_IT c_blob_it(list);
  PBLOB *pblob = new PBLOB; // will be (hopefully) deleted by the pblob_list
  for (c_blob_it.mark_cycle_pt();
       !c_blob_it.cycled_list();
       c_blob_it.forward()) {
      C_BLOB *c_blob = c_blob_it.data();
      PBLOB c_as_p(c_blob, baseline + xheight);
      merge_blobs(pblob, &c_as_p);
  }
  PBLOB_LIST *pblob_list = new PBLOB_LIST; // will be deleted by the word
  PBLOB_IT pblob_it(pblob_list);
  pblob_it.add_after_then_move(pblob);

  // Normalize PBLOB
  WERD word(pblob_list, 0, " ");
  ROW *row = make_tess_ocrrow(baseline, xheight, descender, ascender);
  word.baseline_normalise(row);
  delete row;

  // Create a TBLOB from PBLOB
  return make_tess_blob(pblob, /* flatten: */ TRUE);
}


// Adapt to recognize the current image as the given character.
// The image must be preloaded and be just an image of a single character.
void TessBaseAPI::AdaptToCharacter(const char *unichar_repr,
                                   int length,
                                   float baseline,
                                   float xheight,
                                   float descender,
                                   float ascender) {
  UNICHAR_ID id = unicharset.unichar_to_id(unichar_repr, length);
  LINE_STATS LineStats;
  TEXTROW row;
  fill_dummy_row(baseline, xheight, descender, ascender, &row);
  GetLineStatsFromRow(&row, &LineStats);

  TBLOB *blob = make_tesseract_blob(baseline, xheight, descender, ascender);
  float threshold;
  int best_class = 0;
  float best_rating = -100;


  // Classify to get a raw choice.
  LIST result = AdaptiveClassifier(blob, NULL, &row);
  LIST p;
  for (p = result; p != NULL; p = p->next) {
    A_CHOICE *tesschoice = (A_CHOICE *) p->node;
    if (tesschoice->rating > best_rating) {
      best_rating = tesschoice->rating;
      best_class = tesschoice->string[0];
    }
  }

  FLOAT32 GetBestRatingFor(TBLOB *Blob, LINE_STATS *LineStats, CLASS_ID ClassId);

  // We have to use char-level adaptation because otherwise
  // someone should do forced alignment somewhere.
  void AdaptToChar(TBLOB *Blob,
                   LINE_STATS *LineStats,
                   CLASS_ID ClassId,
                   FLOAT32 Threshold);


  if (id == best_class)
    threshold = GoodAdaptiveMatch;
  else {
    /* the blob was incorrectly classified - find the rating threshold
       needed to create a template which will correct the error with
       some margin.  However, don't waste time trying to make
       templates which are too tight. */
    threshold = GetBestRatingFor(blob, &LineStats, id);
    threshold *= .9;
    const float max_threshold = .125;
    const float min_threshold = .02;

    if (threshold > max_threshold)
        threshold = max_threshold;

    // I have cuddled the following line to set it out of the strike
    // of the coverage testing tool. I have no idea how to trigger
    // this situation nor I have any necessity to do it. --mezhirov
    if (threshold < min_threshold) threshold = min_threshold;
  }

  if (blob->outlines)
    AdaptToChar(blob, &LineStats, id, threshold);
  free_blob(blob);
}


PAGE_RES* TessBaseAPI::RecognitionPass1(BLOCK_LIST* block_list) {
  PAGE_RES *page_res = new PAGE_RES(block_list);
  recog_all_words(page_res, NULL, NULL, 1);
  return page_res;
}

PAGE_RES* TessBaseAPI::RecognitionPass2(BLOCK_LIST* block_list,
                                        PAGE_RES* pass1_result) {
  if (!pass1_result)
    pass1_result = new PAGE_RES(block_list);
  recog_all_words(pass1_result, NULL, NULL, 2);
  return pass1_result;
}

// brief Get a bounding box of a PBLOB.
// TODO(mezhirov) delete this function and replace with blob->bounding_box()
static TBOX pblob_get_bbox(PBLOB *blob) {
  OUTLINE_LIST *outlines = blob->out_list();
  OUTLINE_IT it(outlines);
  TBOX result;
  for (it.mark_cycle_pt(); !it.cycled_list(); it.forward()) {
    OUTLINE *outline = it.data();
    outline->compute_bb();
    result.bounding_union(outline->bounding_box());
  }
  return result;
}

// TODO(mezhirov) delete this function and replace with word->bounding_box()
static TBOX c_blob_list_get_bbox(C_BLOB_LIST *cblobs) {
  TBOX result;
  C_BLOB_IT c_it(cblobs);
  for (c_it.mark_cycle_pt(); !c_it.cycled_list(); c_it.forward()) {
    C_BLOB *blob = c_it.data();
    //bboxes.push(tessy_rectangle(blob->bounding_box()));
    result.bounding_union(blob->bounding_box());
  }
  return result;
}

struct TESS_CHAR : ELIST_LINK {
  char *unicode_repr;
  int length; // of unicode_repr
  float cost;
  TBOX box;

  TESS_CHAR(float _cost, const char *repr, int len = -1) : cost(_cost) {
    length = (len == -1 ? strlen(repr) : len);
    unicode_repr = new char[length + 1];
    strncpy(unicode_repr, repr, length);
  }

  ~TESS_CHAR() {
    delete unicode_repr;
  }
};


static void add_space(ELIST_ITERATOR *it) {
  TESS_CHAR *t = new TESS_CHAR(0, " ");
  it->add_after_then_move(t);
}


static float rating_to_cost(float rating) {
  rating = 100 + rating;
  // cuddled that to save from coverage profiler
  // (I have never seen ratings worse than -100,
  //  but the check won't hurt)
  if (rating < 0) rating = 0;
  return rating;
}


// Extract the OCR results, costs (penalty points for uncertainty),
// and the bounding boxes of the characters.
static void extract_result(ELIST_ITERATOR *out,
                           PAGE_RES* page_res) {
  PAGE_RES_IT page_res_it(page_res);
  int word_count = 0;
  while (page_res_it.word() != NULL) {
    WERD_RES *word = page_res_it.word();
    const char *str = word->best_choice->string().string();
    const char *len = word->best_choice->lengths().string();

    if (word_count)
      add_space(out);
    TBOX bln_rect;
    PBLOB_LIST *blobs = word->outword->blob_list();
    PBLOB_IT it(blobs);
    int n = strlen(len);
    TBOX** boxes_to_fix = new TBOX*[n];
    for (int i = 0; i < n; i++) {
      PBLOB *blob = it.data();
      TBOX current = pblob_get_bbox(blob);
      bln_rect.bounding_union(current);

      TESS_CHAR *tc = new TESS_CHAR(rating_to_cost(word->best_choice->rating()),
                                    str, *len);
      tc->box = current;
      boxes_to_fix[i] = &tc->box;

      out->add_after_then_move(tc);
      it.forward();
      str += *len;
      len++;
    }

    // Find the word bbox before normalization.
    // Here we can't use the C_BLOB bboxes directly,
    // since connected letters are not yet cut.
    TBOX real_rect = c_blob_list_get_bbox(word->word->cblob_list());

    // Denormalize boxes by transforming the bbox of the whole bln word
    // into the denorm bbox (`real_rect') of the whole word.
    double x_stretch = double(real_rect.width()) / bln_rect.width();
    double y_stretch = double(real_rect.height()) / bln_rect.height();
    for (int j = 0; j < n; j++) {
      TBOX *box = boxes_to_fix[j];
      int x0 = int(real_rect.left() +
                   x_stretch * (box->left() - bln_rect.left()) + 0.5);
      int x1 = int(real_rect.left() +
                   x_stretch * (box->right() - bln_rect.left()) + 0.5);
      int y0 = int(real_rect.bottom() +
                   y_stretch * (box->bottom() - bln_rect.bottom()) + 0.5);
      int y1 = int(real_rect.bottom() +
                   y_stretch * (box->top() - bln_rect.bottom()) + 0.5);
      *box = TBOX(ICOORD(x0, y0), ICOORD(x1, y1));
    }
    delete [] boxes_to_fix;

    page_res_it.forward();
    word_count++;
  }
}


// Extract the OCR results, costs (penalty points for uncertainty),
// and the bounding boxes of the characters.
int TessBaseAPI::TesseractExtractResult(char** string,
                                        int** lengths,
                                        float** costs,
                                        int** x0,
                                        int** y0,
                                        int** x1,
                                        int** y1,
                                        PAGE_RES* page_res) {
  ELIST tess_chars;
  ELIST_ITERATOR tess_chars_it(&tess_chars);
  extract_result(&tess_chars_it, page_res);
  tess_chars_it.move_to_first();
  int n = tess_chars.length();
  int string_len = 0;
  *lengths = new int[n];
  *costs = new float[n];
  *x0 = new int[n];
  *y0 = new int[n];
  *x1 = new int[n];
  *y1 = new int[n];
  int i = 0;
  for (tess_chars_it.mark_cycle_pt();
       !tess_chars_it.cycled_list();
       tess_chars_it.forward(), i++) {
    TESS_CHAR *tc = (TESS_CHAR *) tess_chars_it.data();
    string_len += (*lengths)[i] = tc->length;
    (*costs)[i] = tc->cost;
    (*x0)[i] = tc->box.left();
    (*y0)[i] = tc->box.bottom();
    (*x1)[i] = tc->box.right();
    (*y1)[i] = tc->box.top();
  }
  char *p = *string = new char[string_len];

  tess_chars_it.move_to_first();
  for (tess_chars_it.mark_cycle_pt();
        !tess_chars_it.cycled_list();
       tess_chars_it.forward()) {
    TESS_CHAR *tc = (TESS_CHAR *) tess_chars_it.data();
    strncpy(p, tc->unicode_repr, tc->length);
    p += tc->length;
  }
  return n;
}

// Check whether a word is valid according to Tesseract's language model
// returns 0 if the string is invalid, non-zero if valid
int TessBaseAPI::IsValidWord(const char *string) {
  return valid_word(string);
}
