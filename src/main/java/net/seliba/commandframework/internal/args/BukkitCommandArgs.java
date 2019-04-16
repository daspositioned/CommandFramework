package net.seliba.commandframework.internal.args;

import lombok.Getter;
import net.seliba.commandframework.api.ICommandArgs;
import org.bukkit.command.CommandSender;

public class BukkitCommandArgs implements ICommandArgs {

  @Getter
  private CommandSender sender;
  @Getter
  private String[] arguments;
  @Getter
  private String commandLabel;
  @Getter
  private String[] rawArguments;
  @Getter
  private String[] tags;

  public BukkitCommandArgs(CommandSender sender, String[] arguments, String[] tags,
      String commandLabel, int subCommand) {
    String[] modifiedArgs = new String[arguments.length - subCommand];
    for (int i = 0; i < arguments.length - subCommand; i++) {
      modifiedArgs[i] = arguments[i + subCommand];
    }

    this.sender = sender;
    this.commandLabel = commandLabel;

    this.arguments = modifiedArgs;
    this.rawArguments = modifiedArgs;
    this.tags = tags;
  }

  @Override
  public <T> T getSender(Class<T> type) {
    if (type.isAssignableFrom(sender.getClass())) {
      return (T) sender;
    }
    return null;
  }
}