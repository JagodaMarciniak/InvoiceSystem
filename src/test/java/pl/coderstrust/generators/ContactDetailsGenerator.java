package pl.coderstrust.generators;

import pl.coderstrust.model.ContactDetails;

public class ContactDetailsGenerator {
  public static ContactDetails getSampleContactDetails() {
    return new ContactDetails("marianawik@gmail.com", "5239766", "www.marianawik.org.pl",
        AddressGenerator.getSampleAddress());
  }
}
