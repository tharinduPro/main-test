package ocr.another;

import   java.net.*;  
import   java.awt.image.*;  
import   java.awt.*;  
import   java.io.*;  
import   javax.imageio.*;  
 
/**  
  *   <p>Title:   八云测试用例</p>  
  *  
  *   <p>Description:   将CSDN的登陆验证图片转换为数字</p>  
  *  
  *   <p>Copyright:   Copyright   (c)   2004</p>  
  *  
  *   <p>Company:   </p>  
  *  
  *   @author   yagumo  
  *   @version   1.0  
  */  
public   class   CSDNImgIdent   {  
        private   int   intImgWith;  
        private   int   IntImgHeight;  
        private   int   intBgColor;  
        private   int   intCharColor;  
        private   int   intMinX;  
        private   int   intMaxX;  
        private   int   intMinY;  
        private   int   intMaxY;  
        private   String   strNum;  
        private   BufferedImage   img;  
        //座标原点  
        private   Point   pOrigin;  
        //字框宽  
        private   int   intCharWidth   =   8;  
        //字框高  
        private   int   intCharHeight   =   12;  
        //字框横向间隙  
        private   int   intCharSpaceH   =   1;  
        //字框纵向间隙  
        private   int   intCharSpaceY   =   1;  
        //数字字符比特表  
        private   final   long[][]   NUMERIC   =   {{0x3C4281818181L,   0x81818181423CL},               //'0'  
                                                                            {0X080838080808L,   0X08080808083EL},               //'1'  
                                                                            {0x3C4201010102L,   0x04081020407FL},               //'2'  
                                                                            {0x3C420101021CL,   0x02010101423CL},               //'3'  
                                                                            {0x02060A122242L,   0x82FF02020202L},               //'4'  
                                                                            {0x7F4040407C02L,   0x01010101423CL},               //'5'  
                                                                            {0x1E204080BCC2L,   0x81818181423CL},               //'6'  
                                                                            {0x7F0102020404L,   0x080810102020L},               //'7'  
                                                                            {0x3C428181423CL,   0x42818181423CL},               //'8'  
                                                                            {0x3C4281818181L,   0x433D01020478L}};             //'9'  
 
        /**  
          *   构造函数  
          *   @param   url   URL                                       远程文件  
          *   @throws   IOException  
          */  
        public   CSDNImgIdent(URL   url)   throws   IOException   {  
                img   =   ImageIO.read(url);  
                init();  
        }  
 
        /**  
          *   构造函数  
          *   @param   file   File                                   本地文件  
          *   @throws   IOException  
          */  
        public   CSDNImgIdent(File   file)   throws   IOException   {  
                img   =   ImageIO.read(file);  
                init();  
        }  
 
        /**  
          *   类初始工作  
          */  
        private   void   init()   {  
                //得到图象的长度和宽度  
                intImgWith   =   img.getWidth();  
                IntImgHeight   =   img.getHeight();  
                //得到图象的背景颜色  
                intBgColor   =   img.getRGB(0,   0);  
                //初始化图象原点座标  
                pOrigin   =   new   Point(3,   4);  
        }  
        private   void   getBaseInfo()   {  
                System.out.println(intBgColor   +   "|"   +   intCharColor);  
                System.out.println(intMinX   +   "|"   +   intMinY   +   "|"   +   intMaxX   +   "|"   +   intMaxY);  
        }  
 
        /**  
          *   得到字符的左上右下点座标  
          *   @param   intNo   int                                   第n个字符  
          *   @return   int[]  
          */  
        private   Point[]   getCharRange(int   intNo)   {  
                //左上右下点座标  
                Point   pTopLeft   =   new   Point(0,   0);  
                Point   pBottomRight   =   new   Point(0,   0);  
                //左上点  
                pTopLeft.x   =   pOrigin.x   +   intCharWidth   *   (intNo   -   1)   +   intCharSpaceH   *   (intNo   -1);  
                pTopLeft.y   =   pOrigin.y;  
                //右下点  
                pBottomRight.x   =   pOrigin.x   +   intCharWidth   *   intNo   +   intCharSpaceH   *   (intNo   -1)   -   1;  
                pBottomRight.y   =   pOrigin.y   +   intCharHeight   -   1;  
 
                return   new   Point[]   {pTopLeft,   pBottomRight};  
        }  
 
        /**  
          *   与背景颜色比较返回相应的字符  
          *   @param   x   int                                           横座标  
          *   @param   y   int                                           纵座标  
          *   @return   char                                           返回字符  
          */  
        private   char   getBit(int   x,   int   y)   {  
                int   intCurtColor;  
                intCurtColor   =   img.getRGB(x,   y);  
                return   (intCurtColor   ==   intBgColor)?   '0':   '1';  
        }  
 
        /**  
          *   得到第n个字符对应的字符串  
          *   @param   intNo   int                                   第n个字符  
          *   @return   String                                       代表字符位的串  
          */  
        private   String   getCharString(int   intNo)   {  
                //本字符的左上右下点座标  
                Point[]   p   =   getCharRange(intNo);  
                Point   pTopLeft   =   p[0];  
                Point   pBottomRight   =   p[1];  
                //换算边界值  
                int   intX1,   intY1,   intX2,   intY2;  
                intX1   =   pTopLeft.x;  
                intY1   =   pTopLeft.y;  
                intX2   =   pBottomRight.x;  
                intY2   =   pBottomRight.y;  
                //在边界内循环取象素  
                int   i,   j;  
                String   strChar   =   "";  
                for   (i   =   intY1;   i   <=   intY2;   i++)   {  
                        for   (j   =   intX1;   j   <=   intX2;   j++)   {  
                                strChar   =   strChar   +   getBit(j,   i);  
                        }  
                }  
 
                return   strChar;  
        }  
 
        /**  
          *   得到第n个字符对应数值  
          *   @param   intNo   int                                   第n个字符  
          *   @return   int                                             对应数值  
          */  
        public   int   getNum(int   intNo)   {  
                //取得位字符串  
                String   strChar   =   getCharString(intNo);  
                //取得串高位串和低位串  
                String   strCharHigh   =   strChar.substring(0,   strChar.length()   /   2);  
                String   strCharLow   =   strChar.substring(strChar.length()   /   2);  
                //计算高位和低位值  
                long   lCharHigh   =   Long.parseLong(strCharHigh,   2);  
                long   lCharLow   =   Long.parseLong(strCharLow,   2);  
                //在数字中循环比较  
                int   intNum   =   '*';  
                for   (int   i   =   0;   i   <=   9;   i++)   {  
                        if   (lCharHigh   ==   NUMERIC[i][0]   &&   lCharLow   ==   NUMERIC[i][1])   {  
                                intNum   =   i;  
                                break;  
                        }  
                }  
 
                return   intNum;  
        }  
 
        public   static   void   main(String[]   args)   {  
                try   {  
//                         CSDNImgIdent   CSDNImgIdent   =   new   CSDNImgIdent(new   File("d:/temp/5.gif"));  
                        CSDNImgIdent   imgIdent   =   new   CSDNImgIdent(new   URL("http://www.csdn.net/member/ShowExPwd.aspx"));  
                        String   strNum   =   "";  
                        for   (int   i   =   1;   i   <=   4;   i++)   {  
                                strNum   +=   String.valueOf(imgIdent.getNum(i));  
                        }  
                        System.out.println("结果是:   "   +   strNum);  
                }  
                catch   (IOException   ex)   {  
                        System.out.println(ex.getMessage());  
                }  
        }  
}   