package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class CompanyTest {

  @ParameterizedTest
  @ValueSource(strings = {"Sample Wide, Sample, Sample S.p z o.o."})
  public void testValidCompanyName(String givenCompanyName) {
    //given
    Company company = new Company(givenCompanyName,
        new AddressData("Test", "Test", "Test", "Test"),
        new ContactData("Test", "Test", "Test", "Test"),
        new FinancialData("Test", "Test"));

    //when
    String result = company.getName();

    //then
    assertEquals(givenCompanyName, result);
  }

  @ParameterizedTest
  @MethodSource("createArgumentsFAddressDataCorrectInstantiation")
  public void testAddressDataCorrectInstantiation(AddressData givenClass, String expected) {
    //given
    Company company = new Company("Test",
        givenClass,
        new ContactData("Test", "Test", "Test", "Test"),
        new FinancialData("Test", "Test"));

    //when
    String result = company.getAddressData().toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsFAddressDataCorrectInstantiation() {
    return Stream.of(
        Arguments.of(new AddressData("Ogrodowa 8/2", "88-300", "Warszawa", "Polska"),
            new AddressData("Ogrodowa 8/2", "88-300", "Warszawa", "Polska").toString()),
        Arguments.of(new AddressData("Widokowa 3", "21-845", "Gdynia", "Polska"),
            new AddressData("Widokowa 3", "21-845", "Gdynia", "Polska").toString()));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsFContactDataCorrectInstantiation")
  public void testContactDataCorrectInstantiation(ContactData givenClass, String expected) {
    //given
    Company company = new Company("Test",
        new AddressData("Test", "Test", "Test", "Test"),
        givenClass,
        new FinancialData("Test", "Test"));

    //when
    String result = company.getContactData().toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsFContactDataCorrectInstantiation() {
    return Stream.of(
        Arguments.of(new ContactData("janusz@wp.pl", "545999765", "www.test.com", "Test"),
            new ContactData("janusz@wp.pl", "545999765", "www.test.com", "Test").toString()),
        Arguments.of(new ContactData("testSample@gmail.com", "543-998-501", "https://pl.wikipedia.org/wiki/Page", "Test"),
            new ContactData("testSample@gmail.com", "543-998-501", "https://pl.wikipedia.org/wiki/Page", "Test").toString()));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsFFinancialDataCorrectInstantiation")
  public void testFinancialDataCorrectInstantiation(FinancialData givenClass, String expected) {
    //given
    Company company = new Company("Test",
        new AddressData("Test", "Test", "Test", "Test"),
        new ContactData("Test", "Test", "Test", "Test"),
        givenClass);

    //when
    String result = company.getFinancialData().toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsFFinancialDataCorrectInstantiation() {
    return Stream.of(
        Arguments.of(new FinancialData("457-825-99", "12345678901234567890123456"),
            new FinancialData("457-825-99", "12345678901234567890123456").toString()),
        Arguments.of(new FinancialData("2131233123", "12345678901234567890123456"),
            new FinancialData("2131233123", "12345678901234567890123456").toString()));
  }

  @Test
  public void testExceptionWhenCompanyNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(null,
          new AddressData("Test", "Test", "Test", "Test"),
          new ContactData("Test", "Test", "Test", "Test"),
          new FinancialData("Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenAddressDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test",
          null,
          new ContactData("Test", "Test", "Test", "Test"),
          new FinancialData("Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenContactDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test",
          new AddressData("Test", "Test", "Test", "Test"),
          null,
          new FinancialData("Test", "Test"));
    });
  }

  @Test
  public void testExceptionWhenFinancialDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Test",
          new AddressData("Test", "Test", "Test", "Test"),
          new ContactData("Test", "Test", "Test", "Test"),
          null);
    });
  }

  @ParameterizedTest
  @MethodSource("createArgumentsFToStringMethodTest")
  public void testToStringMethod(Company givenCompany, String expected) {
    //when
    String result = givenCompany.toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsFToStringMethodTest() {
    return Stream.of(
        Arguments.of(new Company("SampleCompanyName",
                new AddressData("Ogrodowa 5", "65-976", "Warszawa", "Poland"),
                new ContactData("janusz@wp.pl", "987-765-241", "www.test.com", "Test"),
                new FinancialData("656-234-87", "123456788901234567890123456")),
            "Company{name='SampleCompanyName', "
                + "AddressData(streetNameAndHouseNumber=Ogrodowa 5, postalCode=65-976, city=Warszawa, country=Poland)"
                + "ContactData(email=janusz@wp.pl, phoneNumber=987-765-241, website=www.test.com, additionalInformation=Test)"
                + "FinancialData(taxId=656-234-87, accountNumber=123456788901234567890123456)}"),
        Arguments.of(new Company("SampleCompanyName Sp. z o.o.",
                new AddressData("Wojska Polskiego 20/6", "12-987", "Gdańsk", "Poland"),
                new ContactData("maria-konieczna@gmail.com", "675241836", "https://pl.wikipedia.org/wiki/Page", "Test"),
                new FinancialData("65623487", "123456788901234567890123456")),
            "Company{name='SampleCompanyName Sp. z o.o.', "
                + "AddressData(streetNameAndHouseNumber=Wojska Polskiego 20/6, postalCode=12-987, city=Gdańsk, country=Poland)"
                + "ContactData(email=maria-konieczna@gmail.com, phoneNumber=675241836, website=https://pl.wikipedia.org/wiki/Page, additionalInformation=Test)"
                + "FinancialData(taxId=65623487, accountNumber=123456788901234567890123456)}")
    );
  }
}
