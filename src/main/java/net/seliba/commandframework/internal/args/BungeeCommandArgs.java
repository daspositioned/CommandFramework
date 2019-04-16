package net.seliba.commandframework.internal.args;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.seliba.commandframework.api.ICommandArgs;

public class BungeeCommandArgs implements ICommandArgs {

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

  public BungeeCommandArgs(CommandSender sender, String[] args, String[] tags, String commandLabel,
      int subCommand) {
    String[] modArgs = new String[args.length - subCommand];
    for (int i = 0; i < args.length - subCommand; i++) {
      modArgs[i] = args[i + subCommand];
    }
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(commandLabel);
    for (int x = 0; x < subCommand; x++) {
      stringBuilder.append(".").append(args[x]);
    }
    String cmdLabel = stringBuilder.toString();
    this.sender = sender;
    this.arguments = modArgs;
    this.rawArguments = modArgs;
    this.tags = tags;
    this.commandLabel = cmdLabel;
  }

  @Override
  public <T> T getSender(Class<T> type) {
    if (type.isAssignableFrom(sender.getClass())) {
      return (T) sender;
    }
    return null;
  }
}
