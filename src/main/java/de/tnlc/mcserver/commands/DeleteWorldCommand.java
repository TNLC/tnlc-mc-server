package de.tnlc.mcserver.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class DeleteWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;
        
        World w = Bukkit.getWorld(args[0]);

        if (w == null) {
            sender.sendMessage(ChatColor.RED + "Die angegebene Welt existiert nicht.");
            return true;
        }

        World w1 = Bukkit.getWorld("world");
        for (Player p : w.getPlayers()) {
            if (w1 != null && args[0] != "world") p.teleport(w1.getSpawnLocation());
            else p.kickPlayer(ChatColor.RED + "Deine Welt wird soeben gelöscht!");
        }

        File worldFolder = w.getWorldFolder();
        Bukkit.unloadWorld(w, false);
        worldFolder.delete();

        return true;
    }
    
    public static class TC implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> worlds = new ArrayList<>();
            for (World w : Bukkit.getServer().getWorlds())
                worlds.add(w.getName());
            return worlds;
        }
    }
    
}
