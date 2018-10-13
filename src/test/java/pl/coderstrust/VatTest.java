package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.Vat.VAT_0;
import static pl.coderstrust.Vat.VAT_23;
import static pl.coderstrust.Vat.VAT_5;
import static pl.coderstrust.Vat.VAT_8;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class VatTest {

  @Test
  void testVat0() {
    assertEquals(BigDecimal.valueOf(0.00), VAT_0.getRate());
  }

  @Test
  void testVat5() {
    assertEquals(BigDecimal.valueOf(0.05), VAT_5.getRate());
  }

  @Test
  void testVat8() {
    assertEquals(BigDecimal.valueOf(0.08), VAT_8.getRate());
  }

  @Test
  void testVat23() {
    assertEquals(BigDecimal.valueOf(0.23), VAT_23.getRate());
  }
}
