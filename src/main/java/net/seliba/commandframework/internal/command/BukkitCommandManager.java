package net.seliba.commandframework.internal.command;

import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.api.ICommandFramework;
import net.seliba.commandframework.internal.util.CommandDataHolder;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BukkitCommandManager extends AbstractCommandManager {

    private JavaPlugin plugin;
    private ICommandFramework commandFramework;
    private CommandMap commandMap;

    public BukkitCommandManager(JavaPlugin plugin, ICommandFramework commandFramework) {
        super();
        this.plugin = plugin;
        this.commandFramework = commandFramework;
        if (this.plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            final SimplePluginManager manager = (SimplePluginManager) this.plugin.getServer()
                    .getPluginManager();
            try {
                final Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                this.commandMap = (CommandMap) field.get(manager);
            } catch (final IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void register(String cmd, Command command, Method method, Object methodOrigin) {
        String lable = cmd.split("\\.")[0];

        if (this.commandMap.getCommand(lable) == null) {
            this.commandMap.register(lable, new BukkitCommand(lable, this.commandFramework));
        }

        if (!command.description().isEmpty())
            this.commandMap.getCommand(lable).setDescription(command.description());
        if (!command.usage().isEmpty())
            this.commandMap.getCommand(lable).setUsage(command.usage());


        getEntryHashMap().put(cmd, new CommandDataHolder(command, method, methodOrigin));
    }
}
