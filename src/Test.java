
public class Test {
	static int x, y;
	public static void main(String[] args) {
		x--;	
		int x = 1;
		myMethod();
		System.out.println( x + y + ++x );
	}
	public static void myMethod() {
		y = x++ + x ;
		
		System.out.println( y + ":"  + x );
	}
}

