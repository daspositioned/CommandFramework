package net.seliba.commandframework.internal.command;

import net.seliba.commandframework.CommandArgs;
import net.seliba.commandframework.api.ICommandFramework;
import net.seliba.commandframework.internal.util.CommandDataHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class BukkitCommand extends Command {

  private ICommandFramework commandFramework;

  public BukkitCommand(String name, ICommandFramework commandFramework) {
    super(name);
    this.commandFramework = commandFramework;
  }

  @Override
  public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
    String command = commandLabel;
    if (args.length > 0) {
      command = command + "." + String.join(".", args);
    }
    String lable = commandFramework.getCommandManager().getCommandString(command);
    CommandDataHolder holder = commandFramework.getCommandManager().get(command);

    //checks if a command exists
    if (holder == null) {
      commandSender.sendMessage(commandFramework.getDoesNotExist());
      return true;
    }

    //checks if sender is a console or a player
    if (!(commandSender instanceof Player) && holder.getCommand().inGameOnly()) {
      commandSender.sendMessage(holder.getCommand().inGameOnlyMessage().isEmpty()
              ? commandFramework.getInGameOnlyMessage()
              : holder.getCommand().inGameOnlyMessage());
      return true;
    }

    //checks for Permission
    if ((commandSender instanceof Player)
        && !holder.getCommand().permission().isEmpty()
        && !commandSender.hasPermission(holder.getCommand().permission())) {
      commandSender.sendMessage(holder.getCommand().noPerm().isEmpty()
              ? commandFramework.getNoPermissionMessage()
              : holder.getCommand().noPerm());
          return true;
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
    return true;
  }
}