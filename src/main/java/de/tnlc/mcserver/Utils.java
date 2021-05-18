package de.tnlc.mcserver;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Utils {
    public static List<String> getWorldNames() {
        List<String> result = new ArrayList<>();
        for (World w : Bukkit.getServer().getWorlds())
            result.add(w.getName());
        return result;
    }
}
