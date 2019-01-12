package pl.coderstrust.model.validators;

import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.model.AccountNumber;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.ContactDetails;


public class CompanyValidator {

  public static List<String> validateCompany(Company company) {
    List<String> result = new ArrayList();
    ValidationResultAdder.addResultOfValidation(result, validateName(company.getName()));
    ValidationResultAdder.addResultOfValidation(result, validateTaxIdentificationNumber(company.getTaxIdentificationNumber()));
    ValidationResultAdder.addResultOfValidation(result, validateAccountNumber(company.getAccountNumber()));
    ValidationResultAdder.addResultOfValidation(result, validateContactDetails(company.getContactDetails()));
    return result;
  }

  private static String validateName(String name) {
    if (name == null) {
      return "Name cannot be null";
    }
    if (name ==  "") {
      return "Name cannot be empty";
    }
    return "";
  }

  private static String validateTaxIdentificationNumber(String taxId) {
    if (taxId == null) {
      return "Tax identification number cannot be null";
    }
    if (taxId == "") {
      return "Tax identification number cannot be null";
    }
    return "";
  }

  private static List<String> validateAccountNumber(AccountNumber accountNumber) {
   return AccountNumberValidator.validateAccountNumber(accountNumber);
  }

  private static List<String> validateContactDetails(ContactDetails contactDetails) {
    return ContactDetailsValidator.validateContactDetails(contactDetails);
  }
}
