package net.seliba.commandframework.internal.tab;

import net.seliba.commandframework.CommandArgs;
import net.seliba.commandframework.api.Command;
import net.seliba.commandframework.api.Completer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TabManager {
    private HashMap<String, HashSet<AbstractMap.SimpleEntry<Object, Method>>> registerdEntrys;
    private HashMap<String, HashSet<String>> staticEntrys;

    public TabManager() {
        registerdEntrys = new HashMap<>();
        staticEntrys = new HashMap<>();
    }


    public void register(Completer completer, Object methodOrigin, Method method) {
        register(completer.name(),new AbstractMap.SimpleEntry<>(methodOrigin, method));
        for (String command : completer.aliases()) {
            register(command, new AbstractMap.SimpleEntry<>(methodOrigin, method));
        }
    }

    private void register(String command, AbstractMap.SimpleEntry<Object, Method> entry) {
        if (!List.class.isAssignableFrom(entry.getValue().getReturnType())) {
            throw new IllegalArgumentException("Method needs to Return a List");
        }
        if (!entry.getValue().isAnnotationPresent(Completer.class)) {
            throw new IllegalArgumentException("The Method needs to have a Completer Annotation");
        }
        if (entry.getValue().getParameters().length != 1) {
            throw new IllegalArgumentException("The Method needs to have one CommandArgs parameter.");
        }

        if (registerdEntrys.get(command.toLowerCase()) == null) {
            registerdEntrys.put(command.toLowerCase(), new HashSet<>());
        }
        registerdEntrys.get(command.toLowerCase()).add(entry);
        registerSubCommand(command.toLowerCase());
    }

    private void registerSubCommand(String command) {
        String[] s = command.split("\\.");
        for (int i = 0; i < s.length-1; i++) {
            HashSet<String> set =new HashSet<>();
            set.add(s[i+1]);
            String commandString = buildCommandString(i,s);
            addStaticEntry(commandString, set);
        }
    }

    public void registerSubFormCommand(Command command) {
        registerSubCommand(command.name().toLowerCase());
        for (String cmd : command.aliases()) {
            registerSubCommand(cmd.toLowerCase());
        }
    }

    private String buildCommandString(int index, String[]cmd) {
        String s = "";
        for (int i = 0; i < index+1; i++) {
            if (s.equalsIgnoreCase("")) {
                s = cmd[i];
            }else{
                s = s + "." + cmd[i];
            }
        }
        return s;
    }

    private void addStaticEntry(String command, HashSet<String> toAdd) {
        if (!staticEntrys.containsKey(command)) {
            staticEntrys.put(command, toAdd);
        }else {
            for (String s : toAdd) {
                if (!staticEntrys.get(command).contains(s)) {
                    HashSet<String> l = new HashSet<>(staticEntrys.get(command));
                    l.addAll(toAdd);
                    staticEntrys.put(command, l);
                }
            }
            staticEntrys.get(command).addAll(toAdd);
        }
    }

    public List<String> get(String command, CommandArgs args) {
        command = command.toLowerCase();
        if (registerdEntrys.containsKey(command)) {
            try {
                HashSet<String> output = new HashSet<>();
                for (AbstractMap.SimpleEntry<Object, Method> e : registerdEntrys.get(command)) {
                    Object obj = e.getValue().invoke(e.getKey(), args);
                    if (obj != null) {
                        output.addAll(new HashSet<>((List<String>) obj));
                    }
                }
                if (staticEntrys.containsKey(command)) {
                    output.addAll(staticEntrys.get(command));
                }
                return new ArrayList<>(output);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else if (staticEntrys.containsKey(command)) {
            return new ArrayList<>(staticEntrys.get(command));
        }
        return null;
    }

    public List<String> getNextLowestCompleter(String command) {
        HashSet<String> list = new HashSet<>(staticEntrys.keySet());
        list.addAll(registerdEntrys.keySet());
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
                }else {
                    if (s.split("\\.").length == highest) {
                        output.add(s);
                    }
                }
            }
        }
        return output;
    }
}