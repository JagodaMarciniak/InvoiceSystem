package pl.coderstrust.model.validators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceEntriesValidator {

  public static List<String> validateEntries(List<InvoiceEntry> invoiceEntries) {
    if (invoiceEntries == null) {
      return Collections.singletonList("Invoice entries cannot be null");
    }

    List<String> result = new ArrayList<>();
    invoiceEntries.forEach(invoiceEntry -> {
          Validator.addResultOfValidation(result, validateEntry(invoiceEntry));
        });
    return result;
  }

  public static List<String> validateEntry(InvoiceEntry invoiceEntry){
    if (invoiceEntry == null) {
      return Collections.singletonList("Invoice entry cannot be null");
    }

    List<String> result = new ArrayList<>();
    String resultOfItemValidation = validateItem(invoiceEntry.getItem());
    Validator.addResultOfValidation(result, resultOfItemValidation);
    String resultOfGrossValueValidation = validateGrossValue(invoiceEntry.getGrossValue());
    Validator.addResultOfValidation(result, resultOfGrossValueValidation);
    String resultOfNetValueValidation = validateNetValue(invoiceEntry.getNetValue());
    Validator.addResultOfValidation(result, resultOfNetValueValidation);
    String resultOfPriceValidation = validatePrice(invoiceEntry.getPrice());
    Validator.addResultOfValidation(result, resultOfPriceValidation);
    String resultOfQuantityValidation = validateQuantity(invoiceEntry.getQuantity());
    Validator.addResultOfValidation(result, resultOfQuantityValidation);
    return result;
  }

  private static String validateItem(String item) {
    if (item == null) {
      return "Item cannot be null";
    }
    if (item.trim().isEmpty()) {
      return "Item cannot be empty";
    }
    return null;
  }

  private static String validateQuantity(Long quantity) {
    if (quantity == null) {
      return "Quantity cannot be null";
    }
    if (quantity <= 0) {
      return "Quantity cannot be lower or equal to zero";
    }
    return null;
  }

  private static String validatePrice(BigDecimal price) {
    if (price == null) {
      return "Price cannot be null";
    }
    if (price.doubleValue() < 0) {
      return "Price cannot be lower than zero";
    }
    return null;
  }

  private static String validateNetValue(BigDecimal netValue) {
    if (netValue == null) {
      return "Net value cannot be null";
    }
    if (netValue.doubleValue() < 0) {
      return "Net value cannot be lower than zero";
    }
    return null;
  }

  private static String validateGrossValue(BigDecimal grossValue) {
    if (grossValue == null) {
      return "Gross value cannot be null";
    }
    if (grossValue.doubleValue() < 0) {
      return "Gross value cannot be lower than zero";
    }
    return null;
  }
}
