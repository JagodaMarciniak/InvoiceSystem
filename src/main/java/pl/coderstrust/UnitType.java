package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public enum UnitType {

  PIECE("szt."),
  HOUR("godz."),
  DAY("dzień"),
  FLAT_RATE("ryczałt");

  @NonNull
  @Getter
  private final String text;
}
