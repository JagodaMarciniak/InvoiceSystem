package pl.coderstrust.model;

import lombok.NonNull;
import lombok.Value;

@Value
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
