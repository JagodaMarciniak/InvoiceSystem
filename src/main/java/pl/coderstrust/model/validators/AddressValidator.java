package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.Address;

public class AddressValidator extends Validator {

  public static List<String> validateAddress(Address address) {
    if (address == null) {
      return Collections.singletonList("Address cannot be null");
    }

    List<String> result = new ArrayList<>();
    String resultOfStreetValidation = validateStreet(address.getStreet());
    addResultOfValidation(result, resultOfStreetValidation);
    String resultOfNumberValidation = validateNumber(address.getNumber());
    addResultOfValidation(result, resultOfNumberValidation);
    String resultOfCityValidation = validateCity(address.getCity());
    addResultOfValidation(result, resultOfCityValidation);
    String resultOfPostalCodeValidation = validatePostalCode(address.getPostalCode());
    addResultOfValidation(result, resultOfPostalCodeValidation);
    String resultOfCountryValidation = validateCountry(address.getCountry());
    addResultOfValidation(result, resultOfCountryValidation);
    return result;
  }

  private static String validateStreet(String street) {
    if (street == null) {
      return "Street cannot be null";
    }
    if (street.trim().isEmpty()) {
      return "Street cannot be empty";
    }
    return null;
  }

  private static String validateNumber(String number) {
    if (number == null) {
      return "Number cannot be null";
    }
    if (number.trim().isEmpty()) {
      return "Number cannot be empty";
    }
    return null;
  }

  private static String validatePostalCode(String postalCode) {
    if (postalCode == null) {
      return "Postal code cannot be null";
    }
    if (postalCode.trim().isEmpty()) {
      return "Postal code cannot be empty";
    }
    if (!postalCode.matches("^([0-9]{2}[0-9]{3})")) {
      return "Postal code has to contain only numbers";
    }
    return null;
  }

  private static String validateCity(String city) {
    if (city == null) {
      return "City cannot be null";
    }
    if (city.trim().isEmpty()) {
      return "City cannot be empty";
    }
    return null;
  }

  private static String validateCountry(String country) {
    if (country == null) {
      return "Country cannot be null";
    }
    if (country.trim().isEmpty()) {
      return "Country cannot be empty";
    }
    return null;
  }
}
