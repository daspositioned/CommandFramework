import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.AbstractMap.SimpleEntry;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.seliba.commandframework.CommandArgs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestToTags {

  @Test
  public void testSimple1Argument() {
    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "first",
        new String[0]);

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(1, pairs.length);

    Assertions.assertEquals("first", pairs[0].getValue());
    Assertions.assertEquals("", pairs[0].getKey());
  }

  @Test
  public void testSimpleOneTag() {
    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "-tag some long text to test",
        new String[]{"-tag"});

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(1, pairs.length);

    Assertions.assertEquals("some long text to test", pairs[0].getValue());
    Assertions.assertEquals("-tag", pairs[0].getKey());
  }

  @Test
  public void testOneWordAndOneTag() {
    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "first -tag some long text to test",
        new String[]{"-tag"});

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(2, pairs.length);

    Assertions.assertEquals("first", pairs[0].getValue());
    Assertions.assertEquals("", pairs[0].getKey());
    Assertions.assertEquals("some long text to test", pairs[1].getValue());
    Assertions.assertEquals("-tag", pairs[1].getKey());
  }


  @Test
  public void testMutipleWordsAndTags() {
    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "first second -tag some long text -tag2 some other text",
        new String[]{"-tag", "-tag2"});

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(4, pairs.length);

    Assertions.assertEquals("first", pairs[0].getValue());
    Assertions.assertEquals("", pairs[0].getKey());

    Assertions.assertEquals("second", pairs[1].getValue());
    Assertions.assertEquals("", pairs[1].getKey());

    Assertions.assertEquals("some long text", pairs[2].getValue());
    Assertions.assertEquals("-tag", pairs[2].getKey());

    Assertions.assertEquals("some other text", pairs[3].getValue());
    Assertions.assertEquals("-tag2", pairs[3].getKey());

    Assertions.assertEquals("some long text", args.getArgument("-tag"));

  }


  @Test
  public void testMutipleWordsAndTags2() {
    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "first second -tag some long text",
        new String[]{"-tag", "-tag2"});

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(3, pairs.length);

    Assertions.assertEquals("first", pairs[0].getValue());
    Assertions.assertEquals("", pairs[0].getKey());

    Assertions.assertEquals("second", pairs[1].getValue());
    Assertions.assertEquals("", pairs[1].getKey());

    Assertions.assertEquals("some long text", pairs[2].getValue());
    Assertions.assertEquals("-tag", pairs[2].getKey());

    SimpleEntry<String, String> r1 = args.getArgumentPair("-tag2");
    String r2 = args.getArgument("-tag2");

    Assertions.assertNull(r1);
    Assertions.assertNull(r2);

  }

  @Test
  public void testEmptyTag() {
    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "argument -emptyTag",
        new String[]{"-emptyTag"});

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(2, pairs.length);

    Assertions.assertEquals("argument", pairs[0].getValue());
    Assertions.assertEquals("", pairs[0].getKey());

    //Assertions.assertEquals("", pairs[1].getValue());
    Assertions.assertEquals("-emptyTag", pairs[1].getKey());
  }

  @Test
  public void testEmptyTag2() {
    //Command:
    // -> "/test fist second -tag some long text -tag2 some other text"

    //Creating CommandArgs
    CommandArgs args = createCommandArgs(
        "argument -emptyTag -r not Empty",
        new String[]{"-emptyTag", "-r"});

    //Creating List of Entrys
    SimpleEntry<String, String>[] pairs = args.getArgumentPairs();

    //Run all Tests
    Assertions.assertEquals(3, pairs.length);

    Assertions.assertEquals("argument", pairs[0].getValue());
    Assertions.assertEquals("", pairs[0].getKey());

    Assertions.assertEquals("", pairs[2].getValue());
    Assertions.assertEquals("-emptyTag", pairs[2].getKey());

    Assertions.assertEquals("not Empty", pairs[1].getValue());
    Assertions.assertEquals("-r", pairs[1].getKey());
  }


  private CommandArgs createCommandArgs(String args, String[] tags) {
    ProxiedPlayer player = mock(ProxiedPlayer.class);
    when(player.getName()).thenReturn("Baumhaus");
    when(player.getUniqueId()).thenReturn(UUID.randomUUID());

    return new CommandArgs(
        player,
        args.split(" "),
        tags,
        "test",
        "test".split("\\.").length - 1);
  }

}
