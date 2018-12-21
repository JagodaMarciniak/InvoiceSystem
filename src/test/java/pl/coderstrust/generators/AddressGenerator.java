package pl.coderstrust.generators;

import pl.coderstrust.model.Address;

public class AddressGenerator {
  public static Address getSampleAddress() {
    return new Address(0, "Wojska Polskiego", "4/6", "66-951", "Gdynia", "Poland");
  }
}
