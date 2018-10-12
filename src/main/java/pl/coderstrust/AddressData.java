package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class AddressData {
  private @NonNull String streetNameAndHouseNumber;
  private @NonNull String postalCode;
  private @NonNull String city;
  private @NonNull String country;
}
