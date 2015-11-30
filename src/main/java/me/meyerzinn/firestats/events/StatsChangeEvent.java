package me.meyerzinn.firestats.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class StatsChangeEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;

	public StatsChangeEvent(Player p) {
		this.player = p;
	}

	public Player getPlayer() {
		return this.player;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}