package net.seliba.commandframework;

import net.seliba.commandframework.api.ICommandArgs;
import net.seliba.commandframework.api.PlatformType;
import net.seliba.commandframework.internal.args.BukkitCommandArgs;
import net.seliba.commandframework.internal.args.BungeeCommandArgs;

public class CommandArgs implements ICommandArgs {

  PlatformType platformType;
  ICommandArgs commandArgs;


  public CommandArgs(Object sender, String[] arguments, String[] tags, String commandLabel,
      int subCommand) {
    try {
      Class.forName("net.md_5.bungee.api.plugin.Plugin");
      platformType = PlatformType.BUNGEE;
      commandArgs = new BungeeCommandArgs((net.md_5.bungee.api.CommandSender) sender, arguments,
          tags, commandLabel, subCommand);
    } catch (ClassNotFoundException e) {
      try {
        Class.forName("org.bukkit.plugin.java.JavaPlugin");
        platformType = PlatformType.BUKKIT;
        commandArgs = new BukkitCommandArgs((org.bukkit.command.CommandSender) sender, arguments,
            tags, commandLabel, subCommand);
      } catch (ClassNotFoundException e1) {
        throw new NullPointerException("There is no Bukkit and no Bungee!");
      }
    }
  }


  @Override
  public <T> T getSender(Class<T> type) {
    switch (platformType) {
      case BUKKIT:
        return (T) commandArgs.getSender(org.bukkit.command.CommandSender.class);
      case BUNGEE:
        return (T) commandArgs.getSender(net.md_5.bungee.api.CommandSender.class);
      default:
        return null;
    }
  }

  @Override
  public String[] getArguments() {
    return this.commandArgs.getArguments();
  }

  @Override
  public String getCommandLabel() {
    return this.commandArgs.getCommandLabel();
  }

  @Override
  public String[] getRawArguments() {
    return this.commandArgs.getRawArguments();
  }

  @Override
  public String[] getTags() {
    return this.commandArgs.getTags();
  }

  @Override
  public int getArgumentCount() {
    return this.commandArgs.getArgumentCount();
  }

  @Override
  public int getArgumentInt(int i) throws NumberFormatException {
    return this.commandArgs.getArgumentInt(i);
  }

  @Override
  public double getArgumentDouble(int i) throws NumberFormatException {
    return this.commandArgs.getArgumentDouble(i);
  }

  @Override
  public String getArgument(int i) {
    return commandArgs.getRawArgument(i);
  }

  @Override
  public String getJoinedString(int i) {
    return commandArgs.getJoinedString(i);
  }
}
