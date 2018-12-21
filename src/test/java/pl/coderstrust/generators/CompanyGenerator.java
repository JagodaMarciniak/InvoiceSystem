package pl.coderstrust.generators;

import pl.coderstrust.model.AccountNumber;
import pl.coderstrust.model.Address;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.ContactDetails;

public final class CompanyGenerator {

  public static Company getSampleCompany() {
    String name = "Oracle Sp. z o. o.";
    String taxId = "12345678990";
    AccountNumber accountNumber = new AccountNumber(
        "PL83620519463926400000847295");
    ContactDetails contactDetails = new ContactDetails(0, "contact@oracle.com",
        "+1234567890", "www.oracle.com", new Address(0, "Wyroczni", "13A", "34-760", "Gdynia", "Polska"
    ));
    return new Company(0 ,name, taxId, accountNumber, contactDetails);
  }

  public static Company getSampleCompany(String companyName) {
    String name = companyName;
    String taxId = "12345678990";
    AccountNumber accountNumber = new AccountNumber(
        "PL83620519463926400000847295");
    ContactDetails contactDetails = new ContactDetails(0, "contact@oracle.com",
        "+1234567890", "www.oracle.com", new Address(0, "Wyroczni", "13A", "34-760", "Gdynia", "Polska"
    ));
    return new Company(0, name, taxId, accountNumber, contactDetails);
  }
}
