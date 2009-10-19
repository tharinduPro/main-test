import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
public class TestRuntime {
    public static void main( String args[] ) {
        Runtime rt = Runtime.getRuntime();
        String cmd = "D:/ProgramFiles/Program/Tomcat6/bin/startup.bat";
        try {
            Process p = rt.exec( cmd );
            //BufferedReader br = new BufferedReader ( new InputStreamReader( p.getInputStream() ) );
            //String textLine = null;
            //while( ( textLine = br.readLine() ) != null ) {
                //System.out.println( textLine );
            //}
            p.waitFor();

        }
        catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        catch ( InterruptedException ie ) {
           ie.printStackTrace(); 
        }
    }
}
