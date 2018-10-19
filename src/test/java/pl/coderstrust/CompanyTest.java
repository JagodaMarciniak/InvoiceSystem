package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CompanyTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String name = "SampleCompanyName";
    String taxId = "573-213-99";
    AccountNumber accountNumber = AccountNumberGenerator.getSampleAccountNumber();
    ContactDetails contactDetails = ContactDetailsGenerator.getSampleContactDetails();

    //when
    Company company = new Company(name, taxId, accountNumber, contactDetails);

    //then
    assertEquals(name, company.getName());
    assertEquals(taxId, company.getTaxIdentificationNumber());
    assertEquals(accountNumber, company.getAccountNumber());
    assertEquals(contactDetails, company.getContactDetails());
  }

  @Test
  public void shouldThrowExceptionWhenCompanyNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(null, "Test",
          AccountNumberGenerator.getSampleAccountNumber(),
          ContactDetailsGenerator.getSampleContactDetails());
    });
  }

  @Test
  public void shouldThrowExceptionWhenTaxIdentificationNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", null,
          AccountNumberGenerator.getSampleAccountNumber(),
          ContactDetailsGenerator.getSampleContactDetails());
    });
  }

  @Test
  public void shouldThrowExceptionWhenAccountNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", "Test",
          null,
          ContactDetailsGenerator.getSampleContactDetails());
    });
  }

  @Test
  public void shouldThrowExceptionWhenContactDetailIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", "Test",
          AccountNumberGenerator.getSampleAccountNumber(),
          null);
    });
  }
}
