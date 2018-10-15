package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContactDataTest {

  @ParameterizedTest
  @ValueSource(strings = {"janusz@interia.pl", "adam-malysz@gmail.com", "Poldek78@wp.pl"})
  public void testValidEmailAddress(String givenEmailAddress) {
    //given
    ContactData contactData = new ContactData(givenEmailAddress, "Test", "Test", "Test");

    //when
    String result = contactData.getEmail();

    //then
    assertEquals(givenEmailAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"5275325", "600100900", "42500326715"})
  public void testValidPhoneNumber(String givenPhoneNumber) {
    //given
    ContactData contactData = new ContactData("Test", givenPhoneNumber, "Test", "Test");

    //when
    String result = contactData.getPhoneNumber();

    //then
    assertEquals(givenPhoneNumber, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"www.wp.pl", "www.test.org.pl", "https://pl.wikipedia.org/wiki/Page"})
  public void testValidWwwPageAddress(String givenWwwPageAddress) {
    //given
    ContactData contactData = new ContactData("Test", "Test", givenWwwPageAddress, "Test");

    //when
    String result = contactData.getWebsite();

    //then
    assertEquals(givenWwwPageAddress, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"A4 Paper for third floor", "Fuel E0 KASKA", "Nothing to add"})
  public void testValidAdditionalInformationMessage(String givenAdditionalInformation) {
    //given
    ContactData contactData = new ContactData("Test", "Test", "Test", givenAdditionalInformation);

    //when
    String result = contactData.getAdditionalInformation();

    //then
    assertEquals(givenAdditionalInformation, result);
  }

  @Test
  public void testExceptionWhenEmailAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData(null, "Test", "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenPhoneNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData("Test", null, "Test", "Test");
    });
  }

  @Test
  public void testExceptionWhenWwwPageAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData("Test", "Test", null, "Test");
    });
  }

  @Test
  public void testExceptionWhenAdditionalInformationMassageIsNull() {
    assertThrows(NullPointerException.class, () -> {
      ContactData contactData = new ContactData("Test", "Test", "Test", null);
    });
  }
}
