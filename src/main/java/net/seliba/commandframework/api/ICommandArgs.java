package net.seliba.commandframework.api;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface ICommandArgs {

  <T> T getSender(Class<T> type);

  String getCommandLabel();

  String[] getRawArguments();

  String[] getTags();


  /**
   * use {@link #getRawArguments()} for the original imput NOT regarding tags
   * or use {@link #getArgumentPairs()} or {@link #getArgumentStrings()} for getting all Arguments with Tags.
   */
  @Deprecated
  default String[] getArguments() {
    return getRawArguments();
  }

  default int getArgumentCount() {
    return getArguments().length;
  }


  default String getJoinedString(int i) {
    final StringBuilder buffer = new StringBuilder(getArgument(i));
    for (int ii = i + 1; ii < getArgumentCount(); ++ii) {
      buffer.append(" ").append(getArgument(ii));
    }
    return buffer.toString();
  }

  default AbstractMap.SimpleEntry<String, String>[] getArgumentPairs() {
    String[] args = this.getRawArguments();
    List<AbstractMap.SimpleEntry<String, String>> newArgs = new ArrayList<>();

    String tag = "";
    List<String> params = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      String argument = args[i];
      if (isTag(argument)) {
        if (!(params.size() == 0)) {
          newArgs.add(new AbstractMap.SimpleEntry<>(tag, String.join(" ", params)));
          params = new ArrayList<>();
        }
        tag = argument;
      } else {
        if (tag.equalsIgnoreCase("")) {
          newArgs.add(new AbstractMap.SimpleEntry<>("", argument));
        } else {
          params.add(argument);
        }
      }
    }

    if (!(params.size() == 0)) {
      newArgs.add(new AbstractMap.SimpleEntry<>(tag, String.join(" ", params)));
    }

    //To check for Empty tags, of there are they always will be at the end. see "testEmptyTags2"
    Arrays.asList(getRawArguments()).forEach(s -> {
      if (Arrays.asList(getTags()).contains(s)) {
        if (!newArgs.stream().map(entry -> entry.getKey()).collect(Collectors.toList())
            .contains(s)) {
          newArgs.add(new SimpleEntry<>(s, ""));
        }
      }
    });

    return newArgs.toArray(new AbstractMap.SimpleEntry[newArgs.size()]);
  }

  default boolean isTag(String arg) {
    return Arrays.asList(getTags()).contains(arg);
  }


  default AbstractMap.SimpleEntry<String, String> getArgumentPair(int index) {
    return this.getArgumentPairs()[index];
  }

  default AbstractMap.SimpleEntry<String, String> getArgumentPair(String tag) {
    for (AbstractMap.SimpleEntry<String, String> entry : this.getArgumentPairs()) {
      if (entry.getKey().equalsIgnoreCase(tag)) {
        return entry;
      }
    }
    return null;
  }

  default String getArgument(String tag) {
    return (this.getArgumentPair(tag) == null) ? null : getArgumentPair(tag).getValue();
  }

  default String getArgumentString(int i) {
    return this.getArgumentPairs()[i].getValue();
  }

  default String[] getArgumentStrings() {
    return (Arrays.asList(getArgumentPairs())
        .stream().map(entry -> entry.getValue()).collect(Collectors.toList()))
        .toArray(new String[0]);
  }

  default String[] getUsedTags() {
    List<String> output = new ArrayList<>();
    Arrays.asList(this.getArgumentPairs()).stream().map(entry -> entry.getKey()).collect(Collectors.toList())
        .forEach(s -> {
          if (!s.equalsIgnoreCase(""))
            output.add(s);
        });
    return output.toArray(new String[0]);
  }

  default boolean containsTag(String tag) {
    for (String s : this.getUsedTags()) {
      if (s.equalsIgnoreCase(tag)) {
        return true;
      }
    }
    return false;
  }

  default int getArgumentInt(int i) throws NumberFormatException {
    return Integer.parseInt(this.getRawArguments()[i]);
  }

  default double getArgumentDouble(final int i) throws NumberFormatException {
    return Double.parseDouble(this.getRawArguments()[i]);
  }

  default String getRawArgument(int i) {
    return this.getRawArguments()[i];
  }

  /**
   * @deprecated use {@link #getArgumentPair(int)} or {@link #getRawArgument(int)}
   */
  @Deprecated
  default String getArgument(int i) {
    return getRawArguments()[i];
  }

  default int length() {
    return getRawArguments().length;
  }
}
