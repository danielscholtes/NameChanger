package me.scholtes.namechanger.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.scholtes.namechanger.NameChanger;

public class PlayerData {
	
	private List<UUID> nameNotChanged = new ArrayList<UUID>();
	private List<UUID> updatingInventory = new ArrayList<UUID>();
	
	private File file;
	private FileConfiguration data;
	
	public PlayerData(NameChanger plugin) {
		file = new File(plugin.getDataFolder().getAbsolutePath(), "playerdata.yml");
		data = YamlConfiguration.loadConfiguration(file);
	}
	
	public void loadPlayers() {
		if (data == null) {
			return;
		}
		
		if (data.getStringList("players") == null) {
			return;
		}
		
		for (String s : data.getStringList("players")) {
			nameNotChanged.add(UUID.fromString(s));
		}
	}
	
	public void saveData() {
		List<String> uuids = new ArrayList<String>();
		for (UUID uuid : nameNotChanged) {
			uuids.add(uuid.toString());
		}
		data.set("players", uuids);
		try {
			data.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<UUID> getNameNotChanged() {
		return nameNotChanged;
	}
	
	public List<UUID> getUpdatingInventory() {
		return updatingInventory;
	}

}
