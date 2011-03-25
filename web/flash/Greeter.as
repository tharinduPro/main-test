package {
	public class Greeter {
		public static var validNames:Array = ["Sammy", "Frank", "Dean"];
		public function sayHello(userName:String = ""):String {
			var greeting:String;
			if (userName == "") {
				greeting = "Hello. Please type your user name, and then press the Enter key.";
			} 
			else {
				greeting = "Hello " + userName;
			}
			return greeting;
		}
	}
}