package pl.coderstrust.generators;

import pl.coderstrust.model.ContactDetails;

public class ContactDetailsGenerator {
  public static ContactDetails getSampleContactDetails() {
    return new ContactDetails("maria-nawik@gmail.com", "5239766", "www.maria-nawik.org.pl",
        AddressGenerator.getSampleAddress());
  }
}
