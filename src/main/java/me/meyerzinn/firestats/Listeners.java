package me.meyerzinn.firestats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import me.meyerzinn.firestats.events.StatsChangeEvent;
import me.meyerzinn.firestats.util.Utils;

public class Listeners implements Listener {

	// @EventHandler(priority = EventPriority.MONITOR)
	// public void onPlayerDeathEvent(PlayerDeathEvent e) {
	// if (e.getEntity().getKiller() instanceof Player) {
	// Player victim = (Player) e.getEntity();
	// Player killer = (Player) e.getEntity().getKiller();
	// Firebase playersRef = FireStats.fb.child("players");
	// Query killerQuery =
	// playersRef.orderByChild("uuid").equalTo(killer.getUniqueId().toString());
	// killerQuery.addChildEventListener(new ChildEventListener() {
	// public void onChildAdded(DataSnapshot snapshot, String previousChild) {
	// System.out.println(snapshot.getKey());
	// Firebase killerRef =
	// FireStats.fb.child("players").child(snapshot.getKey());
	// killerRef.child("kills").runTransaction(new Transaction.Handler() {
	// public Transaction.Result doTransaction(MutableData currentData) {
	// if (currentData.getValue() == null) {
	// currentData.setValue(1);
	// } else {
	// currentData.setValue((Integer) currentData.getValue() + 1);
	// }
	// return Transaction.success(currentData);
	// }
	//
	// public void onComplete(FirebaseError firebaseError, boolean committed,
	// DataSnapshot currentData) {
	// }
	// });
	// }
	//
	// public void onCancelled(FirebaseError arg0) {
	// }
	//
	// public void onChildChanged(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildMoved(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildRemoved(DataSnapshot arg0) {
	// }
	// });
	// StatsChangeEvent killerStatsChange = new StatsChangeEvent(killer);
	// Bukkit.getServer().getPluginManager().callEvent(killerStatsChange);
	// Query victimQuery =
	// playersRef.orderByChild("uuid").equalTo(victim.getUniqueId().toString());
	// victimQuery.addChildEventListener(new ChildEventListener() {
	// public void onChildAdded(DataSnapshot snapshot, String previousChild) {
	// System.out.println(snapshot.getKey());
	// Firebase victimRef =
	// FireStats.fb.child("players").child(snapshot.getKey());
	// victimRef.child("deaths").runTransaction(new Transaction.Handler() {
	// public Transaction.Result doTransaction(MutableData currentData) {
	// if (currentData.getValue() == null) {
	// currentData.setValue(1);
	// } else {
	// currentData.setValue((Integer) currentData.getValue() + 1);
	// }
	// return Transaction.success(currentData);
	// }
	//
	// public void onComplete(FirebaseError firebaseError, boolean committed,
	// DataSnapshot currentData) {
	// }
	// });
	// }
	//
	// public void onCancelled(FirebaseError arg0) {
	// }
	//
	// public void onChildChanged(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildMoved(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildRemoved(DataSnapshot arg0) {
	// }
	// });
	// StatsChangeEvent victimStatsChange = new StatsChangeEvent(victim);
	// Bukkit.getServer().getPluginManager().callEvent(victimStatsChange);
	// }
	// }

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player victim = (Player) e.getEntity();
		Firebase victimRef = FireStats.fb.child("players").child(victim.getUniqueId().toString());
		victimRef.child("damage_received").runTransaction(new Transaction.Handler() {

			public Transaction.Result doTransaction(MutableData arg0) {
				if (arg0.getValue() == null) {
					arg0.setValue(e.getDamage());
				} else {
					arg0.setValue((Double) arg0.getValue() + e.getDamage());
				}
				return Transaction.success(arg0);
			}

			public void onComplete(FirebaseError arg0, boolean arg1, DataSnapshot arg2) {
			}
		});
		StatsChangeEvent victimStatsChange = new StatsChangeEvent(victim);
		Bukkit.getPluginManager().callEvent(victimStatsChange);
		if (e.getDamager() instanceof Player) {
			final Player killer = (Player) e.getDamager();
			Firebase killerRef = FireStats.fb.child("players").child(killer.getUniqueId().toString().toString());
			killerRef.child("damage_dealt").runTransaction(new Transaction.Handler() {

				public Transaction.Result doTransaction(MutableData arg0) {
					if (arg0.getValue() == null) {
						arg0.setValue(e.getDamage());
					} else {
						arg0.setValue((Double) arg0.getValue() + e.getDamage());
					}
					return Transaction.success(arg0);
				}

				public void onComplete(FirebaseError arg0, boolean arg1, DataSnapshot arg2) {
				}
			});
			StatsChangeEvent killerStatsChange = new StatsChangeEvent(killer);
			Bukkit.getPluginManager().callEvent(killerStatsChange);
		}
		if (e.getDamage() >= victim.getHealth()) {
			// Fatality
//			System.out.println("Fatality.");
//			System.out.println(victim.getUniqueId().toString());
			// Firebase victimRef =
			// FireStats.fb.child("players").child(victim.getUniqueId().toString());
			victimRef.child("deaths").runTransaction(new Transaction.Handler() {

				public Transaction.Result doTransaction(MutableData arg0) {
					if (arg0.getValue() == null) {
						arg0.setValue(1L);
					} else {
						arg0.setValue((Long)arg0.getValue() + 1L);
					}
					return Transaction.success(arg0);
				}

				public void onComplete(FirebaseError arg0, boolean arg1, DataSnapshot arg2) {
				}
			});
			Bukkit.getPluginManager().callEvent(victimStatsChange);
			if (e.getDamager() instanceof Player) {
				final Player killer = (Player) e.getDamager();
				Firebase killerRef = FireStats.fb.child("players").child(killer.getUniqueId().toString().toString());
				killerRef.child("kills").runTransaction(new Transaction.Handler() {

					public Transaction.Result doTransaction(MutableData arg0) {
						if ((Integer) arg0.getValue() <= 0L) {
							arg0.setValue(1L);
						} else {
							arg0.setValue((Long)arg0.getValue() + 1L);
						}
						return Transaction.success(arg0);
					}

					public void onComplete(FirebaseError arg0, boolean arg1, DataSnapshot arg2) {
					}
				});
				StatsChangeEvent killerStatsChange = new StatsChangeEvent(killer);
				Bukkit.getPluginManager().callEvent(killerStatsChange);
			}
		}
	}
	// @EventHandler(priority = EventPriority.HIGHEST)
	// public void onPlayerDeathEvent(PlayerDeathEvent e) {
	// Player victim = e.getEntity();
	// System.out.println(victim.getUniqueId().toString());
	// Firebase victimRef =
	// FireStats.fb.child("players").child(victim.getUniqueId().toString());
	// victimRef.child("deaths").runTransaction(new Transaction.Handler() {
	//
	// public Result doTransaction(MutableData arg0) {
	// arg0.setValue((Integer) arg0.getValue() + 1);
	// return Transaction.success(arg0);
	// }
	//
	// public void onComplete(FirebaseError arg0, boolean arg1, DataSnapshot
	// arg2) {
	// System.out.println(arg2.getValue());
	// }
	// });
	// StatsChangeEvent victimStatsChange = new StatsChangeEvent(e.getEntity());
	// Bukkit.getPluginManager().callEvent(victimStatsChange);
	// if (e.getEntity().getKiller() instanceof Player) {
	// final Player killer = e.getEntity().getKiller();
	// Firebase killerRef =
	// FireStats.fb.child("players").child(killer.getUniqueId().toString().toString());
	// killerRef.child("kills").runTransaction(new Transaction.Handler() {
	//
	// public Result doTransaction(MutableData arg0) {
	// if ((Integer) arg0.getValue() <= 0) {
	// arg0.setValue(1);
	// } else {
	// arg0.setValue((Integer) arg0.getValue() + 1);
	// }
	// return Transaction.success(arg0);
	// }
	//
	// public void onComplete(FirebaseError arg0, boolean arg1, DataSnapshot
	// arg2) {
	// System.out.println(arg2.getValue());
	// }
	// });
	// StatsChangeEvent killerStatsChange = new StatsChangeEvent(killer);
	// Bukkit.getPluginManager().callEvent(killerStatsChange);
	// }
	// }

	// @EventHandler(priority = EventPriority.MONITOR)
	// public void onEntityDamageByEntityEvent(final EntityDamageByEntityEvent
	// e) {
	// if (e.getEntity() instanceof Player) {
	// Firebase playersRef = FireStats.fb.child("players");
	// Player victim = (Player) e.getEntity();
	// Query victimQuery =
	// playersRef.orderByChild("uuid").equalTo(victim.getUniqueId().toString());
	// victimQuery.addChildEventListener(new ChildEventListener() {
	// public void onChildAdded(DataSnapshot snapshot, String previousChild) {
	// System.out.println(snapshot.getKey());
	// Firebase victimRef =
	// FireStats.fb.child("players").child(snapshot.getKey());
	// victimRef.child("damage_received").runTransaction(new
	// Transaction.Handler() {
	// public Transaction.Result doTransaction(MutableData currentData) {
	// if (currentData.getValue() == null) {
	// currentData.setValue(e.getDamage());
	// } else {
	// currentData.setValue((Double) currentData.getValue() + e.getDamage());
	// }
	// return Transaction.success(currentData);
	// }
	//
	// public void onComplete(FirebaseError firebaseError, boolean committed,
	// DataSnapshot currentData) {
	// }
	// });
	// }
	//
	// public void onCancelled(FirebaseError arg0) {
	// }
	//
	// public void onChildChanged(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildMoved(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildRemoved(DataSnapshot arg0) {
	// }
	// });
	// StatsChangeEvent victimStatsChange = new StatsChangeEvent(victim);
	// Bukkit.getServer().getPluginManager().callEvent(victimStatsChange);
	// if (e.getDamager() instanceof Player) {
	// Player killer = (Player) e.getDamager();
	// Query killerQuery =
	// playersRef.orderByChild("uuid").equalTo(killer.getUniqueId().toString());
	// killerQuery.addChildEventListener(new ChildEventListener() {
	// public void onChildAdded(DataSnapshot snapshot, String previousChild) {
	// System.out.println(snapshot.getKey());
	// Firebase killerRef =
	// FireStats.fb.child("players").child(snapshot.getKey());
	// killerRef.child("damage_dealt").runTransaction(new Transaction.Handler()
	// {
	// public Transaction.Result doTransaction(MutableData currentData) {
	// if (currentData.getValue() == null) {
	// currentData.setValue(e.getDamage());
	// } else {
	// currentData.setValue((Double) currentData.getValue() + e.getDamage());
	// }
	// return Transaction.success(currentData);
	// }
	//
	// public void onComplete(FirebaseError firebaseError, boolean committed,
	// DataSnapshot currentData) {
	// }
	// });
	// }
	//
	// public void onCancelled(FirebaseError arg0) {
	// }
	//
	// public void onChildChanged(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildMoved(DataSnapshot arg0, String arg1) {
	// }
	//
	// public void onChildRemoved(DataSnapshot arg0) {
	// }
	// });
	// StatsChangeEvent killerStatsChange = new StatsChangeEvent(killer);
	// Bukkit.getServer().getPluginManager().callEvent(killerStatsChange);
	// }
	// }
	// }

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(final PlayerJoinEvent e) {
		Firebase playersRef = FireStats.fb.child("players");
		Firebase playerRef = playersRef.child(e.getPlayer().getUniqueId().toString());
		playerRef.addValueEventListener(new ValueEventListener() {
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.getValue() == null)
					Utils.newPlayer(e.getPlayer());
			}

			public void onCancelled(FirebaseError firebaseError) {
				System.out.println("The read failed: " + firebaseError.getMessage());
			}
		});
	}
}