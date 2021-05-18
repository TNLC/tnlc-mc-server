package de.tnlc.mcserver;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.tnlc.mcserver.commands.BattleCommand;
import de.tnlc.mcserver.commands.DeleteWorldCommand;
import de.tnlc.mcserver.commands.RandWorldCommand;
import de.tnlc.mcserver.commands.SeedWorldCommand;
import de.tnlc.mcserver.commands.TPWorldCommand;

public class Plugin extends JavaPlugin {
    private static Plugin plugin;

    public void onEnable() {
        plugin = this;

        getCommand("tpw").setExecutor(new TPWorldCommand());
        getCommand("tpw").setTabCompleter(new TPWorldCommand.TC());

        getCommand("battle").setExecutor(new BattleCommand());

        getCommand("deletew").setExecutor(new DeleteWorldCommand());
        getCommand("deletew").setTabCompleter(new DeleteWorldCommand.TC());

        getCommand("seedworld").setExecutor(new SeedWorldCommand());

        getCommand("randworld").setExecutor(new RandWorldCommand());
    }

    public void onDisable() {
        HandlerList.unregisterAll();
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    
}
