package com.epam.kramanenka.trigram.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorMessage {
  private String author;
  private String message;
}
