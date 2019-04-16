package net.seliba.commandframework;

import java.lang.reflect.Method;

import net.seliba.commandframework.internal.command.AbstractCommandManager;
import net.seliba.commandframework.internal.framework.BukkitCommandFramework;
import net.seliba.commandframework.internal.framework.BungeeCommandFramework;
import net.seliba.commandframework.internal.tab.TabManager;
import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.api.ICommandFramework;
import net.seliba.commandframework.api.PlatformType;

public class CommandFramework implements ICommandFramework {

  PlatformType platformType;
  ICommandFramework commandFramework;


  public CommandFramework(Object plugin) {
    try {
      Class.forName("net.md_5.bungee.api.plugin.Plugin");
      this.platformType = PlatformType.BUNGEE;
      this.commandFramework = new BungeeCommandFramework((net.md_5.bungee.api.plugin.Plugin) plugin);
    } catch (Exception e) {
      try {
        Class.forName("org.bukkit.plugin.java.JavaPlugin");
        this.platformType = PlatformType.BUKKIT;
        this.commandFramework = new BukkitCommandFramework((org.bukkit.plugin.java.JavaPlugin) plugin);
      } catch (Exception e1) {
        throw new NullPointerException("There is no Bukkit and no Bungee!");
      }
    }
  }


  @Override
  public void registerCommands(Object command) {
    this.commandFramework.registerCommands(command);
  }

  @Override
  public void unregisterCommand(Command command) {
    commandFramework.unregisterCommand(command);
  }

  @Override
  public String[] getCommandLabels() {
    return this.commandFramework.getCommandLabels();
  }

  @Override
  public void registerCommand(Command command, Method method, Object commandObject) {
    commandFramework.registerCommand(command, method, commandObject);
  }

  @Override
  public TabManager getTabManager() {
    return commandFramework.getTabManager();
  }

  @Override
  public AbstractCommandManager getCommandManager() {
    return this.commandFramework.getCommandManager();
  }

  @Override
  public void setInGameOnlyMessage(String msg) {
    this.commandFramework.setInGameOnlyMessage(msg);
  }

  @Override
  public String getInGameOnlyMessage() {
    return this.commandFramework.getInGameOnlyMessage();
  }

  @Override
  public void setNoPermissionMessage(String msg) {
    this.commandFramework.setNoPermissionMessage(msg);
  }

  @Override
  public String getNoPermissionMessage() {
    return this.commandFramework.getNoPermissionMessage();
  }

  @Override
  public void setDoesNotExist(String msg) {
    this.commandFramework.setDoesNotExist(msg);
  }

  @Override
  public String getDoesNotExist() {
    return commandFramework.getDoesNotExist();
  }
}
