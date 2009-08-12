package net.dgie.dginfo;

public class Run {
	public static void main( String args[] ) {
		Parent ca = new ChildA();
		ca.methodParent();
		Parent cb = new ChildB();
		cb.methodParent();
	}
}