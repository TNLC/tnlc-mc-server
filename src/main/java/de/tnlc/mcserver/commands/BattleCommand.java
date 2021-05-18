package de.tnlc.mcserver.commands;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.tnlc.mcserver.Constants;
import de.tnlc.mcserver.Plugin;
import de.tnlc.mcserver.listeners.BattleListener;

public class BattleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1)
            return false;
        else if (!(sender instanceof Player)) {
            sender.sendMessage(Constants.ONLY_PLAYERS);
            return true;
        }
        
        Player p = Bukkit.getPlayer(args[0]);
        Player senderPlayer = (Player) sender;

        if (p == senderPlayer) {
            sender.sendMessage(ChatColor.RED + "Du kannst dich nicht selbst herausfordern!");
            return true;
        }
        
        if (p == null) {
            sender.sendMessage(ChatColor.RED + "Der angegebene Spieler scheint nicht aktiv zu sein.");
            return true;
        }

        String senderName = ((Player) sender).getDisplayName();

        p.sendMessage(ChatColor.DARK_PURPLE + senderName + ChatColor.GOLD + " hat dich herausgefordert.");
        senderPlayer.sendMessage(ChatColor.GOLD + "Du hast " + ChatColor.DARK_PURPLE + args[0] + ChatColor.GOLD + " herausgefordert.");


        p.setGameMode(GameMode.SURVIVAL);
        p.setHealth(20);
        p.setFoodLevel(30);
        p.getInventory().clear();

        senderPlayer.setGameMode(GameMode.SURVIVAL);
        senderPlayer.setHealth(20);
        senderPlayer.setFoodLevel(30);
        senderPlayer.getInventory().clear();

        for (PotionEffect pe : p.getActivePotionEffects())
            p.removePotionEffect(pe.getType());
        for (PotionEffect pe : senderPlayer.getActivePotionEffects())
            senderPlayer.removePotionEffect(pe.getType());

        PotionEffect effects[] = {
            new PotionEffect(PotionEffectType.JUMP, 1000000, 5),
            new PotionEffect(PotionEffectType.SPEED, 1000000, 5),
            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 100),
        };

        for (int i = 0; i < effects.length; i++) {
            p.addPotionEffect(effects[i]);
            senderPlayer.addPotionEffect(effects[i]);
        }

        Bukkit.getPluginManager().registerEvents(
            new BattleListener(
                senderPlayer, p,
                buildArea(senderPlayer.getLocation(), p, senderPlayer)
            ), Plugin.getPlugin()
        );

        return true;
    }

    private List<Block> buildArea(Location loc, Player p1, Player p2) {
        List<Block> result = new ArrayList<>();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        for (int i1 = x - 7; i1 < x + 7; i1++) {
            for (int i2 = z - 7; i2 < z + 7; i2++) {
                Block b = loc.getWorld().getBlockAt(i1, 245, i2);
                b.setType(Material.GLASS);
                result.add(b);
            }
        }
        World w = p1.getWorld();
        p1.teleport(new Location(w, x - 5, 246, z - 5));
        p2.teleport(new Location(w, x + 5, 246, z + 5));
        return result;
    }
    
}
