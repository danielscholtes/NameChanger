package me.scholtes.namechanger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

public class NameChangerAPI {

	public static void changeName(Player toChange, Player toReceive, String name, boolean overrideCache, boolean saveOverride) {
		
		WrappedGameProfile profile = WrappedGameProfile.fromPlayer(toChange);
		
		if (NameChanger.getInstance() == null || !NameChanger.getInstance().isEnabled()) {
			return;
		}
		
		if (overrideCache || !NameChanger.getInstance().getPacketCache().getRemovePacketCache().containsKey(toChange.getUniqueId())) {
			PacketContainer packetRemovePlayer = NameChanger.getInstance().getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
			List<PlayerInfoData> removeDataList = new ArrayList<PlayerInfoData>();
			PlayerInfoData removeData = new PlayerInfoData(profile, ((CraftPlayer) toChange).getHandle().ping, NativeGameMode.fromBukkit(toChange.getGameMode()), WrappedChatComponent.fromText(toChange.getName()));
			removeDataList.add(removeData);
			packetRemovePlayer.getPlayerInfoDataLists().write(0, removeDataList);
			packetRemovePlayer.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
			
			if (!overrideCache || saveOverride) {
				NameChanger.getInstance().getPacketCache().getRemovePacketCache().put(toChange.getUniqueId(), packetRemovePlayer);
			}
			if (overrideCache) {
				try {
					NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, packetRemovePlayer);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (overrideCache || !NameChanger.getInstance().getPacketCache().getAddPacketCache().containsKey(toChange.getUniqueId())) {
			PacketContainer packetAddPlayer = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
			
			List<PlayerInfoData> addDataList = new ArrayList<PlayerInfoData>();
			WrappedGameProfile newProfile = profile.withName(name);
			newProfile.getProperties().putAll(profile.getProperties());
			PlayerInfoData addData = new PlayerInfoData(newProfile, ((CraftPlayer) toChange).getHandle().ping, NativeGameMode.fromBukkit(toChange.getGameMode()), 
					WrappedChatComponent.fromText(toChange.getName()));
			addDataList.add(addData);
			packetAddPlayer.getPlayerInfoDataLists().write(0, addDataList);
			packetAddPlayer.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
			
			if (!overrideCache || saveOverride) {
				NameChanger.getInstance().getPacketCache().getAddPacketCache().put(toChange.getUniqueId(), packetAddPlayer);
			}
			if (overrideCache) {
				try {
					NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, packetAddPlayer);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (overrideCache || !NameChanger.getInstance().getPacketCache().getDestroyPacketCache().containsKey(toChange.getUniqueId())) {
			PacketContainer destroyEntityPacket = NameChanger.getInstance().getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
			destroyEntityPacket.getIntegerArrays().write(0, new int[]{toChange.getEntityId()});
			if (!overrideCache || saveOverride) {
				NameChanger.getInstance().getPacketCache().getDestroyPacketCache().put(toChange.getUniqueId(), destroyEntityPacket);
			}
			if (overrideCache) {
				try {
					NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, destroyEntityPacket);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (overrideCache || !NameChanger.getInstance().getPacketCache().getSpawnPacketCache().containsKey(toChange.getUniqueId())) {
			PacketContainer spawnEntityPacket = NameChanger.getInstance().getProtocolManager().createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
			spawnEntityPacket.getIntegers().write(0, toChange.getEntityId());
			spawnEntityPacket.getUUIDs().write(0, toChange.getUniqueId());
			spawnEntityPacket.getDoubles()
				.write(0, toChange.getLocation().getX())
				.write(1, toChange.getLocation().getY())
				.write(2, toChange.getLocation().getZ());
			spawnEntityPacket.getBytes().write(0, (byte) toChange.getLocation().getYaw());
			spawnEntityPacket.getBytes().write(1, (byte) toChange.getLocation().getPitch());
			
			if (!overrideCache || saveOverride) {
				NameChanger.getInstance().getPacketCache().getSpawnPacketCache().put(toChange.getUniqueId(), spawnEntityPacket);	
			}
			if (overrideCache) {
				try {
					NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, spawnEntityPacket);

					if (NameChanger.getPlayerData() != null) {
						if (!NameChanger.getPlayerData().getUpdatingInventory().contains(toChange.getUniqueId())) {
							NameChanger.getPlayerData().getUpdatingInventory().add(toChange.getUniqueId());
							Bukkit.getScheduler().runTaskLater(NameChanger.getInstance(), new Runnable() {	
								@Override
								public void run() {
									ItemStack[] contents = toChange.getInventory().getContents().clone();
									toChange.getInventory().clear();
									Bukkit.getScheduler().runTaskLater(NameChanger.getInstance(), new Runnable() {
										@Override
										public void run() {
											toChange.getInventory().setContents(contents);
											if (NameChanger.getPlayerData().getUpdatingInventory().contains(toChange.getUniqueId())) {
												NameChanger.getPlayerData().getUpdatingInventory().remove(toChange.getUniqueId());
											}
										}
									}, 1L);
								}
							}, 20L);
						}
					}
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (!overrideCache) {
			try {
				
				NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, NameChanger.getInstance().getPacketCache().getRemovePacketCache().get(toChange.getUniqueId()));
				NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, NameChanger.getInstance().getPacketCache().getAddPacketCache().get(toChange.getUniqueId()));
				NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, NameChanger.getInstance().getPacketCache().getDestroyPacketCache().get(toChange.getUniqueId()));
				NameChanger.getInstance().getProtocolManager().sendServerPacket(toReceive, NameChanger.getInstance().getPacketCache().getSpawnPacketCache().get(toChange.getUniqueId()));

				if (NameChanger.getPlayerData() != null) {
					if (!NameChanger.getPlayerData().getUpdatingInventory().contains(toChange.getUniqueId())) {
						NameChanger.getPlayerData().getUpdatingInventory().add(toChange.getUniqueId());
						Bukkit.getScheduler().runTaskLater(NameChanger.getInstance(), new Runnable() {	
							@Override
							public void run() {
								ItemStack[] contents = toChange.getInventory().getContents().clone();
								toChange.getInventory().clear();
								Bukkit.getScheduler().runTaskLater(NameChanger.getInstance(), new Runnable() {
									@Override
									public void run() {
										toChange.getInventory().setContents(contents);
										if (NameChanger.getPlayerData().getUpdatingInventory().contains(toChange.getUniqueId())) {
											NameChanger.getPlayerData().getUpdatingInventory().remove(toChange.getUniqueId());
										}
									}
								}, 1L);
							}
						}, 20L);
					}
				}
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		if (!overrideCache || saveOverride) {
			boolean hidden = false;
			if (NameChanger.getPlayerData() != null) {
				if (!isShowingName(toChange.getUniqueId())) {
					hidden = true;
				}
			}
			
			ToggleNameEvent event = new ToggleNameEvent(toChange, hidden, name);
			Bukkit.getPluginManager().callEvent(event);
		}
	
	}
	
	public static boolean isShowingName(UUID uuid) {
		if (NameChanger.getPlayerData() == null) {
			return false;
		}
		if (NameChanger.getPlayerData().getNameNotChanged().contains(uuid)) {
			return true;
		}
		return false;
	}
	
}
