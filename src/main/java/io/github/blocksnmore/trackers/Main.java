package io.github.blocksnmore.trackers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{ //Start of main class
	FileConfiguration config = getConfig(); // Config file so i don't have to fetch it every request
	
	HashMap<CommandSender, Player> track = new HashMap<CommandSender, Player>(); // The tracker storage
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { // Command
		
	if (config.getBoolean("permission-req") && // Check if permission requirement is on in the config
	sender.hasPermission("trackers.track") || // Check if player has permission
	!config.getBoolean("permission-req")) { // Or check if config has permissions disabled
		
	if (args.length == 0){ // Check for no arguments
		sender.sendMessage(ChatColor.GREEN+"Trackers by Blocks_n_more#5526 \n"+ChatColor.GREEN+"/trackers track <player> or /trackers clear"); // No args message
		
	}else if (args[0].equalsIgnoreCase("track")) { // track argument
		
	if (args.length == 1) { // Check if there is no player provided
		sender.sendMessage(ChatColor.RED+"You need to add a player!"); // No player message
	}else {
		if(Bukkit.getServer().getPlayer(args[1]) != null) { // Check if the player is online
			
			sender.sendMessage(ChatColor.GREEN+"You are now tracking "+args[1]); // Tracking message
			if(!((Player)sender).getInventory().contains(Material.COMPASS) || !config.getBoolean("give-compass")) { // Check if player doesn't have a compass and give-compass is enabled
				
				sender.sendMessage(ChatColor.GREEN+"You did not have a compass so you were given one!"); // Give compass message
				((Player)sender).getInventory().addItem(new ItemStack(Material.COMPASS)); // Gives compass
			}
			Player p = Bukkit.getServer().getPlayer(args[1]); // Get player your tracking
			((Player) sender).setCompassTarget(p.getLocation()); // Set Location of compass
			
			Player playe = Bukkit.getServer().getPlayer(sender.getName()); // Get the player
			track.put(playe, p); // Store the player for updating
		}else {
			sender.sendMessage(ChatColor.RED+"Player is not online!"); // message if player isn't on
		}
	}
	
	}else if (args[0].equalsIgnoreCase("clear")){ // clear argument
		if(track.containsKey(sender)) { // Check if they are tracking someone
			sender.sendMessage(ChatColor.GREEN+"You are no longer tracking anyone"); // Stop tracking message
			track.remove(sender); // Remove player
		}else {
			sender.sendMessage(ChatColor.RED+"You're not tracking anyone!"); // Not tracking message
		}
	}else {
		sender.sendMessage(ChatColor.RED+"I don't know that one"); // Unknown Argument
	}
	}else {
		sender.sendMessage(ChatColor.GREEN+"Trackers by Blocks_n_more#5526 \n"+ChatColor.RED+"You do not have any sub command permissions"); // Missing Permissions message
	}
	return false;
	}
	
	@Override
	public void onEnable() { // Plugin enable
		
		System.out.println(ChatColor.GREEN+"[Trackers] > A Public remake of of Dreams trackers created by Blocks_n_more#5526"); // Loading messages
		System.out.println(ChatColor.GREEN+"[Trackers] > Registering Listeners"); // Register Listener message
		PluginManager manager = getServer().getPluginManager(); // Get Plugin manager
    	manager.registerEvents(this /* class of listener. this if it's your main class */, this/* your main class */); // Register Listener
    
    	
    	System.out.println(ChatColor.GREEN+"[Trackers] > Registered Listeners"); // Register Listener message
    	System.out.println(ChatColor.GREEN+"[Trackers] > Loading config"); // Load Config
    	config.addDefault("permission-req", true); // Register 1st config option
    	config.addDefault("new-tracker", true); // Register 2nd config option
    	config.addDefault("give-compass", true); // Register 3rd config option
        config.options().copyDefaults(true); // Initialize Config
        saveDefaultConfig(); // Save config
        
        System.out.println(ChatColor.GREEN+"[Trackers] > Loaded config"); // Loaded config message
        
        System.out.println(ChatColor.GREEN+"[Trackers] > Plugin is now enabled!"); // Plugin enabled message
        String ver;
		ver = checkVer();
		
        if(ver.equalsIgnoreCase("el")) {
        	System.out.println(ChatColor.RED+"[Trackers] > \"Extreme Legacy\" Version detected. This version is use-at-your-own-risk, Any bugs will not be fixed for these versions");
        }else if(ver.equalsIgnoreCase("l")) {
        System.out.println(ChatColor.GREEN+"[Trackers] > \"Legacy\" Version detected. Be aware there may be slight issues, if you find an issue report it here https://github.com/Blocksnmore/Dream-Trackers-Remake/issues "+
        		ChatColor.GREEN+"\n1.8 issues will likely not be fixed unless it's a critical bug");
        }else {
        	System.out.println(ChatColor.GREEN+"[Trackers] > 1.9+ detected, Most features should work without issues, if you find an issue report it here https://github.com/Blocksnmore/Dream-Trackers-Remake/issues");
        }
	}


	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN+"[Trackers] > Thanks for using this plugin!");
		System.out.println(ChatColor.GREEN+"[Trackers] > The plugin is now disabled"); // Plugin disable message
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) { // Right Click event
		if(track.containsKey(e.getPlayer())) { // Check if player has a tracker
		Player user = e.getPlayer();
		Player player2name = track.get(e.getPlayer()); // Get player
		String ver;
		ver = checkVer();
		if(ver.equalsIgnoreCase("r")) { // Check if version is "legacy"
		if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) { // Check if the item they are holding is a compass 1.9+
		
		if(player2name.getWorld() == e.getPlayer().getWorld()) {
		user.setCompassTarget(player2name.getLocation()); // Set compass location
		user.sendMessage(ChatColor.GREEN+"You are tracking "+player2name.getName()); // Tracking message
		}else { // If the player is not in the same world
			user.sendMessage(ChatColor.RED+"No player to track! They might be in a diffrent world"); // Player not in same world message
		}
		}
		}else {
			if(e.getPlayer().getInventory().getItemInHand().getType().equals(Material.COMPASS)) { // Check if the item they are holding is a compass 1.8
				
				if(player2name.getWorld() == e.getPlayer().getWorld()) {
				user.setCompassTarget(player2name.getLocation()); // Set compass location
				user.sendMessage(ChatColor.GREEN+"You are tracking "+player2name.getName()); // Tracking message
				}else { // If the player is not in the same world
					user.sendMessage(ChatColor.RED+"No player to track! They might be in a diffrent world"); // Player not in same world message
				}
				}	
			
		}
		
		}	
	}
	@EventHandler
	public void onDeath(PlayerRespawnEvent d){	// When a player respawns
			if(config.getBoolean("new-tracker")) { // If the option is enabled in the config
			Player p = d.getPlayer(); // Get player
			if(track.containsKey(p)) { // Check if player is using a tracker
			p.getInventory().addItem(new ItemStack(Material.COMPASS)); // Give compass
			}
		}
	}
	
	public String checkVer() {
		if(Bukkit.getBukkitVersion().contains("1.7") || 
			Bukkit.getBukkitVersion().contains("1.6") || 
			Bukkit.getBukkitVersion().contains("1.5") || 
			Bukkit.getBukkitVersion().contains("1.4") || 
			Bukkit.getBukkitVersion().contains("1.3") || 
			Bukkit.getBukkitVersion().contains("1.2") || 
			Bukkit.getBukkitVersion().contains("1.1") && !Bukkit.getBukkitVersion().contains("1.11")) { // Check if version is recent, legacy or extreme legacy
        	return "el";
        }else if(Bukkit.getBukkitVersion().contains("1.8")) {
        	return "l";
        }else {
        	return "r";
        }
	}
}
