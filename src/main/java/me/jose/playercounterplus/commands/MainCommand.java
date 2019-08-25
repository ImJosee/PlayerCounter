package me.jose.playercounterplus.commands;

import me.jose.playercounterplus.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;

public class MainCommand extends BukkitCommand {

    public MainCommand() {
        super("playercounter");

        setAliases(Arrays.asList("pc", "playerc"));
        setDescription("Main command of playercounter");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§a§m-----------------------");
            sender.sendMessage("§6§l» §a/pc reload §7| Recarga la configuración.");
            sender.sendMessage("§a§m-----------------------");
            return true;
        }
        switch (args[0]) {
            case "reload": {
                if (!sender.hasPermission("playercounter.admin")) {
                    sender.sendMessage("§cNo tienes permisos para hacer esto.");
                    return true;
                }
                Main.getInstance().reload();
                sender.sendMessage("§aConfiguración recargada correctamente.");
                break;
            }
        }
        return true;
    }
}
