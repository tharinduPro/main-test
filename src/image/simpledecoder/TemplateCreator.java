package image.simpledecoder;

import image.simpledecoder.pub.filter.Filter;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

//模板创建类
class TemplateCreator {   
	public static void main(String[] args) throws Exception {   
		Set set = new HashSet();         
		String url = "http://passport.csdn.net/member/ShowEXPwd.ASPx";  
		String filterClazz = "pay365.CsdnFilter";       
		if (args.length>=1) {        
			url = args[0];     
			}       
		if (args.length>=2) {    
			filterClazz = args[1];      
			}     
		Filter csdnFilter = (Filter) Class.forName(filterClazz).newInstance();        
		for (int i = 1; i < 10; i++) {  
			URL u = new URL(url);      
			BufferedImage bi = ImageIO.read(u);     
			ImageData ia2 = new ImageData(bi,csdnFilter);   
			ImageData[] ii = ia2.split();      
			for (int x = 0; x < ii.length; x++) { 
				ImageData imageArr = ii[x];       
				set.add(imageArr);         
				}      
			// set.addAll(Arrays.asList(ia2.split())); 
			}        System.out.println(set.size());     
			for (Iterator iter = set.iterator(); iter.hasNext();) {        
				ImageData ele = (ImageData) iter.next();       
				ele.show();        
				System.out.print("char:");     
				String s = readLine();          
				if (s.length() == 1) {          
					ele.code = s.charAt(0);     
					}        
			}          
			PrintWriter pw = new PrintWriter(new File("template.data"));     
			for (Iterator iter = set.iterator(); iter.hasNext();) {      
				ImageData ele = (ImageData) iter.next();    
				pw.println(ele.encode());    
			}    
			pw.flush();        
			pw.close();    
			}   
	private static BufferedReader reader = new BufferedReader(  
			new InputStreamReader(System.in));  
	private static String readLine() {      
		try {      
			return reader.readLine();   
			} 
		catch (Exception e) {      
			e.printStackTrace();    
			return "";  
			}  
		}}