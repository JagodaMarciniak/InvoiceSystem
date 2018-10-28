package pl.coderstrust;

public final class CompanyGenerator {

  public static Company getSampleCompany() {
    final String name = "Oracle Sp. z o. o.";
    final String taxId = "12345678990";
    final AccountNumber accountNumber = new AccountNumber(
        "PL83620519463926400000847295");
    final ContactDetails contactDetails = new ContactDetails("contact@oracle.com",
        "+1234567890", "www.oracle.com", new Address("Wyroczni", "13A", "34-760", "Gdynia", "Polska"
    ));
    return new Company(name, taxId, accountNumber, contactDetails);
  }
}
