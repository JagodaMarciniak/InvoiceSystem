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

    //when
    Company company = new Company(name, taxId,
        AccountNumberClassGenerator.getSampleAccountNumberClass(),
        ContactDetailsClassGenerator.getSampleContactDetailsClass());
    AccountNumber expectedAccountNumber = new AccountNumber("PL83620519463926400000847295");
    ContactDetails expectedContactDetails = new ContactDetails("maria-nawik@gmail.com", "5239766", "www.maria-nawik.org.pl",
        new Address("Wojska Polskiego", "4/6", "66-951", "Gdynia", "Poland"));

    //then
    assertEquals(name, company.getName());
    assertEquals(taxId, company.getTaxIdentificationNumber());
    assertEquals(expectedAccountNumber, company.getAccountNumber());
    assertEquals(expectedContactDetails, company.getContactDetails());
  }

  @Test
  public void shouldThrowExceptionWhenCompanyNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(null, "Test",
          AccountNumberClassGenerator.getSampleAccountNumberClass(),
          ContactDetailsClassGenerator.getSampleContactDetailsClass());
    });
  }

  @Test
  public void shouldThrowExceptionWhenTaxIdentificationNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", null,
          AccountNumberClassGenerator.getSampleAccountNumberClass(),
          ContactDetailsClassGenerator.getSampleContactDetailsClass());
    });
  }

  @Test
  public void shouldThrowExceptionWhenAccountNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", "Test",
          null,
          ContactDetailsClassGenerator.getSampleContactDetailsClass());
    });
  }

  @Test
  public void shouldThrowExceptionWhenContactDetailIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", "Test",
          AccountNumberClassGenerator.getSampleAccountNumberClass(),
          null);
    });
  }
}
