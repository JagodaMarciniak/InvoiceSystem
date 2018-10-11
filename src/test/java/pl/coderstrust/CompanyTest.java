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
  @MethodSource("createArgumentsForAddressDataPayerCorrectInstantiationInCompanyClass")
  public void testForAddressDataPayerCorrectInstantiationInCompanyClass(AddressDataPayer givenClassAddressClass, List<Object> expected) {
    //given
    Company company = new Company(givenClassAddressClass,
        new ContactDataPayer("Example", "Example"),
        new FinancialDataPayer("Example", "Example", "Example"));

    //when
    List<Object> result = new ArrayList<>();
    result.add(givenClassAddressClass.getStreetName());
    result.add(givenClassAddressClass.getHouseNumber());
    result.add(givenClassAddressClass.getPostalCode());
    result.add(givenClassAddressClass.getCity());
    result.add(givenClassAddressClass.getCountry());

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForAddressDataPayerCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new AddressDataPayer("Ogrodowa","8/2","88-300", "Warszawa","Polska"),
                     new ArrayList<>(Arrays.asList("Ogrodowa", "8/2", "88-300", "Warszawa", "Polska"))),
        Arguments.of(new AddressDataPayer("Widokowa","3","21-845", "Gdynia","Polska"),
                     new ArrayList<>(Arrays.asList("Widokowa", "3", "21-845", "Gdynia", "Polska"))));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForContactDataPayerCorrectInstantiationInCompanyClass")
  public void testForContactDataPayerCorrectInstantiationInCompanyClass(ContactDataPayer givenContactDataPayer, List<Object> expected) {
    //given
    Company company = new Company(new AddressDataPayer("Example", "Example", "Example", "Example", "Example"),
        givenContactDataPayer,
        new FinancialDataPayer("Example", "Example", "Example"));

    //when
    List<Object> result = new ArrayList<>();
    result.add(givenContactDataPayer.getEmail());
    result.add(givenContactDataPayer.getPhoneNumber());

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForContactDataPayerCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new ContactDataPayer("janusz@wp.pl","545999765"),
            new ArrayList<>(Arrays.asList("janusz@wp.pl", "545999765"))),
        Arguments.of(new ContactDataPayer("testSample@gmail.com", "543-998-501"),
            new ArrayList<>(Arrays.asList("testSample@gmail.com", "543-998-501"))));
  }

  @ParameterizedTest
  @MethodSource("createArgumentsForFinancialDataPayerCorrectInstantiationInCompanyClass")
  public void testForFinancialDataPayerCorrectInstantiationInCompanyClass(FinancialDataPayer givenFinancialDataPayer, List<Object> expected) {
    //given
    Company company = new Company(new AddressDataPayer("Example", "Example", "Example", "Example", "Example"),
        new ContactDataPayer("Example", "Example"),
        givenFinancialDataPayer);

    //when
    List<Object> result = new ArrayList<>();
    result.add(givenFinancialDataPayer.getCompanyName());
    result.add(givenFinancialDataPayer.getTaxIdentificationNumber());
    result.add(givenFinancialDataPayer.getBankAccountNumber());

    //then
    assertEquals(expected, result);
  }

  private static Stream<Arguments> createArgumentsForFinancialDataPayerCorrectInstantiationInCompanyClass() {
    return Stream.of(
        Arguments.of(new FinancialDataPayer("SampleCompanyName", "457-825-99", "12345678901234567890123456"),
            new ArrayList<>(Arrays.asList("SampleCompanyName", "457-825-99", "12345678901234567890123456"))),
        Arguments.of(new FinancialDataPayer("SampleCompanyName Sp. z o.o", "2131233123", "12345678901234567890123456"),
            new ArrayList<>(Arrays.asList("SampleCompanyName Sp. z o.o", "2131233123", "12345678901234567890123456"))));
  }

  @Test
  public void testForExceptionWhenAddressDataPayerClassIsNull(){
    assertThrows(NullPointerException.class, ()->{
      new Company(null, new ContactDataPayer("Example", "Example"), new FinancialDataPayer("Example", "Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenContactDataPayerClassIsNull(){
    assertThrows(NullPointerException.class, ()->{
      new Company(new AddressDataPayer("Example", "Example", "Example", "Example", "Example"), null, new FinancialDataPayer("Example", "Example", "Example"));
    });
  }

  @Test
  public void testForExceptionWhenFinancialDataPayerClassIsNull(){
    assertThrows(NullPointerException.class, ()->{
      new Company(new AddressDataPayer("Example", "Example", "Example", "Example", "Example"), new ContactDataPayer("Example", "Example"), null);
    });
  }
}