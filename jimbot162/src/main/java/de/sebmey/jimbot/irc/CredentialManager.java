package de.sebmey.jimbot.irc;

public class CredentialManager {
	private static String botUsername;
	private static String botTwitchOAuth;
	private static String botSRLPass;
	
	public static String getBotUsername() {
		return botUsername;
	}
	public static void setBotUsername(String botUsername) {
		CredentialManager.botUsername = botUsername;
	}
	public static String getBotTwitchOAuth() {
		return botTwitchOAuth;
	}
	public static void setBotTwitchOAuth(String botTwitchOAuth) {
		CredentialManager.botTwitchOAuth = botTwitchOAuth;
	}
	public static String getBotSRLPass() {
		return botSRLPass;
	}
	public static void setBotSRLPass(String botSRLPass) {
		CredentialManager.botSRLPass = botSRLPass;
	}
}
