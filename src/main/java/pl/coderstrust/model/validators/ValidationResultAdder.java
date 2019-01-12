package pl.coderstrust.model.validators;

import java.util.List;

public class ValidationResultAdder {
  public static void addResultOfValidation(List<String> resultList, List<String> resultOfValidation) {
    if (!(resultOfValidation.contains(null)) || !(resultOfValidation.contains(""))){
      resultList.addAll(resultOfValidation);
    }
  }

  public static void addResultOfValidation(List<String> resultList, String resultOfValidation) {
    if (!(resultOfValidation == null) || !(resultOfValidation.trim().isEmpty())) {
      resultList.add(resultOfValidation);
    }
  }
}
