package net.dgie.xmltask;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static final String PROPERTIES_PATH = "config.properties";
    private Properties proObj;

	public Properties getProObj(){
		return this.proObj;
	}

	public void setProObj(Properties proObj) {
		this.proObj=proObj;
	}

    public PropertyReader() {
        proObj = new Properties();
        try {
            InputStream is = this.getClass().getResourceAsStream( PROPERTIES_PATH );
            proObj.load(is);
            if(is!=null) is.close();
        }
        catch(Exception e) {
            System.out.println(e+"file "+PROPERTIES_PATH+" not found");
        }
    }
    
    public String getProperty( String key ) {
       return proObj.getProperty( key );
    }

}
