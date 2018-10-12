package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContactDataTest {

  @ParameterizedTest
  @ValueSource(strings = {"janusz@interia.pl", "adam-malysz@gmail.com", "Poldek78@wp.pl"})
  public void testForValidEmailAddress(String givenEmailAddress) {
    //given
    ContactData contactData = new ContactData(givenEmailAddress, "Example", "Example", "Example");

    //when
    String result = contactData.getEmail();

    //then
    assertSame(givenEmailAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"5275325", "600100900", "42500326715"})
  public void testForValidPhoneNumber(String givenPhoneNumber) {
    //given
    ContactData contactData = new ContactData("Example", givenPhoneNumber, "Example", "Example");

    //when
    String result = contactData.getPhoneNumber();

    //then
    assertSame(givenPhoneNumber, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"www.wp.pl", "www.test.org.pl", "https://pl.wikipedia.org/wiki/Page"})
  public void testForValidWwwPageAddress(String givenWwwPageAddress) {
    //given
    ContactData contactData = new ContactData("Example", "Example", givenWwwPageAddress, "Example");

    //when
    String result = contactData.getWwwPage();

    //then
    assertSame(givenWwwPageAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"A4 Paper for third floor", "Fuel E0 KASKA", "Nothing to add"})
  public void testForValidAdditionalInformationMessage(String givenAdditionalInformation) {
    //given
    ContactData contactData = new ContactData("Example", "Example", "Example", givenAdditionalInformation);

    //when
    String result = contactData.getAdditionalInformation();

    //then
    assertSame(givenAdditionalInformation, result);
  }

  @Test
  public void testForExceptionWhenEmailAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData(null, "Example", "Example", "Example");
    });
  }

  @Test
  public void testForExceptionWhenPhoneNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData("Example", null, "Example", "Example");
    });
  }

  @Test
  public void testForExceptionWhenWwwPageAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData("Example", "Example", null, "Example");
    });
  }

  @Test
  public void testForExceptionWhenAdditionalInformationMassageIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData("Example", "Example", "Example", null);
    });
  }
}
