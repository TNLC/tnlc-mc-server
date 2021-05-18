package de.tnlc.mcserver.commands;

import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.tnlc.mcserver.Constants;

public class RandWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;

        sender.sendMessage(Constants.WORLD_GEN);

        WorldCreator worldCreator = new WorldCreator(args[0]);
        worldCreator.createWorld();

        return true;
    }

}
