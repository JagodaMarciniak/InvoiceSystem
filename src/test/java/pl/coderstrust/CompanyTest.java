package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CompanyTest {

  @ParameterizedTest
  @MethodSource("createArgumentsForCheckFullyInitializationTest")
  public void checkFullyInitialization(Company expectedCompany, String name, String taxId, ContactDetails contactDetails) {
    //given
    Company company = new Company(name, taxId, new AccountNumber(), contactDetails);

    //when
    String resultName = company.getName();
    String resultTaxId = company.getTaxIdentificationNumber();
    ContactDetails resultContactDetails = company.getContactDetails();

    //then
    assertEquals(expectedCompany, company);
    assertEquals(name, resultName);
    assertEquals(taxId, resultTaxId);
    assertEquals(contactDetails, resultContactDetails);
  }

  private static Stream<Arguments> createArgumentsForCheckFullyInitializationTest() {
    return Stream.of(
        Arguments.of(new Company("SampleName", "664-968-81",
                new AccountNumber(),
                new ContactDetails("marian@wp.pl", "765917142", "www.sample.com", "Empty",
                    new Address("Wojska Polskiego", "3", "66-976", "Tarnów", "Poland"))),
            "SampleName", "664-968-81",
            new ContactDetails("marian@wp.pl", "765917142", "www.sample.com", "Empty",
                new Address("Wojska Polskiego", "3", "66-976", "Tarnów", "Poland"))),
        Arguments.of(new Company("SampleName", "664-968-81",
                new AccountNumber(),
                new ContactDetails("karol-Szpak@gmail.com", "523976541", "www.sample-site.com", "Fuel Invoice",
                    new Address("Derdowskiego", "3a", "12-368", "Racławki", "Poland"))),
            "SampleName", "664-968-81",
            new ContactDetails("karol-Szpak@gmail.com", "523976541", "www.sample-site.com", "Fuel Invoice",
                new Address("Derdowskiego", "3a", "12-368", "Racławki", "Poland"))));
  }


  @Test
  public void testExceptionWhenCompanyNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(null, "Test",
          new AccountNumber(),
          new ContactDetails("Test", "Test", "Test", "Test",
              new Address("Test", "Test", "Test", "Test", "Test")));
    });
  }

  @Test
  public void testExceptionWhenTaxIdentificationNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", null,
          new AccountNumber(),
          new ContactDetails("Test", "Test", "Test", "Test",
              new Address("Test", "Test", "Test", "Test", "Test")));
    });
  }

  @Test
  public void testExceptionWhenAccountNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", "Test",
          null,
          new ContactDetails("Test", "Test", "Test", "Test",
              new Address("Test", "Test", "Test", "Test", "Test")));
    });
  }

  @Test
  public void testExceptionWhenContactDetailIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test", "Test",
          new AccountNumber(),
          null);
    });
  }
}
