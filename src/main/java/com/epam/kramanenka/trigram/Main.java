package com.epam.kramanenka.trigram;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;

/**
 * <p>This program produces a CSV file with top five word 3-grams in the commit messages for each author name
 * in event type “PushEvent” within the file 10K.github.jsonl
 * </p>
 */
@Slf4j
public class Main {

  public static final String TYPE_PUSH_EVENT = "\"type\":\"PushEvent\"";
  public static final int DEFAULT_TRIGRAM_COUNT = 5;
  private static final String DEFAULT_OUTPUT_FILENAME = "output/###-top-five-trigrams.csv";
  private static final String DEFAULT_INPUT_FILENAME = "10K.github.jsonl.bz2";

  @SneakyThrows
  public static void main(String[] args) {
    log.info("Program execution started");
    var startTime = Instant.now();
    String inputFilename = args.length > 0 ? args[0] : DEFAULT_INPUT_FILENAME;

    TrigramFinder.builder()
        .inputFilename(inputFilename)
        .outputFilename(DEFAULT_OUTPUT_FILENAME)
        .messageFilter(TYPE_PUSH_EVENT)
        .trigramCount(DEFAULT_TRIGRAM_COUNT)
        .build()
        .processInputFileAndWriteOutputCsv();

    var duration = DurationFormatUtils.formatDuration(Duration.between(startTime, Instant.now()).toMillis(), "HH:mm:ss", true);
    log.info("Finished in {}", duration);
  }
}
