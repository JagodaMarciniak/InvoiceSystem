package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.AccountNumberGenerator;
import pl.coderstrust.generators.ContactDetailsGenerator;

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
    assertThrows(IllegalArgumentException.class, () -> {
      new Company(null, "573-213-99",
          AccountNumberGenerator.getSampleAccountNumber(),
          ContactDetailsGenerator.getSampleContactDetails());
    });
  }

  @Test
  public void shouldThrowExceptionWhenTaxIdentificationNumberIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Company("SampleCompanyName", null,
          AccountNumberGenerator.getSampleAccountNumber(),
          ContactDetailsGenerator.getSampleContactDetails());
    });
  }

  @Test
  public void shouldThrowExceptionWhenAccountNumberIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Company("SampleCompanyName", "573-213-99",
          null,
          ContactDetailsGenerator.getSampleContactDetails());
    });
  }

  @Test
  public void shouldThrowExceptionWhenContactDetailIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Company("SampleCompanyName", "573-213-99",
          AccountNumberGenerator.getSampleAccountNumber(),
          null);
    });
  }
}
