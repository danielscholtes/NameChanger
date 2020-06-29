package me.scholtes.namechanger.listeners;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.scholtes.namechanger.NameChanger;
import me.scholtes.namechanger.data.PacketCache;

public class LeaveListener implements Listener {
	
	private NameChanger plugin;
	private PacketCache packetCache;
	
	public LeaveListener(NameChanger plugin, PacketCache packetCache) {
		this.plugin = plugin;
		this.packetCache = packetCache;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (packetCache.getAddPacketCache().containsKey(event.getPlayer().getUniqueId())) {
			packetCache.getAddPacketCache().remove(event.getPlayer().getUniqueId());
		}
		if (packetCache.getRemovePacketCache().containsKey(event.getPlayer().getUniqueId())) {
			packetCache.getRemovePacketCache().remove(event.getPlayer().getUniqueId());
		}
		if (packetCache.getSpawnPacketCache().containsKey(event.getPlayer().getUniqueId())) {
			packetCache.getSpawnPacketCache().remove(event.getPlayer().getUniqueId());
		}
		if (packetCache.getDestroyPacketCache().containsKey(event.getPlayer().getUniqueId())) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				try {
					plugin.getProtocolManager().sendServerPacket(p, packetCache.getDestroyPacketCache().get(event.getPlayer().getUniqueId()));
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			packetCache.getDestroyPacketCache().remove(event.getPlayer().getUniqueId());
		}
	}

}
