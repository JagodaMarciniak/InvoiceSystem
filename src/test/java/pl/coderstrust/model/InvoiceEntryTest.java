package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.model.UnitType.PIECE;
import static pl.coderstrust.model.Vat.VAT_8;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InvoiceEntryTest {

  @Test
  void checkFullInitialization() {
    //given
    String item = "Kurs Java";
    Long quantity = 1L;
    UnitType unit = PIECE;
    BigDecimal price = new BigDecimal(100);
    Vat vatRate = VAT_8;
    BigDecimal netValue = new BigDecimal(100);
    BigDecimal grossValue = new BigDecimal(108);

    //when
    InvoiceEntry invoiceEntry = new InvoiceEntry(item, quantity, unit, price, vatRate,
        netValue, grossValue);

    //then
    assertEquals(item, invoiceEntry.getItem());
    assertEquals(quantity, invoiceEntry.getQuantity());
    assertEquals(unit, invoiceEntry.getUnit());
    assertEquals(price, invoiceEntry.getPrice());
    assertEquals(vatRate, invoiceEntry.getVatRate());
    assertEquals(netValue, invoiceEntry.getNetValue());
    assertEquals(grossValue, invoiceEntry.getGrossValue());
  }

  @Test
  public void shouldThrowExceptionWhenItemIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry(null, 1L, PIECE,
        new BigDecimal(100), VAT_8, new BigDecimal(100), new BigDecimal(108)));
  }

  @Test
  public void shouldThrowExceptionWhenQuantityIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry("Kurs Java", null, PIECE,
        new BigDecimal(100), VAT_8, new BigDecimal(100), new BigDecimal(108)));
  }

  @Test
  public void shouldThrowExceptionWhenUnitIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry("Kurs Java", 1L, null,
        new BigDecimal(100), VAT_8, new BigDecimal(100), new BigDecimal(108)));
  }

  @Test
  public void shouldThrowExceptionWhenPriceIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry("Kurs Java", 1L, PIECE,
        null, VAT_8, new BigDecimal(100), new BigDecimal(108)));
  }

  @Test
  public void shouldThrowExceptionWhenVatIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry("Kurs Java", 1L, PIECE,
        new BigDecimal(100), null, new BigDecimal(100), new BigDecimal(108)));
  }

  @Test
  public void shouldThrowExceptionWhenNetValueIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry("Kurs Java", 1L, PIECE,
        new BigDecimal(100), VAT_8, null, new BigDecimal(108)));
  }

  @Test
  public void shouldThrowExceptionWhenGrossValueIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new InvoiceEntry("Kurs Java", 1L, PIECE,
        new BigDecimal(100), VAT_8, new BigDecimal(100), null));
  }
}
