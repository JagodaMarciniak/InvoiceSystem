package pl.coderstrust.model.validators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceValidator extends Validator {

  public static List<String> validateInvoice(Invoice invoice, boolean isIdExpected) {
    if (invoice == null) {
      return Collections.singletonList("Invoice cannot be null");
    }

    List<String> result = new ArrayList<>();
    String resultOfIdValidation;
    if (isIdExpected) {
      resultOfIdValidation = validateExpectedId(invoice.getId());
    } else {
      resultOfIdValidation = validateNotExpectedId(invoice.getId());
    }

    addResultOfValidation(result, resultOfIdValidation);
    List<String> resultOfBuyerValidation = validateBuyer(invoice.getBuyer());
    addResultOfValidation(result, resultOfBuyerValidation);
    List<String> resultOfSellerValidation = validateSeller(invoice.getSeller());
    addResultOfValidation(result, resultOfSellerValidation);
    List<String> resultOfEntriesValidation = validateEntries(invoice.getEntries());
    addResultOfValidation(result, resultOfEntriesValidation);
    String resultOfDatesValidation = validateDates(invoice.getIssueDate(), invoice.getDueDate());
    addResultOfValidation(result, resultOfDatesValidation);
    String resultOfTotalNetValueValidation = validateTotalNetValue(invoice.getTotalNetValue());
    addResultOfValidation(result, resultOfTotalNetValueValidation);
    String resultOfTotalGrossValueValidation = validateTotalGrossValue(invoice.getTotalGrossValue());
    addResultOfValidation(result, resultOfTotalGrossValueValidation);
    return result;
  }

  private static String validateExpectedId(String id) {
    if (id == null) {
      return "Id cannot be null";
    }
    if (id.trim().isEmpty()) {
      return "Id cannot be empty";
    }
    return null;
  }

  private static String validateNotExpectedId(String id) {
    if (id != null) {
      return "Id must be null";
    }
    if (id != null && !id.trim().isEmpty()) {
      return "Id must be empty or null";
    }
    return null;
  }

  private static String validateDates(LocalDate issueDate, LocalDate dueDate) {
    if (issueDate == null) {
      return "Issue date cannot be null";
    }
    if (dueDate == null) {
      return "Due date cannot be null";
    }
    if (issueDate.isAfter(dueDate)) {
      return "Due date cannot be before issue date";
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
      return "Total net value cannot be null";
    }
    if (totalNetValue.doubleValue() < 0) {
      return "Total net value cannot be lower than zero";
    }
    return null;
  }

  private static String validateTotalGrossValue(BigDecimal totalGrossValue) {
    if (totalGrossValue == null) {
      return "Total gross value cannot be null";
    }
    if (totalGrossValue.doubleValue() < 0) {
      return "Total gross value cannot be lower than zero";
    }
    return null;
  }
}
