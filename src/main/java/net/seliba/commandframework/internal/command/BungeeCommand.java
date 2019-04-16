package net.seliba.commandframework.internal.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.seliba.commandframework.CommandArgs;
import net.seliba.commandframework.internal.util.CommandDataHolder;
import net.seliba.commandframework.api.ICommandFramework;

import java.lang.reflect.InvocationTargetException;

public class BungeeCommand extends Command {

  private String commandLabel;
  private ICommandFramework commandFramework;

  public BungeeCommand(String name, ICommandFramework commandFramework) {
    super(name);
    commandLabel = name;
    this.commandFramework = commandFramework;
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    String command = commandLabel;
    if (args.length > 0) {
      command = command + "." + String.join(".", args);
    }
    String lable = commandFramework.getCommandManager().getCommandString(command);
    CommandDataHolder holder = commandFramework.getCommandManager().get(command);

    //checks if a command exists
    if (holder == null) {
      commandSender.sendMessage(commandFramework.getDoesNotExist());
      return;
    }

    //checks if sender is a console or a player
    if (!(commandSender instanceof ProxiedPlayer) && holder.getCommand().inGameOnly()) {
      commandSender.sendMessage(holder.getCommand().inGameOnlyMessage().isEmpty()
              ? commandFramework.getInGameOnlyMessage()
              : holder.getCommand().inGameOnlyMessage());
      return;
    }

    //checks for Permission
    if ((commandSender instanceof ProxiedPlayer)
            && !holder.getCommand().permission().isEmpty()
            && !commandSender.hasPermission(holder.getCommand().permission())) {
      commandSender.sendMessage(holder.getCommand().noPerm().isEmpty()
              ? commandFramework.getNoPermissionMessage()
              : holder.getCommand().noPerm());
      return;
    }

    try {
      holder.getMethod().invoke(holder.getOrigin(), new CommandArgs(
              commandSender,
              args,
              holder.getCommand().tags(),
              command.split("\\.")[0],
              lable.split("\\.").length-1));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return;
  }
}