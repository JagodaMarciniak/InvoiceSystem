package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.UnitType.DAY;
import static pl.coderstrust.UnitType.FLAT_RATE;
import static pl.coderstrust.UnitType.HOUR;
import static pl.coderstrust.UnitType.PIECE;

import org.junit.jupiter.api.Test;

class UnitTypeTest {

  @Test
  void shouldReturnExpectedStringWhenGetTextOnPieceInvoked() {
    assertEquals("szt.", PIECE.getText());
  }

  @Test
  void shouldReturnExpectedStringWhenGetTextOnHourInvoked() {
    assertEquals("godz.", HOUR.getText());
  }

  @Test
  void shouldReturnExpectedStringWhenGetTextOnDayInvoked() {
    assertEquals("dzień", DAY.getText());
  }

  @Test
  void shouldReturnExpectedStringWhenGetTextOnFlatRateInvoked() {
    assertEquals("ryczałt", FLAT_RATE.getText());
  }
}
