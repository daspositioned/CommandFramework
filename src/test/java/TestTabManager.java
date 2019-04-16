import net.seliba.commandframework.CommandArgs;
import net.seliba.commandframework.api.Completer;
import net.seliba.commandframework.internal.tab.TabManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TestTabManager {

    @Test
    public void test1() {
        TabManager manager = new TabManager();
        try {
            Method method = this.getClass().getMethod("MethodTest1", CommandArgs.class);
            manager.register(method.getAnnotation(Completer.class), this, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name", null)));
    }


    @Test
    public void test2() {
        TabManager manager = new TabManager();
        try {
            Method method = this.getClass().getMethod("MethodTest2", CommandArgs.class);
            manager.register(method.getAnnotation(Completer.class),this, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("n2", String.join(" ", manager.get("name", null)));
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name.n2", null)));
    }
    @Test
    public void test3() {
        TabManager manager = new TabManager();
        try {
            Method method = this.getClass().getMethod("MethodTest3", CommandArgs.class);
            manager.register(method.getAnnotation(Completer.class), this, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals("n2", String.join(" ", manager.get("name", null)));
        Assertions.assertEquals("n3", String.join(" ", manager.get("name.n2", null)));
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name.n2.n3", null)));
    }

    @Test
    public void test4() {
        TabManager manager = new TabManager();
        try {
            Method method = this.getClass().getMethod("MethodTest4", CommandArgs.class);
            manager.register(method.getAnnotation(Completer.class),this, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals("moin m2 t1 t2", String.join(" ", manager.get("name", null)));
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name.t1", null)));
        Assertions.assertEquals("t4 moin m2", String.join(" ", manager.get("name.t2", null)));
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name.t2.t4", null)));
    }
    @Test
    public void test5() {
        TabManager manager = new TabManager();
        try {
            Method method = this.getClass().getMethod("MethodTest5", CommandArgs.class);
            manager.register(method.getAnnotation(Completer.class),this, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals("moin m2 t1 t2", String.join(" ", manager.get("name", null)));
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name.t1", null)));
        Assertions.assertEquals("t4 moin m2", String.join(" ", manager.get("name.t2", null)));
        Assertions.assertEquals("moin m2", String.join(" ", manager.get("name.t2.t4", null)));

        Assertions.assertEquals("name.t2.t4", manager.getNextLowestCompleter("name.t2.t4").get(0));
        Assertions.assertEquals("name.t2.t4", manager.getNextLowestCompleter("name.t2.t4.eins").get(0));
        Assertions.assertEquals("name.t2.t4", manager.getNextLowestCompleter("name.t2.t4.eins.zwei").get(0));
        Assertions.assertEquals("name.t2.t4", manager.getNextLowestCompleter("name.t2.t4.eins.zwei.drei").get(0));
        Assertions.assertEquals("name.t2.t4", manager.getNextLowestCompleter("name.t2.t4.eins.zwei.drei.vier").get(0));
        Assertions.assertEquals("name.t2.t4", manager.getNextLowestCompleter("name.t2.t4.eins.zwei.drei.vier.fuenf").get(0));
    }

    @Completer(name = "name")
    public List<String> MethodTest1(CommandArgs args) {
        return Arrays.asList("moin", "m2");
    }
    @Completer(name = "name.n2")
    public List<String> MethodTest2(CommandArgs args) {
        return Arrays.asList("moin", "m2");
    }
    @Completer(name = "name.n2.n3")
    public List<String> MethodTest3(CommandArgs args) {
        return Arrays.asList("moin", "m2");
    }

    @Completer(name = "name", aliases = {"name.t1", "name.t2", "name.t2.t4"})
    public List<String> MethodTest4(CommandArgs args) {
        return Arrays.asList("moin", "m2");
    }

    @Completer(name = "name", aliases = {"name.t1", "name.t2", "name.t2.t4"})
    public List<String> MethodTest5(CommandArgs args) {
        return Arrays.asList("moin", "m2");
    }
}
