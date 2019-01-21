package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.Address;
import pl.coderstrust.model.ContactDetails;

public class ContactDetailsValidator extends Validator {
  public static List<String> validateContactDetails(ContactDetails contactDetails) {
    if (contactDetails == null) {
      return Collections.singletonList("Contact details cannot be null");
    }

    List<String> result = new ArrayList<>();
    String resultOfEmailValidation = validateEmail(contactDetails.getEmail());
    addResultOfValidation(result, resultOfEmailValidation);
    String resultOfPhoneNumberValidation = validatePhoneNumber(contactDetails.getPhoneNumber());
    addResultOfValidation(result, resultOfPhoneNumberValidation);
    String resultOfWebsiteValidation = validateWebsite(contactDetails.getWebsite());
    addResultOfValidation(result, resultOfWebsiteValidation);
    List<String> resultOfAddressValidation = validateAddress(contactDetails.getAddress());
    addResultOfValidation(result, resultOfAddressValidation);
    return result;
  }

  private static String validateEmail(String email) {
    if (email == null) {
      return "Email cannot be null";
    }
    if (email.trim().isEmpty()) {
      return "Email cannot be empty";
    }
    if (!email.matches("^(.+)@(.+)$")) {
      return "Email is not valid";
    }
    return null;
  }

  private static String validatePhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return "Phone number cannot be null";
    }
    if (phoneNumber.trim().isEmpty()) {
      return "Phone number cannot be empty";
    }
    if (!phoneNumber.matches("[0-9]+")) {
      return "Phone number can only be numbers";
    }
    return null;
  }

  private static String validateWebsite(String website) {
    if (website == null) {
      return "Website cannot be null";
    }
    if (website.trim().isEmpty()) {
      return "Website cannot be empty";
    }
    if (!website.matches("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
      return "Website has invalid format";
    }
    return null;
  }

  private static List<String> validateAddress(Address address) {
    return AddressValidator.validateAddress(address);
  }
}
