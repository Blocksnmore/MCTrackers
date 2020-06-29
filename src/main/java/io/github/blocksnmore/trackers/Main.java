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

public class Main extends JavaPlugin implements Listener{
	FileConfiguration config = getConfig();
	HashMap<CommandSender, Player> track = new HashMap<CommandSender, Player>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (config.getBoolean("permission-req") && sender.hasPermission("trackers.track") || !config.getBoolean("permission-req")) {
	if (args.length == 0){
		sender.sendMessage(ChatColor.GREEN+"Trackers by Blocks_n_more#5526 \n"+ChatColor.GREEN+"/trackers track <player> or /trackers clear");
	}else if (args[0].equalsIgnoreCase("track")) {
	if (args.length == 1) {
		sender.sendMessage(ChatColor.RED+"You need to add a player!");
	}else {
		if(Bukkit.getServer().getPlayer(args[1]) != null) {
			sender.sendMessage(ChatColor.GREEN+"You are now tracking "+args[1]);
			if(((Player)sender).getInventory().contains(Material.COMPASS)) {
				sender.sendMessage(ChatColor.GREEN+"You did not have a compass so you were given one!");
				((Player)sender).getInventory().addItem(new ItemStack(Material.COMPASS));
			}
			Player p = Bukkit.getServer().getPlayer(args[1]);
			((Player) sender).setCompassTarget(p.getLocation());
			
			Player playe = Bukkit.getServer().getPlayer(sender.getName());
			track.put(playe, p);
		}else {
			sender.sendMessage(ChatColor.RED+"Player is not online!");
		}

	}
	}else if (args[0].equalsIgnoreCase("clear")){
		if(track.containsKey(sender)) {
			sender.sendMessage(ChatColor.GREEN+"You are no longer tracking anyone");
			track.remove(sender);
		}else {
			sender.sendMessage(ChatColor.RED+"You're not tracking anyone!");
		}
	}else {
		sender.sendMessage(ChatColor.RED+"I don't know that one");
	}
	}else {
		sender.sendMessage(ChatColor.GREEN+"Trackers by Blocks_n_more#5526 \n"+ChatColor.RED+"You do not have any sub command permissions");
	}
	return false;
	}
	
	@Override
	public void onEnable() {
		System.out.println(ChatColor.GREEN+"[Trackers] > A Public remake of of Dreams trackers created by Blocks_n_more#5526");
		System.out.println(ChatColor.GREEN+"[Trackers] > Registering Listeners");
		PluginManager manager = getServer().getPluginManager();
    	manager.registerEvents(this /* class of listener. this if it's your main class */, this/* your main class */);
    	System.out.println(ChatColor.GREEN+"[Trackers] > Registered Listeners");
    	System.out.println(ChatColor.GREEN+"[Trackers] > Loading config");
    	config.addDefault("permission-req", true);
    	config.addDefault("new-tracker", true);
        config.options().copyDefaults(true);
        saveDefaultConfig();
        System.out.println(ChatColor.GREEN+"[Trackers] > Loaded config");
        System.out.println(ChatColor.GREEN+"[Trackers] > Plugin is now enabled!");
	}
	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN+"[Trackers] > Plugin is now disabled");
	}
	@EventHandler
	public void onMove(PlayerInteractEvent e) {
		if(track.containsKey(e.getPlayer())) {
		if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {	
		Player player2name = track.get(e.getPlayer());
		if(player2name.getWorld() == e.getPlayer().getWorld()) {
		e.getPlayer().setCompassTarget(player2name.getLocation());
		e.getPlayer().sendMessage(ChatColor.GREEN+"You are tracking "+player2name.getName());
		}
		}
		}	
	}
	@EventHandler
	public void onDeath(PlayerRespawnEvent d){	
			if(config.getBoolean("new-tracker")) {
			Player p = d.getPlayer();
			if(track.containsKey(p)) {
			p.getInventory().addItem(new ItemStack(Material.COMPASS));
			}
		}
	}
}
