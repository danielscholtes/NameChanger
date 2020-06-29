package me.scholtes.namechanger.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.comphenix.protocol.events.PacketContainer;

public class PacketCache {
	
	private Map<UUID, PacketContainer> removePacketCache = new HashMap<UUID, PacketContainer>();
	private Map<UUID, PacketContainer> addPacketCache = new HashMap<UUID, PacketContainer>();
	private Map<UUID, PacketContainer> destroyPacketCache = new HashMap<UUID, PacketContainer>();
	private Map<UUID, PacketContainer> spawnPacketCache = new HashMap<UUID, PacketContainer>();
	
	public Map<UUID, PacketContainer> getRemovePacketCache() {
		return removePacketCache;
	}

	public Map<UUID, PacketContainer> getAddPacketCache() {
		return addPacketCache;
	}

	public Map<UUID, PacketContainer> getDestroyPacketCache() {
		return destroyPacketCache;
	}

	public Map<UUID, PacketContainer> getSpawnPacketCache() {
		return spawnPacketCache;
	}

}