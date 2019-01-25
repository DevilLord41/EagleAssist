package org.redeagle.debugger;

public class Log {
	private static boolean DEBUG = false;
	
	public static void d(String msg) {
		if(DEBUG) System.out.println("[DEBUG]" + msg);
	}
	
	public static void e(String msg) {
		System.out.println("[ERROR]" + msg);
	}
	
	public static void i(String msg) {
		System.out.println("[INFO]" + msg);
	}
	
	public static void setDebug(boolean deb) {
		DEBUG = deb;
	}

}
