package pl.coderstrust.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ErrorMessage {
  @NonNull
  private final String message;

  @NonNull
  private final List<String> details;

  public ErrorMessage(String message) {
    this.message = message;
    details = new ArrayList<>();
  }
}
