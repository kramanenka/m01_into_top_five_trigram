package com.epam.kramanenka.trigram.io;

import com.epam.kramanenka.trigram.util.FileUtils;
import com.epam.kramanenka.trigram.util.TrigramUtils;
import com.opencsv.CSVWriter;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Builder
public class OutputDataProcessor {

  private static final int DEFAULT_TRIGRAM_COUNT = 5;
  private static final String PLACEHOLDER = "###";
  private static final String DEFAULT_OUTPUT_FILENAME = "output/" + PLACEHOLDER + "-top-five-trigrams.csv";

  @Builder.Default
  private String outputFilename = DEFAULT_OUTPUT_FILENAME;
  @Builder.Default
  private int trigramCount = DEFAULT_TRIGRAM_COUNT;

  public void analyzeDataAndWriteOutputCsv(Map<String, List<String>> authorMessages) throws IOException {
    Path outputFilePath = FileUtils.createOutputFile(outputFilename.replaceAll(PLACEHOLDER, String.valueOf(System.currentTimeMillis())));

    try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath.toString()),
        ' ', '\'', '\'', "\n")) {
      log.info("Analyzing data and writing output file {}", outputFilePath);
      findAndWriteTopAuthorMessageTrigrams(writer, authorMessages);
    }
  }

  private void findAndWriteTopAuthorMessageTrigrams(CSVWriter writer, Map<String, List<String>> authorMessages) {
    authorMessages.forEach((author, messages) -> {
      List<String> topFrequentTrigrams = TrigramUtils.findTopFrequentTrigrams(messages, trigramCount);
      if (!topFrequentTrigrams.isEmpty()) {
        List<String> csvLineData = new ArrayList<>();
        csvLineData.add(author);
        csvLineData.addAll(topFrequentTrigrams);
        writer.writeNext(csvLineData.toArray(new String[]{}));
      }
    });
  }
}
