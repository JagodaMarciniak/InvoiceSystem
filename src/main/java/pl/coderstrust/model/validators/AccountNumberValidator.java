package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.model.AccountNumber;

public class AccountNumberValidator {

  public static List<String> validateAccountNumber(AccountNumber accountNumber) {
    List<String> result = new ArrayList();
    addResultOfValidation(result, validateIbanNumber(accountNumber.getIbanNumber()));
    addResultOfValidation(result, validateLocalNumber(accountNumber.getLocalNumber()));
    return result;
  }

  public static String validateIbanNumber(String ibanNumber){
    if (ibanNumber == null) {
      return "Iban Number cannot be null";
    }
    if (ibanNumber == "") {
      return "Iban Number cannot be empty";
    }
    if (!ibanNumber.matches("^([A-Z]{2}[0-9]{26})")) {
      return "Iban Number is invalid";
    }
    return "";
  }

  public static String validateLocalNumber(String localNumber){
    if (localNumber == null) {
      return "Local number cannot be null";
    }
    if (localNumber == "") {
      return "Local number cannot be empty";
    }
    if (!localNumber.matches("[A-Z]{2}")) {
      return "Local number is invalid";
    }
    return "";
  }

  private static void addResultOfValidation(List<String> resultList, String resultOfValidation) {
    if (resultOfValidation!= null && !resultOfValidation.trim().isEmpty()){
      resultList.add(resultOfValidation);
    }
  }
}
