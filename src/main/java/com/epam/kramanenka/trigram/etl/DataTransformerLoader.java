package com.epam.kramanenka.trigram.etl;

import com.epam.kramanenka.trigram.util.DataTrigramUtils;
import com.epam.kramanenka.trigram.util.FileUtils;
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
public class DataTransformerLoader {

  private static final int DEFAULT_TRIGRAM_COUNT = 5;
  private static final String PLACEHOLDER = "###";
  private static final String DEFAULT_OUTPUT_FILENAME = "output/" + PLACEHOLDER + "-top-five-trigrams.csv";

  @Builder.Default
  private String outputFilename = DEFAULT_OUTPUT_FILENAME;
  @Builder.Default
  private int trigramCount = DEFAULT_TRIGRAM_COUNT;

  public void transformDataAndLoadToCsv(Map<String, List<String>> authorMessages) throws IOException {
    Path outputFilePath = FileUtils.createOutputFile(outputFilename.replaceAll(PLACEHOLDER, String.valueOf(System.currentTimeMillis())));

    try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath.toString()),
        ' ', '\'', '\'', "\n")) {
      log.info("Transforming data and writing output file {}", outputFilePath);
      authorMessages.forEach((author, messages) -> {
        String[] csvTrigramsData = transformAuthorMesssagesToTrigramsCsv(author, messages);
        if (csvTrigramsData != null) {
          writer.writeNext(csvTrigramsData);
        }
      });
    }
  }

  private String[] transformAuthorMesssagesToTrigramsCsv(String author, List<String> messages) {
    List<String> topFrequentTrigrams = DataTrigramUtils.findTopFrequentTrigrams(messages, trigramCount);
    if (!topFrequentTrigrams.isEmpty()) {
      List<String> csvTrigramsData = new ArrayList<>();
      csvTrigramsData.add(author);
      csvTrigramsData.addAll(topFrequentTrigrams);
      return csvTrigramsData.toArray(new String[]{});
    }
    return null;
  }
}
