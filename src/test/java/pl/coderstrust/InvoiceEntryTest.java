package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.UnitType.PIECE;
import static pl.coderstrust.Vat.VAT_8;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InvoiceEntryTest {

  @Test
  void checkFullInitialization() {
    //given
    String itemDescription = "Kurs Java";
    Long quantity = 1L;
    UnitType unit = PIECE;
    BigDecimal price = new BigDecimal(100);
    Vat vatRate = VAT_8;
    BigDecimal netValue = new BigDecimal(100);
    BigDecimal grossValue = new BigDecimal(108);

    //when
    InvoiceEntry invoiceEntry = new InvoiceEntry(itemDescription, quantity, unit, price, vatRate,
        netValue, grossValue);

    //then
    assertEquals("Kurs Java", invoiceEntry.getItemDescription());
    assertEquals(Long.valueOf(1), invoiceEntry.getQuantity());
    assertEquals(PIECE, invoiceEntry.getUnit());
    assertEquals(new BigDecimal(100), invoiceEntry.getPrice());
    assertEquals(VAT_8, invoiceEntry.getVatRate());
    assertEquals(new BigDecimal(100), invoiceEntry.getNetValue());
    assertEquals(new BigDecimal(108), invoiceEntry.getGrossValue());
  }

  @Test
  public void shouldThrowExceptionWhenItemDescriptionIsNull() {
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
