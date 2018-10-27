package pl.coderstrust.model;

import lombok.Data;
import lombok.NonNull;

@Data
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
