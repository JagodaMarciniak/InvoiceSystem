package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.coderstrust.UnitType.HOUR;
import static pl.coderstrust.UnitType.PIECE;
import static pl.coderstrust.Vat.VAT_23;
import static pl.coderstrust.Vat.VAT_8;

import com.google.common.testing.EqualsTester;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Arguments.of(null, 1, PIECE, new BigDecimal(100), VAT_8),
        Arguments.of("Kurs Java", null, PIECE, new BigDecimal(100), VAT_8),
        Arguments.of("Kurs Java", 1, null, new BigDecimal(100), VAT_8),
        Arguments.of("Kurs Java", 1, PIECE, null, VAT_8),
        Arguments.of("Kurs Java", 1, PIECE, new BigDecimal(100), null));
  }

  @BeforeEach
  void runBeforeEach() {
    invoiceEntry = new InvoiceEntry("Kurs Java", 2, PIECE, new BigDecimal(4500),
        VAT_23);
  }

  @Test
  void testInvoiceEntryConstructor() {
    //when
    Object[] actualFields = new Object[] {
        invoiceEntry.getItem(), invoiceEntry.getQuantity(), invoiceEntry.getUnit(),
        invoiceEntry.getPrice(), invoiceEntry.getVat(), invoiceEntry.getNetValue(),
        invoiceEntry.getGrossValue()
    };

    //then
    Object[] expectedFields = new Object[] {"Kurs Java", 2, PIECE,
        new BigDecimal(4500), VAT_23, new BigDecimal(9000).setScale(2, RoundingMode.HALF_UP),
        new BigDecimal(11070).setScale(2, RoundingMode.HALF_UP)};
    assertArrayEquals(expectedFields, actualFields);
  }

  @ParameterizedTest
  @MethodSource("constructorParameters")
  void testConstructorWithNulls(String item, Integer quantity, UnitType unit, BigDecimal price,
                                Vat vat) {
    assertThrows(NullPointerException.class, () ->
        new InvoiceEntry(item, quantity, unit, price, vat));
  }

  @Test
  void setItem() {
    // when
    invoiceEntry.setItem("Mleko sojowe");

    // then
    assertEquals("Mleko sojowe", invoiceEntry.getItem());
  }

  @Test
  void setItemWithNull() {
    assertThrows(NullPointerException.class, () -> invoiceEntry.setItem(null));
  }

  @Test
  void testGetItem() {
    assertEquals("Kurs Java", invoiceEntry.getItem());
  }

  @Test
  void setQuantity() {
    // when
    invoiceEntry.setQuantity(10);

    // then
    assertEquals(Integer.valueOf(10), invoiceEntry.getQuantity());
  }

  @Test
  void setQuantityWithNull() {
    assertThrows(NullPointerException.class, () -> invoiceEntry.setQuantity(null));
  }

  @Test
  void testGetQuantity() {
    assertEquals(Integer.valueOf(2), invoiceEntry.getQuantity());
  }

  @Test
  void setUnit() {
    // when
    invoiceEntry.setUnit(HOUR);

    // then
    assertEquals(HOUR, invoiceEntry.getUnit());
  }

  @Test
  void setUnitWithNull() {
    assertThrows(NullPointerException.class, () -> invoiceEntry.setUnit(null));
  }

  @Test
  void testGetUnit() {
    assertEquals(PIECE, invoiceEntry.getUnit());
  }

  @Test
  void setPrice() {
    // when
    invoiceEntry.setPrice(new BigDecimal(300));

    // then
    assertEquals(new BigDecimal(300), invoiceEntry.getPrice());
  }

  @Test
  void setPriceWithNull() {
    assertThrows(NullPointerException.class, () -> invoiceEntry.setPrice(null));
  }

  @Test
  void testGetPrice() {
    assertEquals(new BigDecimal(4500), invoiceEntry.getPrice());
  }

  @Test
  void setVat() {
    // when
    invoiceEntry.setVat(VAT_8);

    // then
    assertEquals(VAT_8, invoiceEntry.getVat());
  }

  @Test
  void setVatWithNull() {
    assertThrows(NullPointerException.class, () -> invoiceEntry.setVat(null));
  }

  @Test
  void testGetVat() {
    assertEquals(VAT_23, invoiceEntry.getVat());
  }

  @Test
  void testUpdateNetValue() {
    //given
    invoiceEntry.setQuantity(3);
    invoiceEntry.setPrice(new BigDecimal(150));

    // when
    invoiceEntry.updateNetValue();

    //then
    assertEquals(new BigDecimal(450).setScale(2, RoundingMode.HALF_UP), invoiceEntry.getNetValue());
  }

  @Test
  void testGetNetValue() {
    assertEquals(new BigDecimal(9000).setScale(2, RoundingMode.HALF_UP),
        invoiceEntry.getNetValue());
  }

  @Test
  void updateGrossValue() {
    invoiceEntry.setQuantity(3);
    invoiceEntry.setPrice(new BigDecimal(150));

    // when
    invoiceEntry.updateGrossValue();

    //then
    assertEquals(new BigDecimal(553.5).setScale(2, RoundingMode.HALF_UP),
        invoiceEntry.getGrossValue());
  }

  @Test
  void testGetGrossValue() {
    assertEquals(new BigDecimal(11070).setScale(2, RoundingMode.HALF_UP),
        invoiceEntry.getGrossValue());
  }

  @Test
  void testToString() {
    //when
    String actualContents = invoiceEntry.toString();

    //then
    String expectedContents = "InvoiceEntry{item='Kurs Java', quantity='2, unit=PIECE, price=4500,"
        + " vat=VAT_23, netValue=9000.00, grossValue=11070.00}";
    assertEquals(expectedContents, actualContents);
  }

  @Test
  public void testEquals() {
    //given
    EqualsTester equalsTester = new EqualsTester();
    InvoiceEntry entryOne = new InvoiceEntry("Kurs Java", 2, PIECE, new BigDecimal(4500),
        VAT_23);
    InvoiceEntry entryTwo = new InvoiceEntry("Kurs Java", 2, PIECE, new BigDecimal(4500),
        VAT_23);
    equalsTester.addEqualityGroup(entryOne, entryTwo);

    //when
    equalsTester.testEquals();
  }
}
