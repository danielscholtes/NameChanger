package me.scholtes.namechanger.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.scholtes.namechanger.NameChanger;
import me.scholtes.namechanger.NameChangerAPI;
import me.scholtes.namechanger.Utils;
import me.scholtes.namechanger.data.PlayerData;

public class JoinListener implements Listener {
	
	private NameChanger plugin;
	private PlayerData playerData;
	
	public JoinListener(NameChanger plugin, PlayerData playerData) {
		this.plugin = plugin;
		this.playerData = playerData;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();	
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (player == p) {
						continue;
					}
					if (!playerData.getNameNotChanged().contains(player.getUniqueId())) {
						NameChangerAPI.changeName(player, p, Utils.color(" Player"), false, false);
					}
					if (!playerData.getNameNotChanged().contains(p.getUniqueId())) {
						NameChangerAPI.changeName(p, player, Utils.color(" Player"), false, false);
					}
				}
			}
		}, 1L);
		
	}

}
