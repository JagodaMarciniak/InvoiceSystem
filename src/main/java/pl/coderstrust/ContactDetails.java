package pl.coderstrust;

import lombok.NonNull;
import lombok.Value;

@Value
public class ContactDetails {
  @NonNull
  String email;

  @NonNull
  String phoneNumber;

  @NonNull
  String website;

  @NonNull
  Address address;
}
