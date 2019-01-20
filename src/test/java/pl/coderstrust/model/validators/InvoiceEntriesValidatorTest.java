package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.generators.InvoiceEntriesGenerator;
import pl.coderstrust.model.InvoiceEntry;

class InvoiceEntriesValidatorTest {

  @ParameterizedTest
  @MethodSource(value = "argumentsForItemsInEntriesValidationTest")
  void validateItemsInEntries(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

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

  @ParameterizedTest
  @MethodSource(value = "argumentsForQuantitiesInEntriesValidationTest")
  void validateEntries(List<InvoiceEntry> invoiceEntries, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceEntriesValidator.validateEntries(invoiceEntries));
  }

  private static Stream<Arguments> argumentsForQuantitiesInEntriesValidationTest() {
    List<InvoiceEntry> invoiceEntriesWithQuantityNull = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithQuantityNull.get(0).setQuantity(null);
    List<InvoiceEntry> invoiceEntriesWithQuantity0 = InvoiceEntriesGenerator.getSampleInvoiceEntries();
    invoiceEntriesWithQuantity0.get(0).setQuantity(0L);
    List<InvoiceEntry> validInvoiceEntries = InvoiceEntriesGenerator.getSampleInvoiceEntries();

    return Stream.of(
        Arguments.of(invoiceEntriesWithQuantityNull, Collections.singletonList("Quantity cannot be null")),
        Arguments.of(invoiceEntriesWithQuantity0, Collections.singletonList("Quantity cannot be zero")),
        Arguments.of(validInvoiceEntries, Collections.emptyList())
    );
  }
}