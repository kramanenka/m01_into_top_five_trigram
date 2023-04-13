package com.epam.kramanenka.trigram.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrigramUtils {

  private static final int COUNT = 3;

  public static List<String> findTopFrequentTrigrams(List<String> messages, int qty) {
    Map<String, Long> allTrigrams = messages.stream()
        .flatMap(message -> findAllTrigrams(message).entrySet().stream())
        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));

    return allTrigrams.entrySet().stream()
        .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
        .limit(qty)
        .map(entry -> entry.getKey())
        .collect(Collectors.toList());
  }

  public static Map<String, Long> findAllTrigrams(String message) {
    if (StringUtils.isBlank(message)) {
      return Map.of();
    }
    String[] words = message.replaceAll("\\s+", " ").trim().split(" ");
    Map<String, Long> trigrams = IntStream.rangeClosed(0, words.length - COUNT)
        .mapToObj(i -> StringUtils.join(words, " ", i, i + COUNT))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    return trigrams;
  }

}
