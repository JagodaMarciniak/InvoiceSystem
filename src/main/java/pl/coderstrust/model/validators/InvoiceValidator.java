package pl.coderstrust.model.validators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceValidator {

  public static List<String> validateInvoice(Invoice invoice, boolean isIdExpected) {
    if (invoice == null) {
      return Collections.singletonList("Invoice cannot be null");
    }

    List<String> result = new ArrayList<>();
    if (isIdExpected) {
      String resultOfIdValidation = validateExpectedId(invoice.getId());
      ValidationResultAdder.addResultOfValidation(result, resultOfIdValidation);
    } else {

    }
    List<String> resultOfBuyerValidation = validateBuyer(invoice.getBuyer());
    ValidationResultAdder.addResultOfValidation(result, resultOfBuyerValidation);
    List<String> resultOfSellerValidation = validateSeller(invoice.getSeller());
    ValidationResultAdder.addResultOfValidation(result, resultOfSellerValidation);
    List<String> resultOfEntriesValidation = validateEntries(invoice.getEntries());
    ValidationResultAdder.addResultOfValidation(result, resultOfEntriesValidation);
    String resultOfDatesValidation = validateDates(invoice.getIssueDate(), invoice.getDueDate());
    ValidationResultAdder.addResultOfValidation(result, resultOfDatesValidation);
    String resultOfTotalNetValueValidation = validateTotalNetValue(invoice.getTotalNetValue());
    ValidationResultAdder.addResultOfValidation(result, resultOfTotalNetValueValidation);
    String resultOfTotalGrossValueValidation = validateTotalGrossValue(invoice.getTotalGrossValue());
    ValidationResultAdder.addResultOfValidation(result, resultOfTotalGrossValueValidation);
    String resultOfCommentsValidation = validateComments(invoice.getComments());
    ValidationResultAdder.addResultOfValidation(result, resultOfCommentsValidation);
    return result;
  }

  private static String validateExpectedId(@NonNull int id) {
    if (id <= 0) {
      return "Id cannot be equal or lower than 0";
    }
    return null;
  }

  private static String validateNotExpectedId(@NonNull int id) {
    if (id <= 0) {
      return "Id cannot be equal or lower than 0";
    }
    return null;
  }

  private static String validateDates(LocalDate issueDate, LocalDate dueDate) {
    if (issueDate == null || dueDate == null) {
      return "The Date cannot be null";
    }
    if (issueDate.isAfter(dueDate)) {
      return "The due date cannot be before issue date";
    }
    return null;
  }

  private static List<String> validateSeller(Company seller) {
    return CompanyValidator.validateCompany(seller);
  }

  private static List<String> validateBuyer(Company buyer) {
    return CompanyValidator.validateCompany(buyer);
  }

  private static List<String> validateEntries(List<InvoiceEntry> entries) {
    return InvoiceEntriesValidator.validateEntries(entries);
  }

  private static String validateTotalNetValue(BigDecimal totalNetValue) {
    if (totalNetValue == null) {
      return "Net Value cannot be null";
    }
    return null;
  }

  private static String validateTotalGrossValue(BigDecimal totalGrossValue) {
    if (totalGrossValue == null) {
      return "Gross value cannot be null";
    }
    return null;
  }

  private static String validateComments(String comments) {
    if (comments == null) {
      return "Comments cannot be null";
    }
    return null;
  }
}
