package me.meyerzinn.firestats.util;

import org.bukkit.entity.Player;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import me.meyerzinn.firestats.FireStats;

public class Utils {

	public static void newPlayer(Player p) {
		Firebase playerRef = FireStats.fb.child("players").child(p.getUniqueId().toString());
		playerRef.child("kills").setValue(0L);
		playerRef.child("deaths").setValue(0L);
		playerRef.child("damage_dealt").setValue(0D);
		playerRef.child("damage_received").setValue(0D);
		playerRef.child("joined").setValue(ServerValue.TIMESTAMP);
	}
	
}
