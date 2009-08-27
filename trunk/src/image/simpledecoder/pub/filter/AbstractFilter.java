package image.simpledecoder.pub.filter;
public abstract class AbstractFilter  implements Filter {  
	public void doFilter(int[][] data) {   
		int height = data.length;        
		if ( height<=0 )          
			return;       
		int width = data[0].length;   
		if (width<=0)        
			return ;   
		for(int i=0;i< height;i++) {  
			for(int j=0;j< width;j++) {    
				data[i][j] = filter(data[i][j]); 
				}       
			}  
		}     
	protected abstract int filter(int p);
	
	}

