package com.officeremerald.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.officeremerald.commands.bountyAdmin;
import com.officeremerald.commands.placeBounty;
import com.officeremerald.events.bountyAcc;

public class CoreB extends JavaPlugin {

	/**
	 * 
	 * Plugin Developer: OfficerEmerald
	 * 
	 * All plugin code was typed by hand by him. Plugin available to public: NO
	 * 
	 * Do not claim ownership.
	 * 
	 */

	public static Economy econ = null;
	public static Plugin instance = null;
	public static HashMap<UUID, Double> bounty = new HashMap<UUID, Double>();
	public static double BountyBal = 0;
	public static double minimumBid = 0;
	public static FileConfiguration config;
	public static ArrayList<String> killer = new ArrayList<String>();
	public static ArrayList<String> target = new ArrayList<String>();
	public static ArrayList<String> sender = new ArrayList<String>();

	public void onEnable() {

		if (!setupEconomy()) {
			getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") == null) {
			getLogger().severe("Plugin: HolographicDisplays not found! Disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Reg. Events
		Bukkit.getPluginManager().registerEvents(new bountyAcc(), this);
		System.out.println("PlayerBounties: Events had been loaded!");

		// Reg. Commands
		getCommand("bounty").setExecutor(new placeBounty());
		getCommand("b").setExecutor(new placeBounty());
		getCommand("bountyplace").setExecutor(new placeBounty());
		getCommand("placebounty").setExecutor(new placeBounty());
		getCommand("bountyadmin").setExecutor(new bountyAdmin());
		getCommand("ba").setExecutor(new bountyAdmin());
		getCommand("bountya").setExecutor(new bountyAdmin());

		System.out.println("PlayerBounties: Commands have been loaded!");

		config = getConfig();
		setupConfig();

		minimumBid = config.getInt("Messages.minimumBid");

		System.out.println("PlayerBounties > Plugin has been loaded succesfully!");

		instance = this;

	}

	public void setupConfig() {
		File configFile = new File("plugins/PlayerBounties", "config.yml");

		if (configFile.exists())
			return;

		config.set("Messages.consoleException", "PlayerBounties » You cannot use that command! Only players may use that command.");
		config.set("Messages.argsAdminPException", "&c&lBOUNTY &r&4» &4&lPlease put a player!");
		config.set("Messages.noPermission", "&c&lBOUNTY &r&4» &4&lYou have insuffient permissions!");
		config.set("Messages.invalidNumberException", "&c&lBOUNTY &r&4» &4&lPlease put a number!");
		config.set("Messages.invalidBalException", "&c&lBOUNTY &r&4» &4&lYou have insuffient funds!");
		config.set("Messages.minimumBid", 1000);
		config.set("Messages.minimumBidException", "&c&lBOUNTY &r&4» &4&lYou must place a bounty of &2&l$&a&l%MinBal&4&l!");
		config.set("Messages.invalidPlayerException", "&c&lBOUNTY &r&4» &4&lThat player was not found!");
		config.set("Messages.playerCheckValidException", "&c&lBOUNTY &r&4» &4&lThat player has a bounty of&r&4» &2&l$&a&l%Bal");
		config.set("Messages.playerCheckInvalidException", "&c&lBOUNTY &r&4» &4&lThat player does not have a bounty!");
		config.set("Messages.invalidPlayerException", "&c&lBOUNTY &r&4» &4&lThat player was not found!");
		config.set("Messages.Help", Arrays.asList("                  ", "                  ", " &r&4«  &c&lBOUNTY &r&4»", "                  ", "&3&l/bounty <Balance> <Player>", "&3&l/bounty help", "                  ", " &r&4«  &c&lADMIN CMD &r&4»", "                  ", "&3&l/bountyadmin <Remove/Check>", "                  ", "                  "));
		config.set("Messages.invalidPlayerAdminException", "&c&lBOUNTY &r&4» &4&lThat player was not found or does not have a bounty!");
		config.set("Messages.reloadSuccess", "&c&lBOUNTY &r&4» &4&lReloaded Config succesfully.");

		config.set("Messages.bountyBalBroadcast", 50000);
		config.set("Messages.bountyBroadcast", "&c&lBOUNTY &r&4» &6&l%BountyPlacer &3&lhas placed a bounty with a balance of&r&4» &2&l$&a%Bal &6&lon &c&l%Target");
		config.set("Messages.bountyBroadcastRemove", "&c&lBOUNTY &r&4» &6&l%BountyPlacer &3&lhas removed the bounty on&r&4» &c&l%Target");
		config.set("Messages.playerBountyClaim", "&c&lBOUNTY &r&4»" + "&3&lYou gained&r&4» &2&l$ &a&l%Bal");
		config.set("Messages.bountyPlayerMessage", "&c&lBOUNTY &r&4» &3&l" + "You have placed a bounty of&r&4» &2&l$&a%Bal &6&lon &c&l%Target");
		config.set("Messages.BountyClaim", "&c&lBOUNTY &r&4» &6&l%Killer&3&lhas taken the bounty of&r&4» &2&l$&a%Bal &3&lfrom &c&l%Target");
		config.set("Hologram.Messages", Arrays.asList("&c&lBOUNTY &r&4»", "&3&lYou gained &r&4» &2&l$&a&l%Bal"));
		config.set("Hologram.StayTimeTicks", 120);
		saveConfig();

		try {
			configFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public void onDisable() {

		System.out.println("PlayerBounties: Plugin has been disabled succesfully!");

	}

	public static String replaceVar(String s) {

		s = s.replace("%Bal", Double.toString(CoreB.BountyBal));
		s = s.replace("%MinBal", Double.toString(minimumBid));
		s = s.replace("%Target", target.toString());
		s = s.replace("%BountyPlacer", sender.toString());
		s = s.replace("%Killer", killer.toString());
		s = s.replace("[", "");
		s = s.replace("]", "");
		s = s.replace("&", "§");

		return s;
	}

}
