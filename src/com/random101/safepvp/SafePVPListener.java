package com.random101.safepvp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class SafePVPListener extends PlayerListener {
	private final SafePVP plugin;
	ArrayList<String> yesPvp = new ArrayList<String>();
	ArrayList<String> noPvp = new ArrayList<String>();
	ArrayList<String> noMsg = new ArrayList<String>();
	ArrayList<String> yesMsg = new ArrayList<String>();
	ArrayList<String> noServer = new ArrayList<String>();
	ArrayList<String> sparList = new ArrayList<String>();
	ArrayList<String> iSparList = new ArrayList<String>();
	String fallMessage;
	String creeperMessage;
	String tntMessage;
	String waterMessage;
	boolean waiting;
	boolean on;
	Boolean defaultPvpOff;
	Boolean defaultMessageOff;
	boolean defaultDMOff;
	boolean inProgress;
	boolean valid;
	int defaultToggleDelay;
	int defaultSparTime;
	boolean accepted;
	String teleportLocation; 
	String xGroup;
	boolean finished;
	Player starter;
	boolean iWaiting;
	boolean iOn;
	boolean iInProgress;
	boolean iValid;
	int defaultISparTime;
	boolean iAccepted;
	String iteleportLocation;
	boolean found;
	String iXGroup;
	boolean iFinished;
	Player iStarter;
	int iWin;
	String currency;
	Properties prop = new Properties(); 

	{
		try {
			/*
			 * Set default config values
			 */
			prop.setProperty("iConomy-duel-win-prize", "50");
			prop.setProperty("command-to-execute-when-spar-is-accepted", "spawn");
			prop.setProperty("pvp-off-on-join", "true");
			prop.setProperty("message-off-on-join", "true");
			prop.setProperty("death-messages", "false");
			prop.setProperty("default-toggle-delay", "30");
			prop.setProperty("spar-acceptance-time", "30");
			prop.setProperty("iSpar-acceptance-time", "30");
			prop.setProperty("execute-command-group", "default");
			prop.setProperty("fall-message", "just took a leap of faith!.. and missed.");
			prop.setProperty("creeper-message", "just hugged a creeper!");
			prop.setProperty("tnt-message", "just went boom!");
			prop.setProperty("water-message", "forgot how to swim...");

			File file = new File("SafePVP.properties");
			if(!file.exists()) {
				file.createNewFile(); 
				prop.store(new FileOutputStream("SafePVP.properties"), null);
			}
			/*
			 * Load the config values
			 */
			prop.load(new FileInputStream("SafePVP.properties"));
			/*
			 * Set the config values with the loaded values (or default, if not found)
			 */
			iWin = Integer.parseInt(prop.getProperty("iConomy-duel-win-prize", "50"));
			teleportLocation = prop.getProperty("command-to-execute-when-spar-is-accepted", "spawn");
			defaultPvpOff = Boolean.parseBoolean(prop.getProperty("pvp-off-on-join", "true"));
			defaultMessageOff = Boolean.parseBoolean(prop.getProperty("message-off-on-join", "true"));
			defaultDMOff = Boolean.parseBoolean(prop.getProperty("death-messages", "true"));
			defaultToggleDelay = Integer.parseInt(prop.getProperty("default-toggle-delay", "30"));
			defaultSparTime = Integer.parseInt(prop.getProperty("spar-acceptance-time", "30"));
			defaultISparTime = Integer.parseInt(prop.getProperty("iSpar-acceptance-time", "30"));
			xGroup = prop.getProperty("execute-command-group", "default");
			fallMessage = prop.getProperty("fall-message", "just took a leap of faith/!.. and missed.");
			creeperMessage = prop.getProperty("creeper-message", "just hugged a creeper!");
			tntMessage = prop.getProperty("tnt-message", "just went boom!");
			waterMessage = prop.getProperty("water-message", "forgot how to swim...");

			/*
			 * Store the config file
			 */
			prop.store(new FileOutputStream("SafePVP.properties"), null);
		} catch (IOException ioe) {
		} finally {
			prop = null;
		}
	}

	boolean inGroup;
	String prefix = ChatColor.GREEN + "[SafePVP] " + ChatColor.WHITE;
	String prefixA = prefix + ChatColor.YELLOW + "[ADMIN] " + ChatColor.WHITE; 
	String spar = prefix + ChatColor.GOLD + "[SPAR] " + ChatColor.WHITE;
	String duel = prefix + ChatColor.GRAY + "[iSPAR] " + ChatColor.WHITE;
	String prefixS = prefix + ChatColor.DARK_BLUE + "[SERVER] " + ChatColor.WHITE;

	public SafePVPListener(SafePVP plugin) {
		this.plugin = plugin;
	}

	public boolean canUseCommand(String b) {
		return true;
	}
	public void messageAll(String message) {
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (Player p : players) {
			p.sendMessage(message);
		}
	}
	public boolean arraySearch(Player[] list, Player target) {
		for (Player p : list)
			if (p.equals(target)) {
				found = true;
			}
		return found;
	}



	@Override
	public void onPlayerJoin(PlayerEvent event) {
		String player = event.getPlayer().getName();
		if (defaultPvpOff) 
			noPvp.add(player);
		else 
			yesPvp.add(player);

		if (defaultMessageOff) 
			noMsg.add(player);
		else 
			yesMsg.add(player);

	}

	@Override
	public void onPlayerQuit(PlayerEvent event) {
		String player = event.getPlayer().getName();
		if (yesPvp.contains(player)) {
			yesPvp.remove(player);
		}
		if (noPvp.contains(player)) {
			noPvp.remove(player);
		}
		if (noMsg.contains(player)) {
			noMsg.remove(player);
		}
		if (yesMsg.contains(player)) {
			yesMsg.remove(player);
		}
	}

	@Override
	public void onPlayerCommand(PlayerChatEvent event) {
		String[] split = event.getMessage().split(" ");
		Player player = event.getPlayer();
		if (split[0].equals("/pvp")) {
			if (canUseCommand("/pvp")) {
				if (split.length == 1) { player.sendMessage(prefix + "Type /pvp help for more info."); } else {
					if (split[1].equals("help") && split.length == 2) {
						player.sendMessage(prefix + "Commands for SafePVP are as follow:");
						player.sendMessage(prefix + "/pvp [on|off|status] - Sets your status or shows it.");
						player.sendMessage(prefix + "/pvp [mon|moff|mstatus] - Sets onDamage message spam on/off, or shows status");
						player.sendMessage(prefix + "/pvp list - Shows everyone on the yes PVP and no PVP list.");
						//						if (player.canUseCommand("/pvpa")) {
						player.sendMessage(prefix + "/pvpa [player] [on|off|status|mstatus] - Sets the player's ");
						player.sendMessage(prefix + "status or shows it to you. mstatus shows the messaging status of");
						player.sendMessage(prefix + "the player. Leaving the last argument blank will show PVP status.");
						player.sendMessage(prefix + "Type /pvp help 2 for more information.");		
					}
					if (split[1].equals("help") && split.length == 3) { 
						//							if (split[2] != null && split[2].equals("2") && player.canUseCommand("/pvpa")) {
						if (split[2] != null && split[2].equals("2")) {

							player.sendMessage(prefix + "/pvps [dmon|dmoff] - Turns death messages on/off for the server.");
						}
					}

					if (split[1].equals("on")) {
						if (noServer.contains(player.getName())) { player.sendMessage(prefix + "Your toggleable PVP status has been revoked by the server"); } else {
							if (noPvp.contains(player.getName())) {
								noPvp.remove(player.getName());
								yesPvp.add(player.getName());
								player.sendMessage(prefix + "You have been added to the PVP list.");
								on = true;
								new Thread() {
									@Override
									public void run() {
										try {
											Thread.sleep(defaultToggleDelay * 1000);
											on = false;
										} catch (InterruptedException e) {
										}
									}
								}.start();
							} else if (yesPvp.contains(player.getName())){
								player.sendMessage(prefix + "You have are already on the PVP list.");
							} else {
								yesPvp.add(player.getName());
								player.sendMessage(prefix + "You have been added to the PVP list.");
								on = true;
								new Thread() {
									@Override
									public void run() {
										try {
											Thread.sleep(defaultToggleDelay * 1000);
											on = false;
										} catch (InterruptedException e) {
										}
									}
								}.start();

							}
						}
					}
					if (split[1].equals("off")) {
						if (noServer.contains(player.getName())) { player.sendMessage(prefix + "You can't toggle your PVP status!"); } else {
							if (on) { player.sendMessage(prefix + "You are currently on the on/off cooldown."); } else {
								if (yesPvp.contains(player.getName())) {
									yesPvp.remove(player.getName());
									noPvp.add(player.getName());
									player.sendMessage(prefix + "You have been added to the no PVP list.");
								} else if (noPvp.contains(player.getName())) {
									player.sendMessage(prefix + "You are already on the no PVP list!");
								} else {
									noPvp.add(player.getName());
									player.sendMessage(prefix + "You have been added to the no PVP list.");
								}
							}
						}
					}
					if (split[1].equals("status")) {
						if (yesPvp.contains(player.getName())) {
							player.sendMessage(prefix + "You are on the PVP list!"); //200
						} else if (noPvp.contains(player.getName())) {
							player.sendMessage(prefix + "You are on the no PVP list!");
						} else {
							player.sendMessage(prefix + "Error when retrieving status. Type /pvp [on|off]");	
						}

					}
					if (split[1].equals("list")) {
						player.sendMessage(prefix + "People on the no PVP list: " + noPvp.toString());
						player.sendMessage(prefix + "People on the PVP list: " + yesPvp.toString());
					}
					if (split[1].equals("moff")) {
						if (yesMsg.contains(player.getName())) {
							yesMsg.remove(player.getName());
							noMsg.add(player.getName());
							player.sendMessage(prefix + "You have been added to the no messaging list.");
						} else if (noMsg.contains(player.getName())) {
							player.sendMessage(prefix + "You are already on the no messaging list!");
						} else {
							noMsg.add(player.getName());
							player.sendMessage(prefix + "You have been added to the no messaging list.");
						}
					}
					if (split[1].equals("mon")) {
						if (noMsg.contains(player.getName())) {
							noMsg.remove(player.getName());
							yesMsg.add(player.getName());
							player.sendMessage(prefix + "You have been added to the messaging list.");
						} else if (yesMsg.contains(player.getName())){
							player.sendMessage(prefix + "You have are already on the messaging list.");
						} else {
							yesMsg.add(player.getName());
							player.sendMessage(prefix + "You have been added to the messaging list.");
						}
					}
					if (split[1].equals("mstatus")) {
						if (yesMsg.contains(player.getName())) {
							player.sendMessage(prefix + "You are on the messaging list!");
						} else if (noMsg.contains(player.getName())) {
							player.sendMessage(prefix + "You are on the no messaging list!");
						} else {
							player.sendMessage(prefix + "Error when retrieving status. Type /pvp [mon|moff]");	
						}
					}
					if (split[1].equalsIgnoreCase("iSpar")) {
						if (split.length == 4) {
							Player[] players = plugin.getServer().getOnlinePlayers();
							Player iTarget = plugin.getServer().getPlayer(split[3]);

							if (split[2].equals("start")) {
								if (iInProgress) {
									player.sendMessage(duel + "There is already an iSpar in progress!");
								} else {
									if (arraySearch(players, iTarget)) {
										if (iTarget.equals(player.getName()) || (!iTarget.getName().equals("DylanG") && !iTarget.getName().equals("Player"))) { player.sendMessage(duel + "You cannot start a duel with yourself!"); } else {
											iSparList.add(player.getName().toLowerCase());
											iSparList.add(iTarget.getName().toLowerCase());
											iValid = true;
											iWaiting = true;
											iStarter = player;
											iInProgress = true;
											player.sendMessage(duel + "Waiting for " + iTarget.getName() + "'s acceptance.");
											iTarget.sendMessage(duel + "Type /pvp iSpar accept " + player.getName() + " to accept the iSpar!");
											new Thread() {
												@Override
												public void run() {
													try {
														Thread.sleep(defaultISparTime * 1000);
														iValid = false;
													} catch (InterruptedException e) {
													}
												}
											}.start();											
										}
									} else {
										player.sendMessage(duel + "Cannot find the target.");
									}
								}
							}
							if (split[2].equals("accept")) {
								if (iWaiting) {
									if (iSparList.contains(player.getName().toLowerCase())) {
										if (split[3].equalsIgnoreCase(iStarter.getName())) {
											if (iValid) { // 284
												player.sendMessage(duel + "You have accepted " + iStarter.getName() + "'s iSpar!");
												iStarter.sendMessage(duel + player.getName() + " has accepted your iSpar!");
												messageAll(duel + "An iSpar has been started between " + iStarter.getName() + " and " + player.getName() + "!");
												iAccepted = true;
												//			if (canUseCommand("/" + teleportLocation)) { //@@@@@@@@
												//				player.command("/" + teleportLocation);
												//			} else {
												//				player.addGroup(iXGroup);
												//				player.command("/" + teleportLocation);
												//				player.removeGroup(iXGroup); 
												//			}
												//			if (iStarter.canUseCommand("/" + teleportLocation)) {
												//				iStarter.command("/" + teleportLocation);
												//			} else {
												//				iStarter.addGroup(iXGroup);
												//				iStarter.command("/" + teleportLocation);
												//				iStarter.removeGroup(iXGroup); 
												//			}
											} else {
												player.sendMessage(duel + defaultISparTime + " seconds have passed. Offer is now invalid.");
												iInProgress = false;
											}
										} else {
											player.sendMessage(duel + "Wrong person to start with!");
										}
									} else {
										player.sendMessage(duel + "The iSpar in progress isn't for you!");
									}
								}	
							}
						}
					}
					if (split[1].equals("spar")) {
						if (split.length == 4) {
							Player[] players = plugin.getServer().getOnlinePlayers();
							Player target = plugin.getServer().getPlayer(split[3]);
							//		int balance = Hooked.getInt("iBalance", new Object[] { "balance", player.getName() });
							//		int newBalance = (balance+iWin);
							//		Hooked.silent("iBalance", new Object[] { "set", player.getName(), newBalance }); 
							if (split[2].equals("start")) {
								if (inProgress) {
									player.sendMessage(spar + "There is already a duel in progress!");
								} else {
									//								for (Player p : players) if (p.equals(target)) found = true;
									if (arraySearch(players, target)) {
										if (target.equals(player.getName()) || !target.getName().equals("DylanG")) { player.sendMessage(spar + "You cannot start a duel with yourself!"); } else {
											sparList.add(player.getName().toLowerCase());
											sparList.add(target.getName().toLowerCase());
											valid = true;
											waiting = true;
											starter = player;
											inProgress = true;
											player.sendMessage(spar + "Waiting for " + target.getName() + "'s acceptance.");
											target.sendMessage(spar + "Type /pvp spar accept " + player.getName() + " to accept the spar!");
											new Thread() {
												@Override
												public void run() {
													try {
														Thread.sleep(defaultSparTime * 1000);
														valid = false;
													} catch (InterruptedException e) {
													}
												}
											}.start();
										}
									} else {
										player.sendMessage(spar + "Cannot find the target.");
									}
								}
							}
							if (split[2].equals("accept")) {
								if (waiting) {
									if (sparList.contains(player.getName().toLowerCase())) {
										if (split[3].equalsIgnoreCase(starter.getName())) {
											if (valid) {
												player.sendMessage(spar + "You have accepted " + starter.getName() + "'s spar!");
												starter.sendMessage(spar + player.getName() + " has accepted your spar!");
												//					etc.getServer().messageAll(spar + "A spar has been started between " + starter.getName() + " and " + player.getName() + "!");
												accepted = true;
												//					if (player.canUseCommand("/" + teleportLocation)) {
												//						player.command("/" + teleportLocation);
												//					} else {
												//						player.addGroup(xGroup);
												//						player.command("/" + teleportLocation);
												//						player.removeGroup(xGroup); 
												//					}
												//					if (starter.canUseCommand("/" + teleportLocation)) {
												//						starter.command("/" + teleportLocation);
												//					} else {
												//						starter.addGroup(xGroup);
												//						starter.command("/" + teleportLocation);
												//						starter.removeGroup(xGroup); 
												//					}
											} else {
												player.sendMessage(spar + defaultSparTime + " seconds have passed. Offer is now invalid.");
												inProgress = false;
											}
										} else {
											player.sendMessage(spar + "Wrong person to start with!");
										}
									} else {
										player.sendMessage(spar + "The spar in progress isn't for you!");
									}
								}	
							}
						}
					}
					if (split[1].equalsIgnoreCase("forfeit")) {
						if (inProgress && accepted && valid && waiting) {
							inProgress = false;
							accepted = false;
							valid = false;
							waiting = false;
							sparList.clear();
							messageAll(prefixS + "The current spar was ended by the server.");
						}
					}
					if (split[1].equalsIgnoreCase("iforfeit")) {
						if (iInProgress && iAccepted && iValid && iWaiting) {
							iInProgress = false;
							iAccepted = false;
							iValid = false;
							iWaiting = false;
							iSparList.clear();
							messageAll(prefixS + duel + "The current iSpar was ended by the server.");
						}
					}
				}
			}
		}
		if (split[0].equals("/pvpa")) {
			if (canUseCommand("/pvpa")) {
				Player[] players = plugin.getServer().getOnlinePlayers();
				if (split.length < 2) {
					player.sendMessage(prefixA + "Invalid syntax. Correct is /pvpa [player] [on|off|status]");
					event.setCancelled(true);
				} else {
					Player target = plugin.getServer().getPlayer(split[1]);
					if (split.length < 3 && target != null ) {
						if (yesPvp.contains(target.getName())) {
							player.sendMessage(prefixA + target.getName() + " is on the PVP list. ");
						} else if (noPvp.contains(target.getName())) {
							player.sendMessage(prefixA + target.getName() + " is on the no PVP list.");
						} else {
							player.sendMessage(prefixA + "Error when retrieving " + target.getName() + "'s status.");	
						}
					}
					if (arraySearch(players, target)) {
						if (split[2].equals("on")) {
							if (noPvp.contains(target.getName())) {
								noPvp.remove(target.getName());
								yesPvp.add(target.getName());
								target.sendMessage(prefix + "You have been added to the PVP list by " + ChatColor.YELLOW + player.getName() + ".");
								player.sendMessage(prefixA + target.getName() + " has been added to the PVP list.");
							} else if (yesPvp.contains(target.getName())){
								player.sendMessage(prefixA + target.getName() + " is already on the PVP list.");
							} else {
								yesPvp.add(target.getName());
								target.sendMessage(prefix + "You have been added to the PVP list by " + ChatColor.YELLOW + player.getName() + ".");
								player.sendMessage(prefixA + target.getName() + " has been added to the PVP list.");
							}
						} 
						if (split[2].equals("off")) {
							if (yesPvp.contains(target.getName())) {
								yesPvp.remove(target.getName());
								noPvp.add(target.getName());
								target.sendMessage(prefix + "You have been added to the no PVP list by " + ChatColor.YELLOW + player.getName() + ".");
								player.sendMessage(prefixA + target.getName() + " has been added to the no PVP list.");
							} else if (noPvp.contains(target.getName())) {
								player.sendMessage(prefixA + target.getName() + " is already on the no PVP list!");
							} else {
								noPvp.add(target.getName());
								target.sendMessage(prefix + "You have been added to the no PVP list by " + ChatColor.YELLOW + player.getName() + ".");
								player.sendMessage(prefixA + target.getName() + " has been added to the no PVP list.");
							}	
						}
						if (split[2].equals("status")) {
							if (yesPvp.contains(target.getName())) {
								player.sendMessage(prefixA + target.getName() + " is on the PVP list.");
							} else if (noPvp.contains(target.getName())) {
								player.sendMessage(prefixA + target.getName() + " is on the no PVP list.");
							} else {
								player.sendMessage(prefixA + "Error when retrieving " + target.getName() + "'s status.");	
							}
						}
						if (split[2].equals("mstatus")) {
							if (yesMsg.contains(target.getName())) {
								player.sendMessage(prefixA + target.getName() + " is on the messaging list.");
							} else if (noMsg.contains(target.getName())) {
								player.sendMessage(prefixA + target.getName() + " is on the no messaging list.");
							} else {
								player.sendMessage(prefixA + "Error when retrieving " + target.getName() + "'s status.");	
							}
						}
					} else {
						if (target != null)
							player.sendMessage(prefixA + "Couldn't find player " + target.getName() + ". Recheck spelling and capitalisation");
					}
				}
			}			 
		}
	}

	public class EntityEvents extends EntityListener {
		public void onEntityDamageByProjectile (EntityDamageByProjectileEvent event) {
			Entity att = event.getDamager();
			Entity def = event.getEntity();
			if((att instanceof PlayerEvent) && (def instanceof PlayerEvent)) {
				Player attacker = ((PlayerEvent) att).getPlayer();
				Player defender = ((PlayerEvent) def).getPlayer();
				if (noPvp.contains(defender.getName())) { 
					if (yesMsg.contains(defender.getName()))
					defender.sendMessage(prefix + attacker.getName() + "'s arrows are useless on you.");
					event.setCancelled(true);
				}
				if (noPvp.contains(attacker.getName())) {
					if (yesMsg.contains(attacker.getName()))
					attacker.sendMessage(prefix + defender.getName() + " is on the no PVP list!");
					event.setCancelled(true);
				}
				if (yesPvp.contains(attacker.getName()) && yesPvp.contains(defender.getName())) {
				}
			}
		}

		public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
			Entity att = event.getDamager();
			Entity def = event.getEntity();
			if((att instanceof PlayerEvent) && (def instanceof PlayerEvent)) {
				Player attacker = ((PlayerEvent) att).getPlayer();
				Player defender = ((PlayerEvent) def).getPlayer();
				int amount = event.getDamage();
				if (accepted) {
					if (finished) { event.setCancelled(true); } else {
						if (sparList.contains(attacker.getName().toLowerCase()) && sparList.contains(defender.getName().toLowerCase())) {
							if (defender.getHealth()-amount <= 0) {
								messageAll(spar + attacker.getName() + " has won the spar between " + defender.getName() + "! Congratulations!");
								finished = true;
								inProgress = false;
								accepted = false;
								valid = false;
								waiting = false;
								sparList.remove(attacker.getName().toLowerCase());
								sparList.remove(defender.getName().toLowerCase());
							}					

						}
					}
				}
				if (iAccepted) {
					if (iFinished) { event.setCancelled(true);  } else {
						if (iSparList.contains(attacker.getName().toLowerCase()) && iSparList.contains(defender.getName().toLowerCase())) {
							if (defender.getHealth()-amount <= 0) {
								messageAll(duel + attacker.getName() + " has won the iSpar between " + defender.getName() + "!");
								messageAll(duel + attacker.getName() + " has been credited " + iWin + " " + currency + "!");
								//	int balance = Hooked.getInt("iBalance", new Object[] { "balance", attacker.getName() });
								//	int newBalance = (balance+iWin);
								//	Hooked.silent("iBalance", new Object[] { "set", attacker.getName(), newBalance }); 
								iFinished = true;
								iInProgress = false;
								iAccepted = false;
								iValid = false;
								iWaiting = false;
								iSparList.remove(attacker.getName().toLowerCase());
								iSparList.remove(defender.getName().toLowerCase());
							}					

						}
					}
				}
				if (sparList.contains(defender.getName().toLowerCase()) && accepted) {
					attacker.sendMessage(spar + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " is participating in a spar and you aren't their partner.");			

				}
				if (sparList.contains(attacker.getName().toLowerCase()) && accepted) {
					attacker.sendMessage(spar + "You are sparring and " + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE +  " isn't your partner.");			

				}
				if (iSparList.contains(defender.getName().toLowerCase()) && iAccepted) {
					attacker.sendMessage(duel + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " is participating in an iSpar and you aren't their partner.");			

				}
				if (iSparList.contains(attacker.getName().toLowerCase()) && iAccepted) {
					attacker.sendMessage(duel + "You are iSparring and " + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " isn't your partner.");			

				}
				if (noPvp.contains(defender.getName())) {
					if (yesMsg.contains(defender.getName())) {
						defender.sendMessage(prefix + "The no PVP list protects you from " + ChatColor.DARK_AQUA + attacker.getName() + ChatColor.WHITE +  ".");
					}
					if (yesMsg.contains(attacker.getName())) {
						attacker.sendMessage(prefix + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " is on the no PVP list. You can't damage them.");			
					}

				}
				if (noPvp.contains(attacker.getName())) {
					if (yesMsg.contains(defender.getName())) {
						defender.sendMessage(prefix + ChatColor.DARK_AQUA + attacker.getName() + ChatColor.WHITE + " is on the no PVP list. You can't be damaged.");
					}
					if (yesMsg.contains(attacker.getName())) {
						attacker.sendMessage(prefix + "You are on the no PVP list. You can not damage!");						
					}

				}
				if (!noPvp.contains(attacker.getName()) && !yesPvp.contains(attacker.getName())) {
					if (yesMsg.contains(attacker.getName())) {
						attacker.sendMessage(prefix + "Type /pvp on to battle!");
					}

				}
				if (!noPvp.contains(defender.getName()) && !yesPvp.contains(defender.getName())) {
					if (yesMsg.contains(attacker.getName())) {
						attacker.sendMessage(prefix + ChatColor.DARK_AQUA + defender.getName() + ChatColor.WHITE + " hasn't enabled PVP yet!");
					}
					if (yesMsg.contains(defender.getName())) {
						defender.sendMessage(prefix + "Type /pvp on to battle or /pvp off to stay safe.");
					}

				}
				if (yesPvp.contains(attacker.getName()) && (yesPvp.contains(defender.getName()))) {
					if (defender.getHealth()-amount <= 0) {
						messageAll(prefix + ChatColor.RED + attacker.getName() + " has just killed " + defender.getName() + ".");
					}
				}
			}
		}
	}
}


