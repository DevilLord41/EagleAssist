package org.redeagle;

import org.apache.commons.lang3.SystemUtils;
import org.redeagle.debugger.Log;


public class BotConfiguration {
	public static String BOT_TOKEN = System.getenv("BOT_TOKEN");
	public static String WINDOWS_PATH = "D:/DiscordBOT/EagleAssist/";
	public static String LINUX_PATH = "/root/";
	public static String BOT_PREFIX = "!s.";
	public static String PATH = "";
	
	public static void configure() {
		if(SystemUtils.IS_OS_WINDOWS) {
			PATH += WINDOWS_PATH;
			Log.i("OS : Windows");
		} else if(SystemUtils.IS_OS_LINUX) {
			PATH += LINUX_PATH;
			Log.i("OS : Linux");
		} else {
			Log.e("System OS are not specified");
			System.exit(0);
		}
	}
}
