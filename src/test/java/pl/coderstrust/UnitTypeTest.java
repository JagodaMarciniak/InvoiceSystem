package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.UnitType.DAY;
import static pl.coderstrust.UnitType.FLAT_RATE;
import static pl.coderstrust.UnitType.HOUR;
import static pl.coderstrust.UnitType.PIECE;

import org.junit.jupiter.api.Test;

class UnitTypeTest {

  @Test
  void shouldReturnExpectedStringWhenGetValueOnPieceInvoked() {
    assertEquals("szt.", PIECE.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnHourInvoked() {
    assertEquals("godz.", HOUR.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnDayInvoked() {
    assertEquals("dzień", DAY.getValue());
  }

  @Test
  void shouldReturnExpectedStringWhenGetValueOnFlatRateInvoked() {
    assertEquals("ryczałt", FLAT_RATE.getValue());
  }
}
