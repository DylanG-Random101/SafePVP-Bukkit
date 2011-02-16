package com.random101.safepvp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.player.PlayerEvent;

public class SafePVPEntityListener extends EntityListener {
	private final SafePVP plugin;

	public SafePVPEntityListener(SafePVP plugin) {
		this.plugin = plugin;
	}

	public SafePVP getPlugin() {
		return plugin;
	}
	
	public void onEntityDamageByProjectile(EntityDamageByProjectileEvent event) {
		Entity att = event.getDamager();
		Entity def = event.getEntity();
		if (((att instanceof PlayerEvent) && (def instanceof PlayerEvent)) || ((att instanceof Player) && (def instanceof Player))) {
			Player attacker = ((PlayerEvent) att).getPlayer();
			Player defender = ((PlayerEvent) def).getPlayer();
			if (SafePVPListener1.noPvp.contains(defender.getName())) { 
				if (SafePVPListener1.yesMsg.contains(defender.getName()))
					defender.sendMessage(SafePVPListener1.prefix + attacker.getName() + "'s arrows are useless on you.");
				if (!event.isCancelled()) {
					event.setCancelled(true);
				}
			}
			if (SafePVPListener1.noPvp.contains(attacker.getName())) {
				if (SafePVPListener1.yesMsg.contains(attacker.getName()))
					attacker.sendMessage(SafePVPListener1.prefix + defender.getName() + " is on the no PVP list!");
				if (!event.isCancelled()) {
					event.setCancelled(true);
				}
			}
			if (SafePVPListener1.yesPvp.contains(attacker.getName()) && SafePVPListener1.yesPvp.contains(defender.getName())) {
			}
		}
	}

	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
		Entity att = event.getDamager();
		Entity def = event.getEntity();
		if (((att instanceof PlayerEvent) && (def instanceof PlayerEvent)) || ((att instanceof Player) && (def instanceof Player))) {
			Player attacker = ((PlayerEvent) att).getPlayer();
			Player defender = ((PlayerEvent) def).getPlayer();
			int amount = event.getDamage();
			if (SafePVPListener1.accepted) {
				if (SafePVPListener1.finished) {
					if (!event.isCancelled()) {
						event.setCancelled(true);
					}
				} else {
					if (SafePVPListener1.sparList.contains(attacker.getName().toLowerCase()) && SafePVPListener1.sparList.contains(defender.getName().toLowerCase())) {
						if (defender.getHealth()-amount <= 0) {
							plugin.getServer().broadcastMessage(SafePVPListener1.spar + attacker.getName() + " has won the spar between " + defender.getName() + "! Congratulations!");
							SafePVPListener1.finished = true;
							SafePVPListener1.inProgress = false;
							SafePVPListener1.accepted = false;
							SafePVPListener1.valid = false;
							SafePVPListener1.waiting = false;
							SafePVPListener1.sparList.remove(attacker.getName().toLowerCase());
							SafePVPListener1.sparList.remove(defender.getName().toLowerCase());
						}					

					}
				}
			}
			if (SafePVPListener1.iAccepted) {
				if (SafePVPListener1.iFinished) { 				
					if (!event.isCancelled()) {
						event.setCancelled(true);
					} 
				} else {
					if (SafePVPListener1.iSparList.contains(attacker.getName().toLowerCase()) && SafePVPListener1.iSparList.contains(defender.getName().toLowerCase())) {
						if (defender.getHealth()-amount <= 0) {
							plugin.getServer().broadcastMessage(SafePVPListener1.duel + attacker.getName() + " has won the iSpar between " + defender.getName() + "!");
							//  plugin.getServer().broadcastMessage(SafePVPListener1.duel + attacker.getName() + " has been credited " + SafePVPListener1.iWin + " " + SafePVPListener1.currency + "!");
							//	int balance = Hooked.getInt("iBalance", new Object[] { "balance", attacker.getName() });
							//	int newBalance = (balance+iWin);
							//	Hooked.silent("iBalance", new Object[] { "set", attacker.getName(), newBalance }); 
							SafePVPListener1.iFinished = true;
							SafePVPListener1.iInProgress = false;
							SafePVPListener1.iAccepted = false;
							SafePVPListener1.iValid = false;
							SafePVPListener1.iWaiting = false;
							SafePVPListener1.iSparList.remove(attacker.getName().toLowerCase());
							SafePVPListener1.iSparList.remove(defender.getName().toLowerCase());
						}					

					}
				}
			}
			if (SafePVPListener1.sparList.contains(defender.getName().toLowerCase()) && SafePVPListener1.accepted) {
				attacker.sendMessage(SafePVPListener1.spar + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " is participating in a spar and you aren't their partner.");			

			}
			if (SafePVPListener1.sparList.contains(attacker.getName().toLowerCase()) && SafePVPListener1.accepted) {
				attacker.sendMessage(SafePVPListener1.spar + "You are sparring and " + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE +  " isn't your partner.");			

			}
			if (SafePVPListener1.iSparList.contains(defender.getName().toLowerCase()) && SafePVPListener1.iAccepted) {
				attacker.sendMessage(SafePVPListener1.duel + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " is participating in an iSpar and you aren't their partner.");			

			}
			if (SafePVPListener1.iSparList.contains(attacker.getName().toLowerCase()) && SafePVPListener1.iAccepted) {
				attacker.sendMessage(SafePVPListener1.duel + "You are iSparring and " + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " isn't your partner.");			

			}
			if (SafePVPListener1.noPvp.contains(defender.getName())) {
				if (SafePVPListener1.yesMsg.contains(defender.getName())) {
					defender.sendMessage(SafePVPListener1.prefix + "The no PVP list protects you from " + ChatColor.DARK_AQUA + attacker.getName() + ChatColor.WHITE +  ".");
				}
				if (SafePVPListener1.yesMsg.contains(attacker.getName())) {
					attacker.sendMessage(SafePVPListener1.prefix + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " is on the no PVP list. You can't damage them.");			
				}
			}
			if (SafePVPListener1.noPvp.contains(attacker.getName())) {
				if (SafePVPListener1.yesMsg.contains(defender.getName())) {
					defender.sendMessage(SafePVPListener1.prefix + ChatColor.DARK_AQUA + attacker.getName() + ChatColor.WHITE + " is on the no PVP list. You can't be damaged.");
				}
				if (SafePVPListener1.yesMsg.contains(attacker.getName())) {
					attacker.sendMessage(SafePVPListener1.prefix + "You are on the no PVP list. You can not damage!");						
				}
			}
			if (!SafePVPListener1.noPvp.contains(attacker.getName()) && !SafePVPListener1.yesPvp.contains(attacker.getName())) {
				if (SafePVPListener1.yesMsg.contains(attacker.getName())) {
					attacker.sendMessage(SafePVPListener1.prefix + "Type /pvp on to battle!");
				}
			}
			if (!SafePVPListener1.noPvp.contains(defender.getName()) && !SafePVPListener1.yesPvp.contains(defender.getName())) {
				if (SafePVPListener1.yesMsg.contains(attacker.getName())) {
					attacker.sendMessage(SafePVPListener1.prefix + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " hasn't enabled PVP yet!");
				}
				if (SafePVPListener1.yesMsg.contains(defender.getName())) {
					defender.sendMessage(SafePVPListener1.prefix + "Type /pvp on to battle or /pvp off to stay safe.");
				}
			}
			if (SafePVPListener1.yesPvp.contains(attacker.getName()) && (SafePVPListener1.yesPvp.contains(defender.getName()))) {
				if (defender.getHealth()-amount <= 0) {
					plugin.getServer().broadcastMessage(SafePVPListener1.prefix + ChatColor.RED + attacker.getName() + " has just killed " + defender.getName() + ".");
				}
			}
		}
	}
}
