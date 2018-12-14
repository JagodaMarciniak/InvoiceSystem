package pl.coderstrust.controller;

import lombok.Data;
import lombok.NonNull;

@Data
public class ResponseMessage {
  @NonNull
  private final String message;
}
