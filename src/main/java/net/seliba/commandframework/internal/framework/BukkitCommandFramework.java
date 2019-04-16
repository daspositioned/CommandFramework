package net.seliba.commandframework.internal.framework;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.seliba.commandframework.internal.command.AbstractCommandManager;
import net.seliba.commandframework.internal.command.BukkitCommandManager;
import net.seliba.commandframework.internal.tab.BukkitTab;
import net.seliba.commandframework.internal.tab.TabManager;
import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.api.ICommandFramework;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

@Setter
@Getter
public class BukkitCommandFramework implements ICommandFramework {

  @Getter (AccessLevel.PRIVATE)
  @Setter (AccessLevel.PRIVATE)
  private JavaPlugin plugin;
  private String noPermissionMessage = "§cyou are not allowed to perform this action";
  private String inGameOnlyMessage = "§cthis command can only be executed ingame";
  private String doesNotExist = "§cthis command does not exist!";
  private static TabManager tabManager;
  private static BukkitCommandManager commandManager;
  private static boolean loaded;



  public BukkitCommandFramework(final JavaPlugin plugin) {
    this.plugin = plugin;
    if (!loaded) {
      tabManager = new TabManager();
      commandManager = new BukkitCommandManager(plugin, this);
      plugin.getServer().getPluginManager().registerEvents(new BukkitTab(plugin, this),plugin);
      loaded = true;
    }
  }

  @Override
  public void unregisterCommand(Command command) {
    try {
      Object result = getPrivateField(this.plugin.getServer().getPluginManager(), "commandMap");
      SimpleCommandMap commandMap = (SimpleCommandMap) result;
      Object map = getPrivateField(commandMap, "knownCommands");
      HashMap<String, org.bukkit.command.Command> knownCommands = (HashMap<String, org.bukkit.command.Command>) map;
      knownCommands.remove(command.name());
      for (String alias : command.aliases()){
        if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(plugin.getName())){
          knownCommands.remove(alias);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
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
    return BukkitCommandFramework.tabManager;
  }

  @Override
  public AbstractCommandManager getCommandManager() {
    return BukkitCommandFramework.commandManager;
  }

  private Object getPrivateField(Object object, String field)throws SecurityException,
          NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Class<?> clazz = object.getClass();
    Field objectField = clazz.getDeclaredField(field);
    objectField.setAccessible(true);
    Object result = objectField.get(object);
    objectField.setAccessible(false);
    return result;
  }

}
