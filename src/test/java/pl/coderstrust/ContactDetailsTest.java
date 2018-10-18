package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContactDetailsTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String email = "janusz@gmail.com";
    String phoneNumber = "885-941-002";
    String website = "www.sampleWebsite.com";

    //when
    ContactDetails contactDetails = new ContactDetails(email, phoneNumber, website,
        AddressClassGenerator.getSampleAddressClass());

    //then
    assertEquals(email, contactDetails.getEmail());
    assertEquals(phoneNumber, contactDetails.getPhoneNumber());
    assertEquals(website, contactDetails.getWebsite());
  }

  @Test
  public void shouldThrowExceptionWhenEmailAddressIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new ContactDetails(null, "Test", "Test",
          AddressClassGenerator.getSampleAddressClass());
    });
  }

  @Test
  public void shouldThrowExceptionWhenPhoneNumberIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new ContactDetails("Test", null, "Test",
          AddressClassGenerator.getSampleAddressClass());
    });
  }

  @Test
  public void shouldThrowExceptionWhenWebsiteIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new ContactDetails("Test", "Test", null,
          AddressClassGenerator.getSampleAddressClass());
    });
  }

  @Test
  public void shouldThrowExceptionWhenAddressClassIsNull() {
    assertThrows(NullPointerException.class, () -> {
      new ContactDetails("Test", "Test", "Test",
          null);
    });
  }
}
