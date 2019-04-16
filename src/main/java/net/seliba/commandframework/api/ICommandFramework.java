package net.seliba.commandframework.api;

import net.seliba.commandframework.internal.command.AbstractCommandManager;
import net.seliba.commandframework.internal.tab.TabManager;

import java.lang.reflect.Method;

public interface ICommandFramework {

  default void registerCommands(Object command) {
    int registedCMDs = 0;
    int registedCompleters = 0;
    for (Method method : command.getClass().getMethods()) {
      if (method.isAnnotationPresent(Command.class)) {
        Class[] params = method.getParameterTypes();
        if (params.length != 1) {
          throw new IllegalStateException(
              "error at Command: " + command.getClass().getName() + "/" + method.getName() + ""
                  + " Invalid parameter count.");
        } else {
          if (!(ICommandArgs.class.isAssignableFrom(params[0]))) {
            throw new IllegalStateException(
                "error at Command: " + command.getClass().getName() + "/" + method.getName()
                    + " Invalid parameter type.");
          }
          registedCMDs++;
          registerCommand(method.getDeclaredAnnotation(Command.class), method, command);
          getTabManager().registerSubFormCommand(method.getDeclaredAnnotation(Command.class));
        }
      } else if (method.isAnnotationPresent(Completer.class)) {
        getTabManager().register(method.getDeclaredAnnotation(Completer.class), command, method);
        registedCompleters++;
      }
    }
    System.out.println("[CommandFramework] registed " + registedCMDs + " Command"+((registedCMDs != 1) ? "s" :"") +" and " +registedCompleters+ " Completer"+((registedCompleters != 1) ? "s" :"") +" in " + command.getClass().getName());
  }

  default void registerCompleters(Object completer) {
    int registedCompletes = 0;
    for (Method method : completer.getClass().getMethods()) {
      if (method.isAnnotationPresent(Completer.class)) {
        registedCompletes++;
        getTabManager().register(method.getDeclaredAnnotation(Completer.class), completer, method);
      }
    }
    System.out.println("[CommandFramework] registed " + registedCompletes + " Completer"+((registedCompletes != 1) ? "s" :"") +" in " + completer.getClass().getName());
  }

  default void unregisterCommands(Object command) {
    Method[] methods = command.getClass().getMethods();
    for (Method method : methods) {
      if (method.isAnnotationPresent(Command.class)) {
        Class[] params = method.getParameterTypes();
        if (params.length != 1) {
          throw new IllegalStateException(
              "error at Command: " + command.getClass().getName() + "/" + method.getName() + ""
                  + " Invalid parameter count.");
        } else {
          if (!(ICommandArgs.class.isAssignableFrom(params[0]))) {
            throw new IllegalStateException(
                "error at Command: " + command.getClass().getName() + "/" + method.getName()
                    + ""
                    + " Invalid parameter type.");
          }
          unregisterCommand(method.getDeclaredAnnotation(Command.class));
        }
      }
    }
  }

  void unregisterCommand(Command command);

  String[] getCommandLabels();

  void registerCommand(Command command, Method method, Object commandObject);

  TabManager getTabManager();
  AbstractCommandManager getCommandManager();

  void setInGameOnlyMessage(String msg);
  String getInGameOnlyMessage();

  void setNoPermissionMessage(String msg);
  String getNoPermissionMessage();

  void setDoesNotExist(String msg);
  String getDoesNotExist();
}