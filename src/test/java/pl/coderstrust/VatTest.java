package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.Vat.VAT_0;
import static pl.coderstrust.Vat.VAT_23;
import static pl.coderstrust.Vat.VAT_5;
import static pl.coderstrust.Vat.VAT_8;

import org.junit.jupiter.api.Test;

class VatTest {

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatZeroInvoked() {
    assertEquals(0.00f, VAT_0.getValue());
  }

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatFiveInvoked() {
    assertEquals(0.05f, VAT_5.getValue());
  }

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatEightInvoked() {
    assertEquals(0.08f, VAT_8.getValue());
  }

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatTwentyThreeInvoked() {
    assertEquals(0.23f, VAT_23.getValue());
  }
}
