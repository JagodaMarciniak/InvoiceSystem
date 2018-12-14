package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.model.UnitType.DAY;
import static pl.coderstrust.model.UnitType.FLAT_RATE;
import static pl.coderstrust.model.UnitType.HOUR;
import static pl.coderstrust.model.UnitType.PIECE;

import org.junit.jupiter.api.Test;


class UnitTypeTest {

  @Test
  void shouldReturnExpectedStringWhenGetValueOnPieceInvoked() {
    assertEquals("piece", PIECE.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnHourInvoked() {
    assertEquals("hour", HOUR.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnDayInvoked() {
    assertEquals("day", DAY.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnFlatRateInvoked() {
    assertEquals("flat rate", FLAT_RATE.getValue());
  }
}
