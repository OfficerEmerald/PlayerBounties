package com.officeremerald.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.officeremerald.utils.CoreB;

public class bountyAdmin implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("bountyadmin") || cmd.getName().equalsIgnoreCase("ba") || cmd.getName().equalsIgnoreCase("bountya")) {

			if (!(sender instanceof Player)) {
				System.out.println(CoreB.config.getString("Messages.consoleException"));
				return true;
			} else {

				Player p = (Player) sender;

				if (args.length == 0) {

					for (String s : CoreB.config.getStringList("Messages.Help")) {
						p.sendMessage(CoreB.replaceVar(s));
					}

					return true;

				} else {

					if (p.hasPermission("bounty.admin")) {

					if (args[0].equalsIgnoreCase("remove")) {

							if (args.length < 2) {
								p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.argsAdminPException")));
								return true;

							} else {

								Player t = Bukkit.getServer().getPlayer(args[1]);

								if (t == null || !CoreB.target.contains(t.getName())) {
									p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.invalidPlayerAdminException")));
									return true;
								} else {
									CoreB.sender.remove(p.getName());
									CoreB.sender.add(p.getName());
									Bukkit.broadcastMessage(CoreB.replaceVar(CoreB.config.getString("Messages.bountyBroadcastRemove")));
									CoreB.sender.remove(p.getName());
									CoreB.target.remove(t.getName());
									return false;
								}

							}

						} else if (args[0].equalsIgnoreCase("check")) {
							if (args.length < 2) {
								p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.argsAdminPException")));
								return true;

							} else {

								Player t = Bukkit.getServer().getPlayer(args[1]);

								if (t == null) {
									p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.invalidPlayerAdminException")));
									return true;
								} else {

									if (CoreB.target.contains(t.getName())) {

										CoreB.sender.add(p.getName());
										p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.playerCheckValidException")));

										return false;
									} else {

										p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.playerCheckInvalidException")));
										return false;
									}

								}
							}

						}
					} else {

						p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.noPermission")));
						return true;
					}

				}

			}
		}

		return false;
	}

}
