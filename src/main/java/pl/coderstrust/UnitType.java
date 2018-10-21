package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public enum UnitType {

  PIECE("piece"),
  HOUR("hour"),
  DAY("day"),
  FLAT_RATE("flat rate");

  @NonNull
  @Getter
  private final String value;
}
