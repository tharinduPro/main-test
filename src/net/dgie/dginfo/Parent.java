package net.dgie.dginfo;

public class Parent {
	public void methodParent(){
		calledMethod();
	}
	
	public void calledMethod() {
		System.out.println( "Parent is called" );
	}
}