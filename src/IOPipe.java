

import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;



public class IOPipe {
	public static void main( String[] args ) throws Exception {
		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream();
		try {
			pos.connect(pis);
			new IOPipe().new Producer(pos).start();
			new IOPipe().new Consumer(pis).start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	class Producer extends Thread{
		private PipedOutputStream pos;
		private OutputStream originalStream;
		public Producer(PipedOutputStream pos){
			this.pos = pos;
		}
		
		public void run() {
			try {
				
				pos.write(originalStream.toString().getBytes());
				pos.close();
			}catch( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	class Consumer extends Thread{
		private PipedInputStream pis;
		public Consumer(PipedInputStream pis){
			this.pis = pis;
		}
		public void run(){
			try {
				byte[] buf = new byte[100];
				int len = pis.read(buf);
				System.out.println(new String(buf,0,len));
				pis.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	} 
}
