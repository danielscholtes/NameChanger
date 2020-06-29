package me.scholtes.namechanger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static void message(Player player, String text) {
		player.sendMessage(color(text));
	}
	
	
}
