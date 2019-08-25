package me.jose.playercounterplus;
import me.jose.playercounterplus.commands.MainCommand;
import me.jose.playercounterplus.servers.BigServer;
import me.jose.playercounterplus.servers.PluginMessage;
import me.jose.playercounterplus.servers.Server;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin {

    private Set<Server> servers = new HashSet<>();
    private Set<BigServer> bigServers = new HashSet<>();

    @Override
    public void onEnable() {
        File config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        loadServers();

        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                for (Server sv : servers) {
                    new BukkitRunnable() {
                        public void run() {
                            PluginMessage.requestPlayerCount(sv.getName());
                        }
                    }.runTask(Main.this);
                }
            }
        }, 20L, 20L);

        new Placeholders();
        registerCommand(new MainCommand());

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§6PlayerCounter by §aJose");
        Bukkit.getConsoleSender().sendMessage("§6Loaded §a"+servers.size()+" §6servers and");
        Bukkit.getConsoleSender().sendMessage("§a"+bigServers.size()+" §6big servers groups.");
        Bukkit.getConsoleSender().sendMessage("");
    }

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    public Set<Server> getServers() {
        return this.servers;
    }

    private void loadServers() {
        for(String section : getConfig().getConfigurationSection("servers").getKeys(false)) {
            List<String> servers = getConfig().getStringList("servers."+section);
            Set<Server> sv_list = new HashSet<>();
            for(String server : servers) {
                Server server1;
                if(this.servers.stream().noneMatch(sv -> sv.getName().equalsIgnoreCase(server))) {
                    server1 = new Server(server);
                    this.servers.add(server1);
                }
                if(servers.size() > 1) {
                    server1 = this.servers.stream().filter(sev -> sev.getName().equalsIgnoreCase(server)).findAny().orElse(null);
                    sv_list.add(server1);
                }
            }
            if(servers.size() > 1) {
                this.bigServers.add(new BigServer(section, sv_list));
            }
        }
    }

    public Set<BigServer> getBigServers() {
        return this.bigServers;
    }

    private void registerCommand(BukkitCommand command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        reloadConfig();
        servers.clear();
        loadServers();
    }

}
