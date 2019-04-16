package net.seliba.commandframework.internal.framework;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.api.ICommandFramework;
import net.seliba.commandframework.internal.command.AbstractCommandManager;
import net.seliba.commandframework.internal.command.BungeeCommand;
import net.seliba.commandframework.internal.command.BungeeCommandManager;
import net.seliba.commandframework.internal.tab.BungeeTab;
import net.seliba.commandframework.internal.tab.TabManager;

import java.lang.reflect.Method;

@Setter
@Getter
public class BungeeCommandFramework implements ICommandFramework {

  @Setter
  @Getter
  private Plugin plugin;

  private String noPermissionMessage = "§cyou are not allowed to perform this action";
  private String inGameOnlyMessage = "§cthis command can only be executed ingame";
  private String doesNotExist = "§cthis command does not exist!";

  private static TabManager tabManager;
  private static BungeeCommandManager commandManager;
  private static boolean loaded;

  public BungeeCommandFramework(Plugin plugin) {
    this.plugin = plugin;
    if (!loaded) {
      tabManager = new TabManager();
      commandManager = new BungeeCommandManager(plugin, this);
      this.plugin.getProxy().getPluginManager().registerListener(plugin, new BungeeTab(plugin, this));
      loaded = true;
    }
  }

  @Override
  public String[] getCommandLabels() {
    return commandManager.getEntryHashMap().keySet().toArray(new String[0]);
  }

  @Override
  public void registerCommand(Command command, Method method, Object commandObject) {
    commandManager.register(command, method, commandObject);
  }

  @Override
  public TabManager getTabManager() {
    return BungeeCommandFramework.tabManager;
  }

  @Override
  public AbstractCommandManager getCommandManager() {
    return BungeeCommandFramework.commandManager;
  }

  @Override
  public void unregisterCommand(Command command) {
    ProxyServer.getInstance().getPluginManager().unregisterCommand(new BungeeCommand(command.name().split("\\.")[0], this));
    for (String a : command.aliases()) {
      ProxyServer.getInstance().getPluginManager().unregisterCommand(new BungeeCommand(a.split("\\.")[0], this));
    }
  }
}
