package com.epam.kramanenka.trigram.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {

  @SneakyThrows
  public static Path getExistingFilePath(String filename) {
    URL resource = FileUtils.class.getClassLoader().getResource(filename);
    if (resource != null) {
      return Path.of(resource.toURI());
    }
    return Path.of(URI.create(filename));
  }

  @SneakyThrows
  public static Path createOutputFile(String filename) {
    Path newFilePath = Paths.get(filename);
    Files.createFile(newFilePath);
    log.info("Output file is created: {}", newFilePath.toAbsolutePath());
    return newFilePath;
  }
}
