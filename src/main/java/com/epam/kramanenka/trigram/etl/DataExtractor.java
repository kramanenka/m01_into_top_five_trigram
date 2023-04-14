package com.epam.kramanenka.trigram.etl;

import com.epam.kramanenka.trigram.json.JsonProcessor;
import com.epam.kramanenka.trigram.model.AuthorMessage;
import com.epam.kramanenka.trigram.util.FileUtils;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
@Builder
public class DataExtractor {
  private static final String TYPE_PUSH_EVENT = "\"type\":\"PushEvent\"";
  private static final String DEFAULT_INPUT_FILENAME = "10K.github.jsonl.bz2";

  @Builder.Default
  @Setter
  private String inputFilename = DEFAULT_INPUT_FILENAME;
  @Builder.Default
  private String messageFilter = TYPE_PUSH_EVENT;

  public Map<String, List<String>> extractDataFromInputFile() throws IOException {
    Path inputFilePath = FileUtils.getExistingFilePath(inputFilename);

    try (InputStream inputStream = new BZip2CompressorInputStream(new BufferedInputStream(Files.newInputStream(inputFilePath)))) {
      log.info("Reading input file {}", inputFilePath.toAbsolutePath());
      return readAuthorMessages(inputStream);
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
}
