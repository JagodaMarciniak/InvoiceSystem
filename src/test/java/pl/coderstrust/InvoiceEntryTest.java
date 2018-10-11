package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.UnitType.PIECE;
import static pl.coderstrust.Vat.VAT_23;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

class InvoiceEntryTest {

  @Test
  public void testInvoiceEntryConstructor() {
    //given
    InvoiceEntry invoiceEntry = new InvoiceEntry("Kurs Java", 2, PIECE, new BigDecimal(4500),
        VAT_23);

    //when
    Object[] actualFields = new Object[] {
        invoiceEntry.getItem(), invoiceEntry.getQuantity(), invoiceEntry.getUnit(),
        invoiceEntry.getPrice(), invoiceEntry.getVat(), invoiceEntry.getNetValue(),
        invoiceEntry.getGrossValue()
    };

    //then
    Object[] expectedFields = new Object[] {"Kurs Java", 2, PIECE,
        new BigDecimal(4500), VAT_23, new BigDecimal(9000),
        new BigDecimal(11070).setScale(2, RoundingMode.HALF_UP)};
    assertArrayEquals(expectedFields, actualFields);
  }

  @Test
  public void testInvoiceEntryConstructorWithNulls() {
    assertThrows(NullPointerException.class, () ->
        new InvoiceEntry(null, 2, PIECE, new BigDecimal(4500), VAT_23));
  }
}
