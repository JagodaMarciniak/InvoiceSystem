package pl.coderstrust.model.validators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceEntryValidator {

  public static List<String> validateEntries(List<InvoiceEntry> invoiceEntries) {
    if (invoiceEntries == null) {
      return Collections.singletonList("Invoice entries cannot be null");
    }

    List<String> result = new ArrayList<>();
    invoiceEntries.forEach(invoiceEntry -> {
          ValidationResultAdder.addResultOfValidation(result, validateSingleEntry(invoiceEntry));
        });
    return result;
  }

  public static List<String> validateSingleEntry(InvoiceEntry invoiceEntry){
    if (invoiceEntry == null) {
      return Collections.singletonList("Invoice entry cannot be null");
    }

    List<String> result = new ArrayList<>();
    String resultOfItemValidation = validateItem(invoiceEntry.getItem());
    ValidationResultAdder.addResultOfValidation(result, resultOfItemValidation);
    String resultOfGrossValueValidation = validateGrossValue(invoiceEntry.getGrossValue());
    ValidationResultAdder.addResultOfValidation(result, resultOfGrossValueValidation);
    String resultOfNetValueValidation = validateNetValue(invoiceEntry.getNetValue());
    ValidationResultAdder.addResultOfValidation(result, resultOfNetValueValidation);
    String resultOfPriceValidation = validatePrice(invoiceEntry.getPrice());
    ValidationResultAdder.addResultOfValidation(result, resultOfPriceValidation);
    String resultOfQuantityValidation = validateQuantity(invoiceEntry.getQuantity());
    ValidationResultAdder.addResultOfValidation(result, resultOfQuantityValidation);
    return result;
  }

  private static String validateItem(String item) {
    if (item == null) {
      return "Item cannot be null";
    }
    if (item ==  "") {
      return "Item cannot be empty";
    }
    return null;
  }

  private static String validateQuantity(Long quantity) {
    if (quantity == null) {
      return "Quantity cannot be null";
    }
    if (quantity ==  0) {
      return "Quantity cannot be zero";
    }
    return null;
  }

  private static String validatePrice(BigDecimal price) {
    if (price == null) {
      return "UnitType cannot be null";
    }
    if (price.signum() < 0) {
      return "Price has to be larger than 0";
    }
    return null;
  }

  private static String validateNetValue(BigDecimal netValue) {
    if (netValue == null) {
      return "Net value cannot be null";
    }
    if (netValue.doubleValue() < 0) {
      return "Net value cannot be lower than 0";
    }
    return null;
  }

  private static String validateGrossValue(BigDecimal grossValue) {
    if (grossValue == null) {
      return "Gross Value cannot be null";
    }
    if (grossValue.doubleValue() < 0) {
      return "Gross Value cannot be lower than 0";
    }
    return null;
  }
}
