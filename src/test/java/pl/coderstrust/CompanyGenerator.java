package pl.coderstrust;

public final class CompanyGenerator {
  private static final String NAME = "Oracle Sp. z o. o.";
  private static final String TAX_ID = "12345678990";
  private static final AccountNumber ACCOUNT_NUMBER = new AccountNumber(
      "PL83620519463926400000847295");
  private static final ContactDetails CONTACT_DETAILS = new ContactDetails("contact@oracle.com",
      "+1234567890", "www.oracle.com", new Address("Wyroczni", "13A", "34-760", "Gdynia", "Polska"
  ));

  public static Company getSampleCompany() {
    return new Company(NAME, TAX_ID, ACCOUNT_NUMBER, CONTACT_DETAILS);
  }
}
