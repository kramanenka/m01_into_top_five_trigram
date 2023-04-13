package com.epam.kramanenka.trigram;

import com.epam.kramanenka.trigram.json.JsonProcessor;
import com.epam.kramanenka.trigram.model.AuthorMessage;
import com.epam.kramanenka.trigram.util.FileUtils;
import com.epam.kramanenka.trigram.util.TrigramUtils;
import com.opencsv.CSVWriter;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Builder
@Slf4j
public class TrigramFinder {

  private String inputFilename;
  private String outputFilename;
  private String messageFilter;
  private int trigramCount;

  public void processInputFileAndWriteOutputCsv() throws IOException {
    Path inputFilePath = FileUtils.getExistingFilePath(inputFilename);
    Path outputFilePath = FileUtils.createOutputFile(outputFilename.replaceAll("###", String.valueOf(System.currentTimeMillis())));

    try (InputStream inputStream = new BZip2CompressorInputStream(new BufferedInputStream(Files.newInputStream(inputFilePath)));
         CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath.toString()),
             ' ', '\'', '\'', "\n")) {
      log.info("Reading input file {}", inputFilePath.toAbsolutePath());
      Map<String, List<String>> authorMessages = readAuthorMessages(inputStream);
      log.info("Analyzing data and writing output file {}", outputFilePath);
      findAndWriteTopAuthorMessageTrigrams(writer, authorMessages);
    }
  }

  private Map<String, List<String>> readAuthorMessages(InputStream inputStream) {
    Stream<String> jsonLines = new BufferedReader(new InputStreamReader(inputStream)).lines();
    JsonProcessor jsonProcessor = new JsonProcessor();

    Map<String, List<String>> authorMessages = jsonLines
        .filter(line -> line.contains(messageFilter))
        .flatMap(line -> jsonProcessor.extractAuthorMessages(line).stream())
        .collect(Collectors.groupingBy(AuthorMessage::getAuthor, mapping(AuthorMessage::getMessage, toList())));
    return authorMessages;
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
