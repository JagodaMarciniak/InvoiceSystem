package pl.coderstrust.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ResponseMessage {
  @NonNull
  private final String message;

  @NonNull
  private final List<String> details;

  public ResponseMessage(String message) {
    this.message = message;
    details = new ArrayList<>();
  }
}
