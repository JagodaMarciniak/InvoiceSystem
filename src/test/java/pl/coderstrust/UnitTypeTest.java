package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.UnitType.DAY;
import static pl.coderstrust.UnitType.FLAT_RATE;
import static pl.coderstrust.UnitType.HOUR;
import static pl.coderstrust.UnitType.PIECE;

import org.junit.jupiter.api.Test;

class UnitTypeTest {
  @Test
  void testPiece() {
    assertEquals("szt.", PIECE.getText());
  }

  @Test
  void testHour() {
    assertEquals("godz.", HOUR.getText());
  }

  @Test
  void testDay() {
    assertEquals("dzień", DAY.getText());
  }

  @Test
  void testFlatRate() {
    assertEquals("ryczałt", FLAT_RATE.getText());
  }
}
