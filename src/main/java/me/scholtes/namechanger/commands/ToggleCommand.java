package me.scholtes.namechanger.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.scholtes.namechanger.NameChangerAPI;
import me.scholtes.namechanger.Utils;
import me.scholtes.namechanger.data.PlayerData;

public class ToggleCommand implements CommandExecutor {

	private PlayerData playerData;
	
	public ToggleCommand(PlayerData playerData) {
		this.playerData = playerData;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("Not a player!");
		}
		
		Player player = (Player) sender;
		
		if (!playerData.getNameNotChanged().contains(player.getUniqueId())) {
			boolean overriden = false;
			playerData.getNameNotChanged().add(player.getUniqueId());
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (player == p) {
					continue;
				}
				if (!overriden) {
					overriden = true;
					NameChangerAPI.changeName(player, p, player.getName(), true, true);
					continue;
				}
				NameChangerAPI.changeName(player, p, player.getName(), false, false);
			}
			Utils.message(player, "&aYour name is now visible to everyone");
			return true;
		}
		
		playerData.getNameNotChanged().remove(player.getUniqueId());
		boolean overriden = false;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (player == p) {
				continue;
			}
			if (!overriden) {
				overriden = true;
				NameChangerAPI.changeName(player, p, Utils.color(" Player"), true, true);
				continue;
			}
			NameChangerAPI.changeName(player, p, Utils.color(" Player"), false, false);
		}
		Utils.message(player, "&aYour name is now hidden");
		return true;
	}
	

}
