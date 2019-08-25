package me.jose.playercounterplus.servers;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jose.playercounterplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if(!channel.equals("BungeeCord")) {
            return;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            if(subchannel.equals("PlayerCount")) {
                String server = in.readUTF();
                int players = in.readInt();
                for(Server sv : Main.getInstance().getServers()) {
                    if(sv.getName().equalsIgnoreCase(server)) {
                        sv.setOnlinePlayers(players);
                    }
                }
                for(BigServer big_sv : Main.getInstance().getBigServers()) {
                    int online = 0;
                    for(Server svs : big_sv.getServers()) {
                        online += svs.getOnlinePlayers();
                    }
                    big_sv.setOnlinePlayers(online);
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§cThere is a server that doesnt exists in your BungeeCord on the §econfig.yml");
        }
    }

    public static void requestPlayerCount(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if(p != null) {
            p.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
        }
    }

}
