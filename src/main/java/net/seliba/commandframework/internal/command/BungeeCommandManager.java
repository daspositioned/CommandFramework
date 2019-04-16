package net.seliba.commandframework.internal.command;

import net.md_5.bungee.api.plugin.Plugin;
import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.api.ICommandFramework;
import net.seliba.commandframework.internal.util.CommandDataHolder;

import java.lang.reflect.Method;

public class BungeeCommandManager extends AbstractCommandManager {

    private Plugin plugin;
    private ICommandFramework commandFramework;

    public BungeeCommandManager(Plugin plugin, ICommandFramework commandFramework) {
        this.plugin = plugin;
        this.commandFramework = commandFramework;
    }

    @Override
    public void register(String cmd, Command command, Method method, Object methodOrigin) {
        String label = cmd.split("\\.")[0];
        this.plugin.getProxy().getPluginManager().registerCommand(plugin, new BungeeCommand(label, this.commandFramework));

        getEntryHashMap().put(cmd, new CommandDataHolder(command, method, methodOrigin));
    }
}