package com.epam.kramanenka.trigram.utils;

import com.epam.kramanenka.trigram.util.TrigramUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TrigramUtilsTest {

  @Test
  void shouldReturnEmptyResultWhenInputIsNullOnFind() {
    assertThat(TrigramUtils.findAllTrigrams(null))
        .isEmpty();
  }

  @Test
  void shouldReturnEmptyResultWhenInputIsEmptyOnFind() {
    assertThat(TrigramUtils.findAllTrigrams(""))
        .isEmpty();
  }

  @Test
  void shouldReturnEmptyResultWhenInputContainsLessThan3WordsOnFind() {
    assertThat(TrigramUtils.findAllTrigrams("one two"))
        .isEmpty();
  }

  @Test
  void shouldReturnOneTrigramWhenInputContainsExactly3WordsOnFind() {
    assertThat(TrigramUtils.findAllTrigrams("one two three"))
        .containsExactlyEntriesOf(Map.of("one two three", 1L));
  }

  @Test
  void shouldReturnCorrectTrigramsOnFind() {
    assertThat(TrigramUtils.findAllTrigrams("  one two  three   four one  two  three four  "))
        .containsExactlyInAnyOrderEntriesOf(Map.of(
            "one two three", 2L,
            "two three four", 2L,
            "three four one", 1L,
            "four one two", 1L
        ));
  }

  @Test
  void shouldReturnCorrectTrigramsOnFindAll() {
    List<String> input = List.of(
        "  one two  three   four one  two  three four  ",
        "  one two  three   four one  two  three four  five",
        "  one two  three",
        "  three four one",
        "  one two  three   four");

    assertThat(TrigramUtils.findTopFrequentTrigrams(input, 3))
        .containsExactly("one two three", "two three four", "three four one");
  }
}