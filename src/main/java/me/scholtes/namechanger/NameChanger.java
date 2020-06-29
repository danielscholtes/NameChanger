package me.scholtes.namechanger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import me.scholtes.namechanger.commands.ToggleCommand;
import me.scholtes.namechanger.data.PacketCache;
import me.scholtes.namechanger.data.PlayerData;
import me.scholtes.namechanger.listeners.JoinListener;
import me.scholtes.namechanger.listeners.LeaveListener;

public class NameChanger extends JavaPlugin {
	
	private ProtocolManager protocolManager;
	private PacketCache packetCache = new PacketCache();
	private static PlayerData playerData;
	
	private static NameChanger instance;
	
	public void onLoad() {
		protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	public void onEnable() {
		instance = this;
		
		playerData = new PlayerData(this);
		
		getServer().getPluginManager().registerEvents(new JoinListener(this, playerData), this);
		getServer().getPluginManager().registerEvents(new LeaveListener(this, packetCache), this);
		
		playerData.loadPlayers();
		
		getCommand("togglename").setExecutor(new ToggleCommand(playerData));
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				playerData.saveData();
			}
		}, 0L, 20 * 1L);
	}
	
	public void onDisable() {
		playerData.saveData();
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}
	
	public static NameChanger getInstance() {
		return instance;
	}
	
	public PacketCache getPacketCache() {
		return packetCache;
	}
	
	public static PlayerData getPlayerData() {
		return playerData;
	}

}
