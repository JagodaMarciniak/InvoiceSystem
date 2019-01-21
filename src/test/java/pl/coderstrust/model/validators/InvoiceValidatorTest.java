package pl.coderstrust.model.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InvoiceValidatorTest {

  private static final Invoice validInvoice = InvoiceGenerator.getRandomInvoice();

  private static Stream<Arguments> argumentsForInvoiceIdValidationWhenIdExpected() {
    Invoice invoiceWithNullId = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullId.setId(null);
    Invoice invoiceWithEmptyId = InvoiceGenerator.getRandomInvoice();
    invoiceWithEmptyId.setId("");
    Invoice validInvoice = InvoiceGenerator.getRandomInvoice();

    return Stream.of(
        Arguments.of(invoiceWithNullId, Collections.singletonList("Id cannot be null")),
        Arguments.of(invoiceWithEmptyId, Collections.singletonList("Id cannot be empty")),
        Arguments.of(validInvoice, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForInvoiceIdValidationWhenIdNotExpected() {
    Invoice invoiceWithNullId = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullId.setId("1");
    Invoice invoiceWithEmptyId = InvoiceGenerator.getRandomInvoice();
    invoiceWithEmptyId.setId("a");
    Invoice validInvoice = InvoiceGenerator.getRandomInvoice();
    validInvoice.setId(null);

    return Stream.of(
        Arguments.of(invoiceWithNullId, Collections.singletonList("Id must be null")),
        Arguments.of(invoiceWithEmptyId, Collections.singletonList("Id must be null")),
        Arguments.of(validInvoice, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForDatesValidationWhenIdExpected() {
    Invoice invoiceWithNullIssueDate = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullIssueDate.setIssueDate(null);
    Invoice invoiceWithNullDueDate = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullDueDate.setDueDate(null);
    Invoice invoiceWithIssueDateAfterDueDate = InvoiceGenerator.getRandomInvoice();
    invoiceWithIssueDateAfterDueDate.setIssueDate(LocalDate.of(1, 1, 2));
    invoiceWithIssueDateAfterDueDate.setDueDate(LocalDate.of(1, 1, 1));
    Invoice validInvoice = InvoiceGenerator.getRandomInvoice();

    return Stream.of(
        Arguments.of(invoiceWithNullIssueDate, Collections.singletonList("Issue date cannot be null")),
        Arguments.of(invoiceWithNullDueDate, Collections.singletonList("Due date cannot be null")),
        Arguments.of(invoiceWithIssueDateAfterDueDate, Collections.singletonList("Due date cannot be before issue date")),
        Arguments.of(validInvoice, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForInvoiceTotalNetValueValidationWhenIdExpected() {
    Invoice invoiceWithNullTotalNetValue = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullTotalNetValue.setTotalNetValue(null);
    Invoice invoiceWithNegativeTotalNetValue = InvoiceGenerator.getRandomInvoice();
    invoiceWithNegativeTotalNetValue.setTotalNetValue(new BigDecimal(-5));
    Invoice validInvoice = InvoiceGenerator.getRandomInvoice();

    return Stream.of(
        Arguments.of(invoiceWithNullTotalNetValue, Collections.singletonList("Total net value cannot be null")),
        Arguments.of(invoiceWithNegativeTotalNetValue, Collections.singletonList("Total net value cannot be lower than zero")),
        Arguments.of(validInvoice, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForInvoiceTotalGrossValueValidationWhenIdExpected() {
    Invoice invoiceWithNullTotalGrossValue = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullTotalGrossValue.setTotalGrossValue(null);
    Invoice invoiceWithNegativeTotalGrossValue = InvoiceGenerator.getRandomInvoice();
    invoiceWithNegativeTotalGrossValue.setTotalGrossValue(new BigDecimal(-5));
    Invoice validInvoice = InvoiceGenerator.getRandomInvoice();

    return Stream.of(
        Arguments.of(invoiceWithNullTotalGrossValue, Collections.singletonList("Total gross value cannot be null")),
        Arguments.of(invoiceWithNegativeTotalGrossValue, Collections.singletonList("Total gross value cannot be lower than zero")),
        Arguments.of(validInvoice, Collections.emptyList())
    );
  }

  private static Stream<Arguments> argumentsForBuyerSellerEntriesValidationWhenIdExpected() {
    Invoice invoiceWithNullSeller = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullSeller.setSeller(null);
    Invoice invoiceWithNullBuyer = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullBuyer.setBuyer(null);
    Invoice invoiceWithNullEntries = InvoiceGenerator.getRandomInvoice();
    invoiceWithNullEntries.setEntries(null);
    Invoice validInvoice = InvoiceGenerator.getRandomInvoice();

    return Stream.of(
        Arguments.of(invoiceWithNullSeller, Collections.singletonList("Company cannot be null")),
        Arguments.of(invoiceWithNullBuyer, Collections.singletonList("Company cannot be null")),
        Arguments.of(invoiceWithNullEntries, Collections.singletonList("Invoice entries cannot be null")),
        Arguments.of(validInvoice, Collections.emptyList())
    );
  }

  @Test
  void shouldValidateIfInvoiceIsNull() {
    //given
    List<String> expectedResult = Collections.singletonList("Invoice cannot be null");

    //then
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(null, true));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForInvoiceIdValidationWhenIdExpected")
  void validateIdWhenExpected(Invoice invoice, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(invoice, true));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForInvoiceIdValidationWhenIdNotExpected")
  void validateIdWhenNotExpected(Invoice invoice, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(invoice, false));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForDatesValidationWhenIdExpected")
  void validateDatesWhenIdExpected(Invoice invoice, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(invoice, true));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForInvoiceTotalNetValueValidationWhenIdExpected")
  void validateInvoiceTotalNetValueWhenIdExpected(Invoice invoice, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(invoice, true));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForInvoiceTotalGrossValueValidationWhenIdExpected")
  void validateTotalGrossValueWhenIdExpected(Invoice invoice, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(invoice, true));
  }

  @ParameterizedTest
  @MethodSource(value = "argumentsForBuyerSellerEntriesValidationWhenIdExpected")
  void validateBuyerSellerAndEntriesWhenIdExpected(Invoice invoice, List<String> expectedResult) {
    assertEquals(expectedResult, InvoiceValidator.validateInvoice(invoice, true));
  }


}