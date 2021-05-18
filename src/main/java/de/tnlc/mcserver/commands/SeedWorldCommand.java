package de.tnlc.mcserver.commands;

import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.tnlc.mcserver.Constants;
import de.tnlc.mcserver.Plugin;

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


        sender.sendMessage(Constants.WORLD_GEN);

        Plugin.getPlugin().getLogger().info("generating world with seed '" + seed + "' ("+ realSeed +") ...");
        

        WorldCreator worldCreator = new WorldCreator(args.length == 2 ? args[1] : args[0]);
        worldCreator.seed(realSeed);
        worldCreator.createWorld();

        sender.sendMessage(Constants.WORLD_GEN_DONE);

        return true;
    }
    
}
