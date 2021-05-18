package de.tnlc.mcserver.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.tnlc.mcserver.Utils;
import net.md_5.bungee.api.ChatColor;

public class TPWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Das dürfen nur Spieler!");
            return true;
        }
        
        World w = Bukkit.getWorld(args[0]);

        if (w == null) {
            sender.sendMessage(ChatColor.RED + "Die angegebene Welt existiert nicht.");
            return true;
        }
        
        ((Player) sender).teleport(w.getSpawnLocation());

        return true;
    }

    public static class TC implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return Utils.getWorldNames();
        }
    }

}
