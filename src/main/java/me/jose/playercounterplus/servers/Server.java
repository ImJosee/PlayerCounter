package me.jose.playercounterplus.servers;

public class Server {

    private String name;
    private int online;

    public Server(String name) {
        this.name = name;
        this.online = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getOnlinePlayers() {
        return this.online;
    }

    void setOnlinePlayers(int value) {
        this.online = value;
    }

    @Override
    public boolean equals(Object value) {
        if(value instanceof Server) {
            return ((Server) value).getName().equalsIgnoreCase(getName());
        }
        return false;
    }
}
