package pl.coderstrust;

import lombok.*;

@Value
@AllArgsConstructor
public class Address {
  @NonNull
  String street;
  @NonNull
  String number;
  @NonNull
  String postalCode;
  @NonNull
  String city;
  @NonNull
  String country;
}
