package de.tnlc.mcserver.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
            if (w1 != null) p.teleport(w1.getSpawnLocation());
            else p.kickPlayer(ChatColor.RED + "Deine Welt wird soeben gelöscht!");
        }

        File worldFolder = w.getWorldFolder();
        Bukkit.unloadWorld(w, false);
        worldFolder.delete();

        return true;
    }
    
}
