package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.model.Address;
import pl.coderstrust.model.ContactDetails;

public class ContactDetailsValidator {
  public static List<String> validateContactDetails(ContactDetails contactDetails) {
    List<String> result = new ArrayList<>();
    ValidationResultAdder.addResultOfValidation(result, validateEmail(contactDetails.getEmail()));
    ValidationResultAdder.addResultOfValidation(result, validatePhoneNumber(contactDetails.getPhoneNumber()));
    ValidationResultAdder.addResultOfValidation(result, validateWebsite(contactDetails.getWebsite()));
    ValidationResultAdder.addResultOfValidation(result, validateAdress(contactDetails.getAddress()));
    return result;
  }

  private static String validateEmail(String email) {
    if (email == null) {
      return "Email cannot be null";
    }
    if (email == "") {
      return "Email cannot be empty";
    }
    if (!email.matches("^(.+)@(.+)$")) {
      return "Email is not valid";
    }
    return "";
  }

  private static String validatePhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return "Phone number cannot be null";
    }
    if (phoneNumber == "") {
      return "Phone number cannot be empty";
    }
    if (!phoneNumber.matches("^[0-9]")){
      return "Phone number can only be numbers";
    }
    return "";
  }

  private static String validateWebsite(String website) {
    if (website == null) {
      return "Website cannot be null";
    }
    if (website == "") {
      return "Website cannot be empty";
    }
    if (!website.matches("@(https?|ftp)://(-\\.)?([^\\s/?\\.#-]+\\.?)+(/[^\\s]*)?$@iS")){
      return "Website has invalid format";
    }
    return "";
  }

  private static List<String> validateAdress(Address address) {
    return AdressValidator.validateAddress(address);
  }

}
