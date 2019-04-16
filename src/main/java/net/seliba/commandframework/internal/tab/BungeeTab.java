package net.seliba.commandframework.internal.tab;

import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.seliba.commandframework.CommandArgs;
import net.seliba.commandframework.api.ICommandFramework;

import java.util.List;

public class BungeeTab implements Listener {
    private Plugin plugin;
    private ICommandFramework framework;

    public BungeeTab(Plugin plugin, ICommandFramework framework) {
        this.plugin = plugin;
        this.framework = framework;
    }

    @EventHandler
    public void tab(TabCompleteEvent event) {
        String cmd =  String.join(".", event.getCursor().replaceAll("/", "").toLowerCase().split(" "));
        List<String> suggestions = framework.getTabManager().get(cmd, new CommandArgs(event.getSender(), cmd.split("\\."), new String[0] , cmd, cmd.split("\\.").length - 1));
        if (suggestions != null) {
            event.getSuggestions().addAll(suggestions);
        }else if (framework.getTabManager().getNextLowestCompleter(cmd).size() > 0) {
            String cmdLabel = framework.getTabManager().getNextLowestCompleter(cmd).get(0);
            CommandArgs args = new CommandArgs(event.getSender(), cmd.split("\\."), new String[0] , cmdLabel, cmdLabel.split("\\.").length - 1);
            event.getSuggestions().addAll(framework.getTabManager().get(cmdLabel, args));
        }
    }
}
