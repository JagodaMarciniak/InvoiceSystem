package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.AccountNumber;

public class AccountNumberValidator extends Validator {

  public static List<String> validateAccountNumber(AccountNumber accountNumber) {
    if (accountNumber == null){
      return Collections.singletonList("Account number cannot be null");
    }

    List<String> result = new ArrayList();
    String resultOfIbanNumberValidation = validateIbanNumber(accountNumber.getIbanNumber());
    addResultOfValidation(result, resultOfIbanNumberValidation);
    String resultOfLocalNumberValidation = validateLocalNumber(accountNumber.getLocalNumber());
    addResultOfValidation(result, resultOfLocalNumberValidation);
    if (resultOfIbanNumberValidation == null && resultOfLocalNumberValidation == null) {
      String resultOfComparison = compareNumbers(accountNumber.getIbanNumber(), accountNumber.getLocalNumber());
      addResultOfValidation(result, resultOfComparison);
    }
    return result;
  }

  private static String validateIbanNumber(String ibanNumber){
    if (ibanNumber == null) {
      return "Iban number cannot be null";
    }
    if (ibanNumber.trim().isEmpty()) {
      return "Iban number cannot be empty";
    }
    if (!ibanNumber.matches("^([A-Z]{2}[0-9]{26})")) {
      return "Iban number is invalid";
    }
    return null;
  }

  private static String validateLocalNumber(String localNumber){
    if (localNumber == null) {
      return "Local number cannot be null";
    }
    if (localNumber.trim().isEmpty()) {
      return "Local number cannot be empty";
    }
    if (!localNumber.matches("^([0-9]{26})")) {
      return "Local number is invalid";
    }
    return null;
  }

  private static String compareNumbers(String ibanNumber, String localNumber) {
    if (!(ibanNumber.substring(2).equals(localNumber))){
      return "Iban number and local number do not fit";
    }
    return null;
  }
}
