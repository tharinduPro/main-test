package net.dgie.xmltask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class XMLFixer{
    private String encoding;
    private String URL;
    public XMLFixer( String URL ) {
        GetMethod method = null;
        try {
            this.URL = URL;
            //获得response 的编码
            HttpClient client = new HttpClient();   
            method = new GetMethod( URL );
            client.executeMethod(method);
            encoding = method.getResponseCharSet();
            method.releaseConnection();
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
        finally {
            method.releaseConnection();
        }
    }

    public void getXMLFromURL( File saveToFile ) {
        FileOutputStream outStream = null;
        try {
            Format format = Format.getPrettyFormat();
            format.setEncoding( encoding );

            XMLOutputter outToFile = new XMLOutputter();
            outToFile.setFormat( format );

            // Create an instance of Tagsoup
            SAXBuilder builder = new SAXBuilder("org.ccil.cowan.tagsoup.Parser"); 
            // Parse my (HTML) URL into a well-formed document
            Document doc = builder.build( new URL( this.URL ) );
            outStream = new FileOutputStream( saveToFile );
            outToFile.output( doc,outStream);
            outStream.flush();
        }
        catch( JDOMException jde ) {
            jde.printStackTrace();
        }
        catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        finally {
            try {
                outStream.close();
            }
            catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
    }

    public void fixXMLFile( File inputFile, File outputFile ) {
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            in = new BufferedReader( new InputStreamReader( new FileInputStream( inputFile ), this.encoding ) );
            out= new BufferedWriter( new OutputStreamWriter( new FileOutputStream( outputFile ), this.encoding ) );
            String inputLine = null;
            Pattern p = Pattern.compile("(href|src|action)\\s*=\\s*\"((?:http://)?[-:;%=&?_A-Za-z0-9./]+)\"");
            URLChanger UC = new URLChanger( this.URL );
            while ((inputLine = in.readLine()) != null) {
                Matcher m = p.matcher( inputLine );
                if( m.find() ) {
                    String link = m.group(2);
                    if( link.startsWith( "http://" ) ) {

                    }
                    else if( link.startsWith( "/" ) ) {
                        link = m.group(1) + "=\"" + UC.getRootPath() + link + "\""; 
                        StringBuffer sb = new StringBuffer();
                        m.appendReplacement(sb, link);
                        m.appendTail(sb);
                        inputLine =sb.toString();
                    } 
                    else {
                        link = m.group(1) + "=\"" + UC.getDirPath() + link + "\"";
                        StringBuffer sb = new StringBuffer();
                        m.appendReplacement(sb, link);
                        m.appendTail(sb);
                        inputLine =sb.toString();
                    }
                }
                out.write( inputLine, 0, inputLine.length() );
                out.newLine();
            }
            out.flush();
        }
        catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        finally {
            try {
                in.close();
                out.close();
            }
            catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
    }
}

class URLChanger {
    private String dirPath;
	
    private String rootPath;

	public String getRootPath(){
		return this.rootPath;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath=rootPath;
	}
    
    public String getDirPath(){
        return this.dirPath;
    }
    public void setDirPath(String dirPath) {
        this.dirPath=dirPath;
    }
    
    public URLChanger( String inputURL ) {
        Pattern rootPattern = Pattern.compile( "((http://[\\w\\d.:-]+/?)(?:[\\w\\d_-]+/?)*)(?:/[\\w\\d_-]+\\.html)?/?" );
        Matcher rootMatcher = rootPattern.matcher( inputURL );
        if( rootMatcher.matches() ) {
            this.dirPath = rootMatcher.group(1);
            if( !this.dirPath.endsWith( "/" ) ) {
                this.dirPath += "/";
            }
            this.rootPath = rootMatcher.group(2);
            if( this.rootPath.endsWith( "/" ) ) {
                this.rootPath = this.rootPath.substring( 0, this.rootPath.lastIndexOf( "/" ) );
            }
        }
    }
}

