package pl.coderstrust.generators;

import pl.coderstrust.model.AccountNumber;
import pl.coderstrust.model.Address;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.ContactDetails;

public final class CompanyGenerator {

  public static Company getSampleCompany() {
    String name = "Oracle Sp. z o. o.";
    String taxId = "12345678990";
    AccountNumber accountNumber = AccountNumberGenerator.getSampleAccountNumber();
    ContactDetails contactDetails = new ContactDetails("contact@oracle.com",
        "1234567890", "http://www.oracle.com", new Address("Wyroczni", "13A", "34760", "Gdynia", "Polska"
    ));
    return new Company(name, taxId, accountNumber, contactDetails);
  }

  public static Company getSampleCompany(String companyName) {
    String name = companyName;
    String taxId = "12345678990";
    AccountNumber accountNumber = AccountNumberGenerator.getSampleAccountNumber();
    ContactDetails contactDetails = new ContactDetails("contact@oracle.com",
        "1234567890", "http://www.oracle.com", new Address("Wyroczni", "13A", "34760", "Gdynia", "Polska"
    ));
    return new Company(name, taxId, accountNumber, contactDetails);
  }
}
