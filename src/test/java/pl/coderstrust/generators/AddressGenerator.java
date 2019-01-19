package pl.coderstrust.generators;

import pl.coderstrust.model.Address;

public class AddressGenerator {
  public static Address getSampleAddress() {
    return new Address("Wojska Polskiego", "4/6", "66951", "Gdynia", "Poland");
  }
}
