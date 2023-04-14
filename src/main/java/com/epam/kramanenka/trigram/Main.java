package com.epam.kramanenka.trigram;

import com.epam.kramanenka.trigram.etl.DataExtractor;
import com.epam.kramanenka.trigram.etl.DataTransformerLoader;
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

  @SneakyThrows
  public static void main(String[] args) {
    log.info("Program execution started");
    var startTime = Instant.now();

    var dataExtractor = DataExtractor.builder().build();
    var dataTransformerLoader = DataTransformerLoader.builder().build();
    if (args.length > 0) {
      dataExtractor.setInputFilename(args[0]);
    }
    var data = dataExtractor.extractDataFromInputFile();
    dataTransformerLoader.transformDataAndLoadToCsv(data);

    var duration = DurationFormatUtils.formatDuration(Duration.between(startTime, Instant.now()).toMillis(), "HH:mm:ss", true);
    log.info("Finished in {}", duration);
  }
}
