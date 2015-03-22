package com.officeremerald.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.officeremerald.utils.CoreB;

@SuppressWarnings("deprecation")
public class bountyAcc implements Listener {

	/**
	 * 
	 * Plugin Developer: OfficerEmerald
	 * 
	 * All plugin code was typed by hand by him. Plugin available to public: NO
	 * 
	 * Do not claim ownership.
	 * 
	 */

	Hologram h;

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		Player dead = e.getEntity();
		Player killer = dead.getKiller();

		CoreB.killer.add(killer.getName());
		if (killer instanceof Player) {

			if (CoreB.bounty.containsKey(dead.getUniqueId())) {
				CoreB.econ.depositPlayer(killer, CoreB.bounty.get(dead.getUniqueId()));
				h = HolographicDisplaysAPI.createHologram(CoreB.instance, dead.getEyeLocation());

				for (String s : CoreB.config.getStringList("Hologram.Messages")) {
					h.addLine(CoreB.replaceVar(s));
				}

				if (CoreB.bounty.get(dead.getUniqueId()) < CoreB.config.getInt("Messages.bountyBalBroadcast")) {
					CoreB.config.set("Messages.Killer", killer.getUniqueId());

					killer.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.playerBountyClaim")));
					CoreB.bounty.remove(dead.getUniqueId());
					CoreB.target.remove(dead.getName());
					CoreB.killer.remove(killer.getName());
					killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
					dead.playSound(dead.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
					Bukkit.getScheduler().scheduleAsyncDelayedTask(CoreB.instance, new Runnable() {

						public void run() {

							h.delete();

						}

					}, CoreB.config.getInt("Hologram.StayTimeTicks"));

				} else {

					Bukkit.broadcastMessage(CoreB.replaceVar(CoreB.config.getString("Messages.BountyClaim")));

					killer.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.playerBountyClaim")));
					killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
					dead.playSound(dead.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
					CoreB.bounty.remove(dead.getUniqueId());
					CoreB.target.remove(dead.getName());
					CoreB.killer.remove(killer.getName());

					Bukkit.getScheduler().scheduleAsyncDelayedTask(CoreB.instance, new Runnable() {

						public void run() {

							h.delete();

						}

					}, CoreB.config.getInt("Hologram.StayTimeTicks"));

				}
			} else {
				return;
			}

		} else {
			return;
		}
	}
}
