package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.model.Address;

public class AdressValidator {

  public static List<String> validateAddress(Address adress) {
    List<String> result = new ArrayList<>();
    ValidationResultAdder.addResultOfValidation(result, validateStreet(adress.getStreet()));
    ValidationResultAdder.addResultOfValidation(result, validateNumber(adress.getNumber()));
    ValidationResultAdder.addResultOfValidation(result, validateCity(adress.getCity()));
    ValidationResultAdder.addResultOfValidation(result, validatePostalCode(adress.getPostalCode()));
    ValidationResultAdder.addResultOfValidation(result, validateCountry(adress.getCountry()));
    return result;
  }

  private static String validateStreet(String street) {
    if (street == null) {
      return "Street cannot be null";
    }
    if (street == "") {
      return "Street cannot be empty";
    }
    return "";
  }

  private static String validateNumber(String number) {
    if (number == null) {
      return "Number cannot be null";
    }
    if (number == "") {
      return "Number cannot be empty";
    }
    return "";
  }

  private static String validatePostalCode(String postalCode) {
    if (postalCode == null) {
      return "Postal code cannot be null";
    }
    if (postalCode == "") {
      return "Postal code cannot be empty";
    }
    return "";
  }

  private static String validateCity(String city) {
    if (city == null) {
      return "City cannot be null";
    }
    if (city == "") {
      return "City cannot be empty";
    }
    return "";
  }

  private static String validateCountry(String Country) {
    if (Country == null) {
      return "Country cannot be null";
    }
    if (Country == "") {
      return "Country cannot be empty";
    }
    return "";
  }
}
