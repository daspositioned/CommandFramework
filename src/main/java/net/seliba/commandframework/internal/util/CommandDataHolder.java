package net.seliba.commandframework.internal.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.seliba.commandframework.api.Command;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class CommandDataHolder {
    Command command;
    Method method;
    Object origin;
}
