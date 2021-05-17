package de.tnlc.mcserver.commands;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.tnlc.mcserver.Plugin;
import net.md_5.bungee.api.ChatColor;

public class SeedWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 && args.length != 2) return false;
        
        String seed = args[0];

        long realSeed;

        try {
            realSeed = Long.valueOf(seed);
        } catch (NumberFormatException e) {
            realSeed = seed.hashCode();
        }


        sender.sendMessage(ChatColor.GOLD + "Welt wird generiert...");

        Plugin.getPlugin().getLogger().info("generating world with seed '" + seed + "' ("+ realSeed +") ...");
        

        WorldCreator worldCreator = new WorldCreator(args.length == 2 ? args[1] : args[0]);
        worldCreator.seed(realSeed);
        worldCreator.createWorld();

        sender.sendMessage(ChatColor.GREEN + "Welt wurde generiert!");

        return true;
    }
    
}
