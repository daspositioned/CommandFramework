package net.seliba.commandframework.internal.tab;

import net.seliba.commandframework.CommandArgs;
import net.seliba.commandframework.api.ICommandFramework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BukkitTab implements Listener {
    private JavaPlugin plugin;
    private ICommandFramework framework;

    public BukkitTab(JavaPlugin plugin, ICommandFramework framework) {
        this.plugin = plugin;
        this.framework = framework;
    }

    @EventHandler
    public void onTab(TabCompleteEvent event) {
        String cmd =  String.join(".", event.getBuffer().replaceAll("/", "").toLowerCase().split(" "));
        List<String> suggestions = framework.getTabManager().get(cmd, new CommandArgs(event.getSender(), cmd.split("\\."), new String[0] , cmd, cmd.split("\\.").length - 1));
        if (suggestions != null) {
            event.getCompletions().addAll(suggestions);
        }else if (framework.getTabManager().getNextLowestCompleter(cmd).size() > 0) {
            String cmdLabel = framework.getTabManager().getNextLowestCompleter(cmd).get(0);
            CommandArgs args = new CommandArgs(event.getSender(), cmd.split("\\."), new String[0] , cmdLabel, cmdLabel.split("\\.").length - 1);
            event.getCompletions().addAll((framework.getTabManager().get(cmdLabel, args)));
        }
    }
}