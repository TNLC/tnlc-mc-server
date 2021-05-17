package de.tnlc.mcserver.listeners;

import org.bukkit.block.Block;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;

public class BattleListener implements Listener {
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
