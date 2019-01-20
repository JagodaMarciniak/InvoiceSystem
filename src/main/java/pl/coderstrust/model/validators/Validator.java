package pl.coderstrust.model.validators;

import java.util.List;

abstract class Validator {
  static void addResultOfValidation(List<String> resultList, List<String> resultsOfValidation) {
    resultsOfValidation.stream().forEach(element -> resultList.add(element));
  }

  static void addResultOfValidation(List<String> resultList, String resultOfValidation) {
    if (resultOfValidation != null) {
      resultList.add(resultOfValidation);
    }
  }
}
