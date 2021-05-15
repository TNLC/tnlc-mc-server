package de.tnlc.mcserver;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Plugin extends JavaPlugin {
    private static Plugin plugin;

    public void onEnable() {
        plugin = this;
        getCommand("battle").setExecutor(new BattleCommand());
    }

    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static class BattleListener implements Listener {
        private Player p1, p2;
        // private int p1Score, p2Score;
        private List<Block> area;

        public BattleListener(Player p1, Player p2, List<Block> area) {
            this.p1 = p1;
            this.p2 = p2;
            this.area = area;
        }

        @EventHandler
        private void onPlayerDie(PlayerDeathEvent event) {
            handleDeath(event.getEntity());
        }

        @EventHandler
        private void onPlayerMove(PlayerMoveEvent event) {
            Player p = event.getPlayer();
            if (p == p1 || p == p2)
                if (p.getLocation().getY() < 240)
                    handleDeath(p);
        }

        public void handleDeath(Player p) {
            boolean b = false;

            String winMsg = ChatColor.GREEN + "Du hast die Herausforderung gewonnen!";
            String loseMsg = ChatColor.RED + "Du hast die Herausforderung verloren!";
            if (p == p1) {
                p1.sendMessage(loseMsg);
                p2.sendMessage(winMsg);
                b = true;
            } else if (p == p2) {
                p1.sendMessage(winMsg);
                p2.sendMessage(loseMsg);
                b = true;
            }
            if (b) {
                for (PotionEffect pe : p1.getActivePotionEffects())
                    p1.removePotionEffect(pe.getType());
                for (PotionEffect pe : p2.getActivePotionEffects())
                    p2.removePotionEffect(pe.getType());
                disposeArea();
                unregister();
            }
        }

        public void disposeArea() {
            for (Block b : area) b.setType(Material.AIR);
        }

        public void unregister() {
            HandlerList.unregisterAll(this);
        }
    }

    public static class BattleCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length != 1)
                return false;
            else if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Das können nur Spieler.");
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

            ;

            Bukkit.getPluginManager().registerEvents(
                new BattleListener(senderPlayer, p, buildArea(senderPlayer.getLocation(), p, senderPlayer)), plugin);

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
}
