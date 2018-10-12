package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class CompanyTest {

  @ParameterizedTest
  @MethodSource("createArgumentsForAddressDataCorrectInstantiationInCompanyClass")
  public void testForAddressDataCorrectInstantiationInCompanyClass(AddressData givenClassAddressClass, List<Object> expected) {
    //given
    Company company = new Company(givenClassAddressClass,
        new ContactData("Example", "Example", "Example", "Example"),
        new FinancialData("Example", "Example", "Example"));

    //when
    List<Object> result = new ArrayList<>();
    result.add(givenClassAddressClass.getStreetNameAndHouseNumber());
    result.add(givenClassAddressClass.getPostalCode());
    result.add(givenClassAddressClass.getCity());
    result.add(givenClassAddressClass.getCountry());

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForAddressDataCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new AddressData("Ogrodowa 8/2", "88-300", "Warszawa", "Polska"),
            new ArrayList<>(Arrays.asList("Ogrodowa 8/2", "88-300", "Warszawa", "Polska"))),
        Arguments.of(new AddressData("Widokowa 3", "21-845", "Gdynia", "Polska"),
            new ArrayList<>(Arrays.asList("Widokowa 3", "21-845", "Gdynia", "Polska"))));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForContactDataCorrectInstantiationInCompanyClass")
  public void testForContactDataCorrectInstantiationInCompanyClass(ContactData givenContactData, List<Object> expected) {
    //given
    Company company = new Company(new AddressData("Example", "Example", "Example", "Example"),
        givenContactData,
        new FinancialData("Example", "Example", "Example"));

    //when
    List<Object> result = new ArrayList<>();
    result.add(givenContactData.getEmail());
    result.add(givenContactData.getPhoneNumber());
    result.add(givenContactData.getWwwPage());
    result.add(givenContactData.getAdditionalInformation());

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForContactDataCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new ContactData("janusz@wp.pl", "545999765", "www.test.com", "Additional Information"),
            new ArrayList<>(Arrays.asList("janusz@wp.pl", "545999765", "www.test.com", "Additional Information"))),
        Arguments.of(new ContactData("testSample@gmail.com", "543-998-501", "https://pl.wikipedia.org/wiki/Page", "Additional Information"),
            new ArrayList<>(Arrays.asList("testSample@gmail.com", "543-998-501", "https://pl.wikipedia.org/wiki/Page", "Additional Information"))));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForFinancialDataCorrectInstantiationInCompanyClass")
  public void testForFinancialDataCorrectInstantiationInCompanyClass(FinancialData givenFinancialData, List<Object> expected) {
    //given
    Company company = new Company(new AddressData("Example", "Example", "Example", "Example"),
        new ContactData("Example", "Example", "Example", "Additional Information"),
        givenFinancialData);

    //when
    List<Object> result = new ArrayList<>();
    result.add(givenFinancialData.getCompanyName());
    result.add(givenFinancialData.getTaxIdentificationNumber());
    result.add(givenFinancialData.getBankAccountNumber());

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForFinancialDataCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new FinancialData("SampleCompanyName", "457-825-99", "12345678901234567890123456"),
            new ArrayList<>(Arrays.asList("SampleCompanyName", "457-825-99", "12345678901234567890123456"))),
        Arguments.of(new FinancialData("SampleCompanyName Sp. z o.o", "2131233123", "12345678901234567890123456"),
            new ArrayList<>(Arrays.asList("SampleCompanyName Sp. z o.o", "2131233123", "12345678901234567890123456"))));
  }

  @Test
  public void testForExceptionWhenAddressDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(null, new ContactData("Example", "Example", "Example", "Additional Information"), new FinancialData("Example", "Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenContactDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(new AddressData("Example", "Example", "Example", "Example"), null, new FinancialData("Example", "Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenFinancialDataClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new Company(new AddressData("Example", "Example", "Example", "Example"), new ContactData("Example", "Example", "Example", "Example"), null);
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
        Arguments.of(new Company(
                new AddressData("Ogrodowa 5", "65-976", "Warszawa", "Poland"),
                new ContactData("janusz@wp.pl", "987-765-241", "www.test.com", "Additional Information"),
                new FinancialData("Sample", "656-234-87", "123456788901234567890123456")),
            "Company{AddressData(streetNameAndHouseNumber=Ogrodowa 5, postalCode=65-976, city=Warszawa, country=Poland)ContactData(email=janusz@wp.pl, phoneNumber=987-765-241, wwwPage=www.test.com, additionalInformation=Additional Information)FinancialData(companyName=Sample, taxIdentificationNumber=656-234-87, bankAccountNumber=123456788901234567890123456)}"),
        Arguments.of(new Company(
                new AddressData("Wojska Polskiego 20/6", "12-987", "Gdańsk", "Poland"),
                new ContactData("maria-konieczna", "675241836", "https://pl.wikipedia.org/wiki/Page", "Additional Information"),
                new FinancialData("Sample Sp. z o.o", "65623487", "123456788901234567890123456")),
            "Company{AddressData(streetNameAndHouseNumber=Wojska Polskiego 20/6, postalCode=12-987, city=Gdańsk, country=Poland)ContactData(email=maria-konieczna, phoneNumber=675241836, wwwPage=https://pl.wikipedia.org/wiki/Page, additionalInformation=Additional Information)FinancialData(companyName=Sample Sp. z o.o, taxIdentificationNumber=65623487, bankAccountNumber=123456788901234567890123456)}")
    );
  }
}
