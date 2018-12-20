package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UnitType {

  PIECE("piece"),
  HOUR("hour"),
  DAY("day"),
  FLAT_RATE("flat rate");

  @Getter
  private final String value;
}
