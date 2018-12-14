package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class VatTest {

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatZeroInvoked() {
    assertEquals(0.00f, Vat.VAT_0.getValue());
  }

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatFiveInvoked() {
    assertEquals(0.05f, Vat.VAT_5.getValue());
  }

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatEightInvoked() {
    assertEquals(0.08f, Vat.VAT_8.getValue());
  }

  @Test
  void shouldReturnExpectedRateWhenGetValueOnVatTwentyThreeInvoked() {
    assertEquals(0.23f, Vat.VAT_23.getValue());
  }
}
