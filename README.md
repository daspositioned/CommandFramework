**!!!! Commands die Mithilfe des commandframeworks erstellt wurden dürfen nicht in der plugin.yml registriert werden !!!!**


I created a commandframework based on the framework form minnymin3 and extended it onto Bungee as well. 



a CommandFramework can be created by the following: 
Bungee:
```java 
CommandFramework commandFramework = new CommandFramework(net.md_5.bungee.api.plugin.Plugin plugin);
```
Bukkit: 
```java 
CommandFramework commandFramework = new CommandFramework(org.bukkit.plugin.java.JavaPlugin plugin);
```
registering a Class with Commands,
this two examples work the Exact same way on Bukkit and on Bungee.

```java
//(Bukkit and Bungee)
commandFramework.registerCommands(new SomeCommandClass()); 
//This can be any Class what so ever, it does not have to have 
//any interfaces. The only thing it need is a @Command annotation.
```

The only thing a command Method needs is the `@Command` annotation and the `name = "name"` thing. 
```java
//(Bukkit and Bungee)
    @Command(name = "test.sub", aliases = { "test.subcommand"})
    public void testSub(CommandArgs args) {
        args.getSender(CommandSender.class).sendMessage("This is a test subcommand");
    }
```

This will create a sub command of test and will be executed when someone sends the command '/test sub' or '/test subcommand'. 

In a actual Project it would look like that: 

Main Class:
```java
package me.test.test.bukkit;

import CommandFramework;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin {

  @Override
  public void onEnable() {
    CommandFramework framework = new CommandFramework(this);
    framework.registerCommands(new BukkitRandomCommand());
  }
}
```
CommandClass: 
```java
package me.test.test.bukkit;

import Command;
import CommandArgs;
import org.bukkit.command.CommandSender;

public class BukkitRandomCommand {

  @Command(name = "test.one")
  public void JustATestCommand(CommandArgs args) {
    args.getSender(CommandSender.class).sendMessage("test.one");
  }
}
```
*note: it works on bungee the exact same way except that there is a other import for Commandsender.

The Commandframework also has a tab Completer that registers commands and sub command automatically.
If you want you also can add custom Methods for tab completion that are called every time a user tabs on the
defined command. so if you say `name = "test.sub1"` at the completer the method will be called after this command is enterd.
This Methods do not need to be registered as long as they are in a registered command class. 
if they are not they can be registered by saying `commandFramework.registerCompleters(Object class);`

