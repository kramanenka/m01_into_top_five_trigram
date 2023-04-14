package com.epam.kramanenka.trigram.etl;

import com.epam.kramanenka.trigram.util.DataTrigramUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DataTrigramUtilsTest {

  @Test
  void shouldReturnEmptyResultWhenInputIsNullOnFind() {
    assertThat(DataTrigramUtils.findAllTrigrams(null))
        .isEmpty();
  }

  @Test
  void shouldReturnEmptyResultWhenInputIsEmptyOnFind() {
    assertThat(DataTrigramUtils.findAllTrigrams(""))
        .isEmpty();
  }

  @Test
  void shouldReturnEmptyResultWhenInputContainsLessThan3WordsOnFind() {
    assertThat(DataTrigramUtils.findAllTrigrams("one two"))
        .isEmpty();
  }

  @Test
  void shouldReturnOneTrigramWhenInputContainsExactly3WordsOnFind() {
    assertThat(DataTrigramUtils.findAllTrigrams("one two three"))
        .containsExactlyEntriesOf(Map.of("one two three", 1L));
  }

  @Test
  void shouldReturnCorrectTrigramsOnFind() {
    assertThat(DataTrigramUtils.findAllTrigrams("  one two  three   four one  two  three four  "))
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

    assertThat(DataTrigramUtils.findTopFrequentTrigrams(input, 3))
        .containsExactly("one two three", "two three four", "three four one");
  }
}