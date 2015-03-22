package com.officeremerald.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.officeremerald.utils.CoreB;

public class placeBounty implements CommandExecutor {

	/**
	 * 
	 * Plugin Developer: OfficerEmerald
	 * 
	 * All plugin code was typed by hand by him. Plugin available to public: NO
	 * 
	 * Do not claim ownership.
	 * 
	 */

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("bounty") || cmd.getName().equalsIgnoreCase("b") || cmd.getName().equalsIgnoreCase("bountyplace") || cmd.getName().equalsIgnoreCase("placebounty")) {
			if (!(sender instanceof Player)) {
				System.out.println(CoreB.config.getString("Messages.consoleException"));
				return true;
			} else {

				Player p = (Player) sender;

				if (args.length < 2) {

					for (String s : CoreB.config.getStringList("Messages.Help")) {
						p.sendMessage(CoreB.replaceVar(s));
					}

				} else {

					try {

						Double.parseDouble(args[0]);

					} catch (NumberFormatException e) {

						p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.invalidNumberException")));
						return true;

					}

					if (CoreB.econ.getBalance(p) < Double.parseDouble(args[0])) {

						p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.invalidBalException")));

						return true;

					} else {

						if (Double.parseDouble(args[0]) <= CoreB.config.getInt("Messages.minimumBid")) {
							p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.minimumBidException")));

							return true;
						} else {

							Player t = Bukkit.getServer().getPlayer(args[1]);

							if (t == null) {
								p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.invalidPlayerException")));

								return true;
							} else {

								if (CoreB.target.contains(t.getName())) {

									p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.playerCheckValidException")));

									return true;
								} else {
									double bountyPB = Double.parseDouble(args[0]);
									if (bountyPB > CoreB.config.getInt("Messages.bountyBalBroadcast")) {

										CoreB.BountyBal = bountyPB;
										CoreB.bounty.put(t.getUniqueId(), Double.parseDouble(args[0]));
										CoreB.target.add(t.getName());

										p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.bountyPlayerMessage")));
										CoreB.sender.add(p.getName());
										Bukkit.broadcastMessage(CoreB.replaceVar(CoreB.config.getString("Messages.bountyBroadcast")));
										CoreB.sender.remove(p.getName());
										CoreB.econ.withdrawPlayer(p, bountyPB);
										return false;

									} else {
										CoreB.BountyBal = bountyPB;

										CoreB.bounty.put(t.getUniqueId(), Double.parseDouble(args[0]));
										CoreB.target.add(t.getName());

										p.sendMessage(CoreB.replaceVar(CoreB.config.getString("Messages.bountyPlayerMessage")));

										CoreB.econ.withdrawPlayer(p, bountyPB);
										return false;
									}

								}

							}
						}
					}
				}

			}
		}

		return false;
	}
}
