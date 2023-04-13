package com.epam.kramanenka.trigram.json;

import com.epam.kramanenka.trigram.model.AuthorMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonProcessorTest {

  private JsonProcessor jsonProcessor = new JsonProcessor();

  @Test
  void shouldReturnEmptyListWhenInputJsonIsNull() {
    assertThat(jsonProcessor.extractAuthorMessages(null)).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenInputJsonIsBlank() {
    assertThat(jsonProcessor.extractAuthorMessages("  ")).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenInputJsonIsNotValid() {
    assertThat(jsonProcessor.extractAuthorMessages("some invalid json")).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenInputJsonWithoutPayload() {
    String json = "{\"id\":\"11185349996\",\"type\":\"PushEvent\"}";

    assertThat(jsonProcessor.extractAuthorMessages(json)).isEmpty();
  }


  @Test
  void shouldReturnEmptyListWhenInputJsonWithoutCommits() {
    String json = "{\"id\":\"11185349996\",\"type\":\"PushEvent\",\"payload\":{\"push_id\":4450652997}}";

    assertThat(jsonProcessor.extractAuthorMessages(json)).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenInputJsonWithoutCommitAuthor() {
    String json = "{\"id\":\"11185349996\",\"type\":\"PushEvent\",\"payload\":{\"push_id\":4450652997," +
        "\"commits\":[{\"message\":\"message 1\",\"url\":\"some url\"}]}}";

    assertThat(jsonProcessor.extractAuthorMessages(json)).isEmpty();
  }

  @Test
  void shouldSkipCommitsWithoutAuthor() {
    String json = "{\"id\":\"11185349996\",\"type\":\"PushEvent\"," +
        "\"payload\":{\"push_id\":4450652997,\"commits\":[{\"message\":\"message 1\",\"url\":\"some url\"}" +
        ",{\"author\":{\"name\":\"some author\",\"email\":\"some@email.com\"},\"message\":\"message 2\"}]}}";

    assertThat(jsonProcessor.extractAuthorMessages(json)).containsExactly(AuthorMessage.builder()
        .author("some author")
        .message("message 2")
        .build());
  }

  @Test
  void shouldFormatMessageToLowercaseAndRemovePunctuation() {
    String json = "{\"id\":\"11185349996\",\"type\":\"PushEvent\",\"payload\":{\"push_id\":4450652997,\"commits\":[{\"author\":{\"name\":\"Author 1\"},\"message\":\"Message 1.2.3 #push \\nother\"},{\"author\":{\"name\":\"Author 2\"},\"message\":\"Message 4-5-6 url/other/ # push \\nSome\"}]}}";

    assertThat(jsonProcessor.extractAuthorMessages(json)).containsExactly(
        AuthorMessage.builder()
            .author("Author 1")
            .message("message 123 #push  other")
            .build(),
        AuthorMessage.builder()
            .author("Author 2")
            .message("message 456 url/other/ # push  some")
            .build());
  }
}
