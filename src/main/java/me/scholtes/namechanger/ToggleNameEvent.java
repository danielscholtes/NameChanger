package me.scholtes.namechanger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ToggleNameEvent extends Event {
	
	private final Player player;
	private final boolean hidden;
	private final String name;
	
	public ToggleNameEvent(Player player, boolean hidden, String name) {
		this.player = player;
		this.hidden = hidden;
		this.name = name;
	}
	
	private static final HandlerList HANDLERS = new HandlerList();
	
	public HandlerList getHandlers() {
		return HANDLERS;
	}

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
	public Player getPlayer() {
		return player;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public String getChangedName() {
		return name;
	}

}
