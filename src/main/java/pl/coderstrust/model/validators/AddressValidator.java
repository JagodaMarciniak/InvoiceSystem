package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.Address;

public class AddressValidator {

  public static List<String> validateAddress(Address address) {
    if (address == null) {
      return Collections.singletonList("Address cannot be null");
    }

    List<String> result = new ArrayList<>();
    String resultOfStreetValidation = validateStreet(address.getStreet());
    ValidationResultAdder.addResultOfValidation(result, resultOfStreetValidation);
    String resultOfNumberValidation = validateNumber(address.getNumber());
    ValidationResultAdder.addResultOfValidation(result, resultOfNumberValidation);
    String resultOfCityValidation = validateCity(address.getCity());
    ValidationResultAdder.addResultOfValidation(result, resultOfCityValidation);
    String resultOfPostalCodeValidation = validatePostalCode(address.getPostalCode());
    ValidationResultAdder.addResultOfValidation(result, resultOfPostalCodeValidation);
    String resultOfCountryValidation = validateCountry(address.getCountry());
    ValidationResultAdder.addResultOfValidation(result, resultOfCountryValidation);
    return result;
  }

  private static String validateStreet(String street) {
    if (street == null) {
      return "Street cannot be null";
    }
    if (street == "") {
      return "Street cannot be empty";
    }
    return null;
  }

  private static String validateNumber(String number) {
    if (number == null) {
      return "Number cannot be null";
    }
    if (number == "") {
      return "Number cannot be empty";
    }
    if (!number.matches(".*[0-9].*")) {
      return "Number has to contain only digits";
    }
    return null;
  }

  private static String validatePostalCode(String postalCode) {
    if (postalCode == null) {
      return "Postal code cannot be null";
    }
    if (postalCode == "") {
      return "Postal code cannot be empty";
    }
    if (!postalCode.matches("^([0-9]{2}[0-9]{3})")){
      return "Postal code has to contain only numbers";
    };
    return null;
  }

  private static String validateCity(String city) {
    if (city == null) {
      return "City cannot be null";
    }
    if (city == "") {
      return "City cannot be empty";
    }
    return null;
  }

  private static String validateCountry(String Country) {
    if (Country == null) {
      return "Country cannot be null";
    }
    if (Country == "") {
      return "Country cannot be empty";
    }
    return null;
  }
}
