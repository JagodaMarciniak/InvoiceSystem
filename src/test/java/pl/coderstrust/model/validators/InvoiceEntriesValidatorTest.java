package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.generators.InvoiceEntriesGenerator;
import pl.coderstrust.model.InvoiceEntry;

class InvoiceEntriesValidatorTest {

  private static Stream<Arguments> argumentsForItemsInEntriesValidationTest() {
    List<InvoiceEntry> invoiceEntriesWithItemNull = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithItemNull.get(0).setItem(null);
    List<InvoiceEntry> invoiceEntriesWithItemEmpty = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithItemEmpty.get(0).setItem("");
    List<InvoiceEntry> validInvoiceEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return Stream.of(
        Arguments.of(invoiceEntriesWithItemNull, Collections.singletonList("Item cannot be null")),
        Arguments.of(invoiceEntriesWithItemEmpty, Collections.singletonList("Item cannot be empty")),
        Arguments.of(validInvoiceEntries, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForQuantitiesInEntriesValidationTest() {
    List<InvoiceEntry> invoiceEntriesWithQuantityNull = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithQuantityNull.get(0).setQuantity(null);
    List<InvoiceEntry> invoiceEntriesWithQuantity0 = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithQuantity0.get(0).setQuantity(0L);
    List<InvoiceEntry> validInvoiceEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return Stream.of(
        Arguments.of(invoiceEntriesWithQuantityNull, Collections.singletonList("Quantity cannot be null")),
        Arguments.of(invoiceEntriesWithQuantity0, Collections.singletonList("Quantity cannot be lower or equal to zero")),
        Arguments.of(validInvoiceEntries, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForPricesInEntriesValidationTest() {
    List<InvoiceEntry> invoiceEntriesWithPriceNull = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithPriceNull.get(0).setPrice(null);
    List<InvoiceEntry> invoiceEntriesWithPriceNegative = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithPriceNegative.get(0).setPrice(new BigDecimal(-3));
    List<InvoiceEntry> validInvoiceEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return Stream.of(
        Arguments.of(invoiceEntriesWithPriceNull, Collections.singletonList("Price cannot be null")),
        Arguments.of(invoiceEntriesWithPriceNegative, Collections.singletonList("Price cannot be lower than zero")),
        Arguments.of(validInvoiceEntries, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForNetValuesInEntriesValidationTest() {
    List<InvoiceEntry> invoiceEntriesWithNetValueNull = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithNetValueNull.get(0).setNetValue(null);
    List<InvoiceEntry> invoiceEntriesWithNetValueNegative = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithNetValueNegative.get(0).setNetValue(new BigDecimal(-3));
    List<InvoiceEntry> validInvoiceEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return Stream.of(
        Arguments.of(invoiceEntriesWithNetValueNull, Collections.singletonList("Net value cannot be null")),
        Arguments.of(invoiceEntriesWithNetValueNegative, Collections.singletonList("Net value cannot be lower than zero")),
        Arguments.of(validInvoiceEntries, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForGrossValuesInEntriesValidationTest() {
    List<InvoiceEntry> invoiceEntriesWithGrossValueNull = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithGrossValueNull.get(0).setGrossValue(null);
    List<InvoiceEntry> invoiceEntriesWithGrossValueNegative = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithGrossValueNegative.get(0).setGrossValue(new BigDecimal(-3));
    List<InvoiceEntry> validInvoiceEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return Stream.of(
        Arguments.of(invoiceEntriesWithGrossValueNull, Collections.singletonList("Gross value cannot be null")),
        Arguments.of(invoiceEntriesWithGrossValueNegative, Collections.singletonList("Gross value cannot be lower than zero")),
        Arguments.of(validInvoiceEntries, Collections.emptyList())
    );
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForItemsInEntriesValidationTest")
  void validateItemsInEntries(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForQuantitiesInEntriesValidationTest")
  void validateQuantities(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForPricesInEntriesValidationTest")
  void validatePrices(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForNetValuesInEntriesValidationTest")
  void validateNetValues(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForGrossValuesInEntriesValidationTest")
  void validateGrossValues(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

  @Test
  void testToValidateIfInvoiceEntriesIsNull() {
    //given
    List<String> expectedResult = Collections.singletonList("Invoice entries cannot be null");

    //then
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(null));
  }
}
