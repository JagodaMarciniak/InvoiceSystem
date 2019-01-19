package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.coderstrust.model.AccountNumber;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.ContactDetails;


public class CompanyValidator {

  public static List<String> validateCompany(Company company) {
    if (company == null) {
      return Collections.singletonList("Company cannot be null");
    }

    List<String> result = new ArrayList();
    String resultOfNameValidation = validateName(company.getName());
    ValidationResultAdder.addResultOfValidation(result, resultOfNameValidation);
    String resultOfTaxIdentificationNumberValidation = validateTaxIdentificationNumber(company.getTaxIdentificationNumber());
    ValidationResultAdder.addResultOfValidation(result, resultOfTaxIdentificationNumberValidation);
    List<String> resultOfAccountNumberValidation = validateAccountNumber(company.getAccountNumber());
    ValidationResultAdder.addResultOfValidation(result, resultOfAccountNumberValidation);
    List<String> resultOfContactDetailsValidation = validateContactDetails(company.getContactDetails());
    ValidationResultAdder.addResultOfValidation(result, resultOfContactDetailsValidation);
    return result;
  }

  private static String validateName(String name) {
    if (name == null) {
      return "Name cannot be null";
    }
    if (name ==  "") {
      return "Name cannot be empty";
    }
    return null;
  }

  private static String validateTaxIdentificationNumber(String taxId) {
    if (taxId == null) {
      return "Tax identification number cannot be null";
    }
    if (taxId == "") {
      return "Tax identification number cannot be null";
    }
    return null;
  }

  private static List<String> validateAccountNumber(AccountNumber accountNumber) {
   return AccountNumberValidator.validateAccountNumber(accountNumber);
  }

  private static List<String> validateContactDetails(ContactDetails contactDetails) {
    return ContactDetailsValidator.validateContactDetails(contactDetails);
  }
}
