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
  public void testForValidCompanyName(String givenCompanyName) {
    //given
    Company company = new Company(givenCompanyName,
        new AddressData("Example", "Example", "Example", "Example"),
        new ContactData("Example", "Example", "Example", "Example"),
        new FinancialData("Example", "Example"));

    //when
    String result = company.getName();

    //then
    assertEquals(givenCompanyName, result);
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForAddressDataCorrectInstantiationInCompanyClass")
  public void testForAddressDataCorrectInstantiationInCompanyClass(AddressData givenClassAddressClass, String expected) {
    //given
    Company company = new Company("Example",
        givenClassAddressClass,
        new ContactData("Example", "Example", "Example", "Example"),
        new FinancialData("Example", "Example"));

    //when
    String result = company.getAddressData().toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForAddressDataCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new AddressData("Ogrodowa 8/2", "88-300", "Warszawa", "Polska"),
            new AddressData("Ogrodowa 8/2", "88-300", "Warszawa", "Polska").toString()),
        Arguments.of(new AddressData("Widokowa 3", "21-845", "Gdynia", "Polska"),
            new AddressData("Widokowa 3", "21-845", "Gdynia", "Polska").toString()));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForContactDataCorrectInstantiationInCompanyClass")
  public void testForContactDataCorrectInstantiationInCompanyClass(ContactData givenContactData, String expected) {
    //given
    Company company = new Company("Example",
        new AddressData("Example", "Example", "Example", "Example"),
        givenContactData,
        new FinancialData("Example", "Example"));

    //when
    String result = company.getContactData().toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForContactDataCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new ContactData("janusz@wp.pl", "545999765", "www.test.com", "Additional Information"),
            new ContactData("janusz@wp.pl", "545999765", "www.test.com", "Additional Information").toString()),
        Arguments.of(new ContactData("testSample@gmail.com", "543-998-501", "https://pl.wikipedia.org/wiki/Page", "Additional Information"),
            new ContactData("testSample@gmail.com", "543-998-501", "https://pl.wikipedia.org/wiki/Page", "Additional Information").toString()));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForFinancialDataCorrectInstantiationInCompanyClass")
  public void testForFinancialDataCorrectInstantiationInCompanyClass(FinancialData givenFinancialData, String expected) {
    //given
    Company company = new Company("Example",
        new AddressData("Example", "Example", "Example", "Example"),
        new ContactData("Example", "Example", "Example", "Additional Information"),
        givenFinancialData);

    //when
    String result = company.getFinancialData().toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForFinancialDataCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new FinancialData("457-825-99", "12345678901234567890123456"),
            new FinancialData("457-825-99", "12345678901234567890123456").toString()),
        Arguments.of(new FinancialData("2131233123", "12345678901234567890123456"),
            new FinancialData("2131233123", "12345678901234567890123456").toString()));
  }

  @Test
  public void testForExceptionWhenCompanyNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(null,
          new AddressData("Example", "Example", "Example", "Example"),
          new ContactData("Example", "Example", "Example", "Example"),
          new FinancialData("Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenAddressDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Example", null, new ContactData("Example", "Example", "Example", "Additional Information"), new FinancialData("Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenContactDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Example", new AddressData("Example", "Example", "Example", "Example"), null, new FinancialData("Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenFinancialDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company("Example", new AddressData("Example", "Example", "Example", "Example"), new ContactData("Example", "Example", "Example", "Example"), null);
    });
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForToStringMethodTest")
  public void testForToStringMethod(Company givenCompany, String expected) {
    //when
    String result = givenCompany.toString();

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForToStringMethodTest() {
    return Stream.of(
        Arguments.of(new Company("SampleCompanyName",
                new AddressData("Ogrodowa 5", "65-976", "Warszawa", "Poland"),
                new ContactData("janusz@wp.pl", "987-765-241", "www.test.com", "Additional Information"),
                new FinancialData("656-234-87", "123456788901234567890123456")),
            "Company{name='SampleCompanyName', AddressData(streetNameAndHouseNumber=Ogrodowa 5, postalCode=65-976, city=Warszawa, country=Poland)ContactData(email=janusz@wp.pl, phoneNumber=987-765-241, website=www.test.com, additionalInformation=Additional Information)FinancialData(taxId=656-234-87, accountNumber=123456788901234567890123456)}"),
        Arguments.of(new Company("SampleCompanyName Sp. z o.o.",
                new AddressData("Wojska Polskiego 20/6", "12-987", "Gdańsk", "Poland"),
                new ContactData("maria-konieczna@gmail.com", "675241836", "https://pl.wikipedia.org/wiki/Page", "Additional Information"),
                new FinancialData("65623487", "123456788901234567890123456")),
            "Company{name='SampleCompanyName Sp. z o.o.', AddressData(streetNameAndHouseNumber=Wojska Polskiego 20/6, postalCode=12-987, city=Gdańsk, country=Poland)ContactData(email=maria-konieczna@gmail.com, phoneNumber=675241836, website=https://pl.wikipedia.org/wiki/Page, additionalInformation=Additional Information)FinancialData(taxId=65623487, accountNumber=123456788901234567890123456)}")
    );
  }
}
