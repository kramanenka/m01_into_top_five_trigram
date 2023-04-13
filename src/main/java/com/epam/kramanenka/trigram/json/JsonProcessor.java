package com.epam.kramanenka.trigram.json;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonAttribute;
import com.epam.kramanenka.trigram.model.AuthorMessage;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JsonProcessor {

  private DslJson<Object> dslJson = new DslJson<>();

  public List<AuthorMessage> extractAuthorMessages(String json) {
    if (StringUtils.isBlank(json)) {
      return List.of();
    }
    try {
      Event event = dslJson.deserialize(Event.class, new ByteArrayInputStream(json.getBytes()));

      return event.payload.commits.stream()
          .filter(commit -> commit.author != null)
          .map(commit -> AuthorMessage.builder()
              .author(commit.author.name)
              .message(formatMessage(commit.message))
              .build())
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return List.of();
  }

  private String formatMessage(String message) {
    return message
        .replaceAll("[^\\w\\s#/]", "")
        .replaceAll("[\\r\\n]", " ")
        .toLowerCase();
  }

  @CompiledJson
  @ToString
  public static class Event {
    @JsonAttribute(mandatory = true)
    public Payload payload;
  }

  @ToString
  @CompiledJson
  public static class Payload {
    @JsonAttribute(mandatory = true)
    public List<Commit> commits;
  }

  @ToString
  @CompiledJson
  public static class Commit {
    public Author author;
    @JsonAttribute
    public String message;
  }

  @ToString
  @CompiledJson
  public static class Author {
    @JsonAttribute
    public String name;
  }
}
