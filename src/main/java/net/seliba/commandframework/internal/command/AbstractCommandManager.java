package net.seliba.commandframework.internal.command;

import lombok.Getter;
import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.internal.util.CommandDataHolder;

import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractCommandManager {
    @Getter
    private HashMap<String, CommandDataHolder> entryHashMap;

    public AbstractCommandManager() {
        entryHashMap = new HashMap<>();
    }

    public abstract void register(String cmd, Command command, Method method, Object methodOrigin);

    public CommandDataHolder get(String name) {
        CommandDataHolder output = getEntryHashMap().get(name);
        if (output != null) {
            return output;
        }else{
            List<String> list = getNextLowestCommand(name);
            if (list.isEmpty()) {
                return null;
            }else {
                return getEntryHashMap().get(getNextLowestCommand(name).get(0));
            }
        }
    }

    public String getCommandString(String name) {
        if (getEntryHashMap().containsKey(name)) {
            return name;
        }else {
            List<String> list = getNextLowestCommand(name);
            if (list.isEmpty()) {
                return null;
            }else {
                return getNextLowestCommand(name).get(0);
            }
        }
    }

    public List<String> getNextLowestCommand(String command) {
        HashSet<String> list = new HashSet<>(entryHashMap.keySet());
        if (list.contains(command))
            return Arrays.asList(command);

        String cmd = command.replace(" ", ".").toLowerCase();
        List<String> output = new ArrayList<>();
        int highest = 0;
        for (String s : list) {
            if (cmd.toLowerCase().startsWith(s.toLowerCase())) {
                if (s.split("\\.").length > highest) {
                    output.clear();
                    output.add(s);
                    highest = s.split("\\.").length;
                }else if (s.split("\\.").length == highest) {
                    output.add(s);
                }
            }
        }
        return output;
    }

    public void register(Command command, Method method, Object methodOrigin) {
        register(command.name(), command, method, methodOrigin);
        for (String alias : command.aliases()) {
            register(alias, command, method, methodOrigin);
        }
    }
}
