package me.jose.playercounterplus;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.jose.playercounterplus.servers.BigServer;
import me.jose.playercounterplus.servers.Server;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderHook {

    Placeholders() {
        PlaceholderAPI.registerPlaceholderHook(Main.getInstance(), this);
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        for(Server server : Main.getInstance().getServers()) {
            if(identifier.equalsIgnoreCase(server.getName())) {
                return String.valueOf(server.getOnlinePlayers());
            }
        }
        for(BigServer bg : Main.getInstance().getBigServers()) {
            if(identifier.equalsIgnoreCase(bg.getName())) {
                return String.valueOf(bg.getOnlinePlayers());
            }
        }
        return "";
    }

}
