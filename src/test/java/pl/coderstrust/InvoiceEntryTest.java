package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.UnitType.PIECE;
import static pl.coderstrust.Vat.VAT_23;
import static pl.coderstrust.Vat.VAT_8;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InvoiceEntryTest {

  private static InvoiceEntry invoiceEntry;

  private static Stream<Arguments> constructorParameters() {
    return Stream.of(
        Arguments.of(null, 1, PIECE, new BigDecimal(100), VAT_8, new BigDecimal(100),
            new BigDecimal(123)), Arguments.of("Kurs Java", null, PIECE, new BigDecimal(100),
            VAT_8, new BigDecimal(100), new BigDecimal(123)),
        Arguments.of("Kurs Java", 1, null, new BigDecimal(100), VAT_8, new BigDecimal(100),
            new BigDecimal(123)),
        Arguments.of("Kurs Java", 1, PIECE, null, VAT_8, new BigDecimal(100), new BigDecimal(123)),
        Arguments.of("Kurs Java", 1, PIECE, new BigDecimal(100), null, new BigDecimal(100),
            new BigDecimal(123)),
        Arguments.of("Kurs Java", 1, PIECE, new BigDecimal(100), VAT_8, null, new BigDecimal(123)),
        Arguments.of("Kurs Java", 1, PIECE, new BigDecimal(100), VAT_8, new BigDecimal(100), null));
  }

  @BeforeEach
  void runBeforeEach() {
    invoiceEntry = new InvoiceEntry("Kurs Java", 2, PIECE, new BigDecimal(4500), VAT_23,
        new BigDecimal(9000), new BigDecimal(11070));
  }

  @Test
  void shouldReturnInvoiceEntryObjectWithExpectedFields() {
    //when
    Object[] actualFields = new Object[] {invoiceEntry.getItem(), invoiceEntry.getQuantity(),
        invoiceEntry.getUnit(), invoiceEntry.getPrice(), invoiceEntry.getVat(),
        invoiceEntry.getNetValue(), invoiceEntry.getGrossValue()};

    //then
    Object[] expectedFields = new Object[] {"Kurs Java", 2, PIECE, new BigDecimal(4500), VAT_23,
        new BigDecimal(9000), new BigDecimal(11070)};
    assertArrayEquals(expectedFields, actualFields);
  }

  @ParameterizedTest
  @MethodSource("constructorParameters")
  void shouldThrowExceptionWhenWhenParameterIsNull(
      String item, Integer quantity, UnitType unit, BigDecimal price, Vat vat,
      BigDecimal netValue, BigDecimal grossValue) {
    assertThrows(NullPointerException.class, () -> new InvoiceEntry(item, quantity, unit, price,
        vat, netValue, grossValue));
  }

  @Test
  void shouldReturnExpectedContentWhenToStringIsInvoked() {
    //when
    String actualContent = invoiceEntry.toString();

    //then
    String expectedContent =
        "InvoiceEntry{item='Kurs Java', quantity='2, unit=PIECE, price=4500, vat=VAT_23,"
            + " netValue=9000, grossValue=11070}";
    assertEquals(expectedContent, actualContent);
  }
}
