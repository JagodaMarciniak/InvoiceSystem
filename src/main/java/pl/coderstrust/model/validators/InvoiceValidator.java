package pl.coderstrust.model.validators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceValidator {

  public static List<String> validateInvoice(Invoice invoice) {
    List<String> result = new ArrayList<>();
    ValidationResultAdder.addResultOfValidation(result, validateId(invoice.getId()));
    ValidationResultAdder.addResultOfValidation(result, validateBuyer(invoice.getBuyer()));
    ValidationResultAdder.addResultOfValidation(result, validateSeller(invoice.getSeller()));
    ValidationResultAdder.addResultOfValidation(result, validateEntries(invoice.getEntries()));
    ValidationResultAdder.addResultOfValidation(result, validateDates(invoice.getIssueDate(), invoice.getDueDate()));
    ValidationResultAdder.addResultOfValidation(result, validateTotalNetValue(invoice.getTotalNetValue()));
    ValidationResultAdder.addResultOfValidation(result, validateTotalGrossValue(invoice.getTotalGrossValue()));
    ValidationResultAdder.addResultOfValidation(result, validateComments(invoice.getComments()));
    return result;
  }

  private static String validateId(@NonNull int id) {
    if (id <= 0) {
      return "Id cannot be equal or lower than 0";
    }
    return "";
  }

  private static String validateDates(LocalDate issueDate, LocalDate dueDate) {
    if (issueDate == null || dueDate == null) {
      return "The Date cannot be null";
    }
    if (issueDate.isAfter(dueDate)) {
      return "The due date cannot be before issue date";
    }
    return "";
  }

  private static List<String> validateSeller(Company seller) {
    return CompanyValidator.validateCompany(seller);
  }

  private static List<String> validateBuyer(Company buyer) {
    return CompanyValidator.validateCompany(buyer);
  }

  private static List<String> validateEntries(List<InvoiceEntry> entries) {
    return InvoiceEntryValidator.validateEntries(entries);
  }

  private static String validateTotalNetValue(BigDecimal totalNetValue) {
    if (totalNetValue == null) {
      return "Net Value cannot be null";
    }
    return "";
  }

  private static String validateTotalGrossValue(BigDecimal totalGrossValue) {
    if (totalGrossValue == null) {
      return "Gross value cannot be null";
    }
    return "";
  }

  private static String validateComments(String comments) {
    if (comments == null) {
      return "Comments cannot be null";
    }
    return "";
  }
}
