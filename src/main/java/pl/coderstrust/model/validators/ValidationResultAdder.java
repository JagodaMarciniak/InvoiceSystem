package pl.coderstrust.model.validators;

import java.util.List;

public class ValidationResultAdder {
  public static void addResultOfValidation(List<String> resultList, List<String> resultsOfValidation) {
    resultsOfValidation.stream().forEach(element -> resultList.add(element));
  }

  public static void addResultOfValidation(List<String> resultList, String resultOfValidation) {
    if (resultOfValidation != null) {
      resultList.add(resultOfValidation);
    }
  }
}
