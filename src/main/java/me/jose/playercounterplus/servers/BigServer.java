package me.jose.playercounterplus.servers;

import java.util.Set;

public class BigServer {

    private String name;
    private Set<Server> servers;
    private int online_players;

    public BigServer(String name, Set<Server> servers) {
        this.name = name;
        this.servers = servers;
        for(Server sv : servers) {
            this.servers.add(sv);
            this.online_players += sv.getOnlinePlayers();
        }
    }

    public String getName() {
        return this.name;
    }

    public int getOnlinePlayers() {
        return this.online_players;
    }

    public Set<Server> getServers() {
        return this.servers;
    }
    public void setOnlinePlayers(int amount) {
        this.online_players = amount;
    }

}
