package pl.coderstrust;

import lombok.*;

@Value
@AllArgsConstructor
public class ContactDetails {
  @NonNull
  String email;
  @NonNull
  String phoneNumber;
  @NonNull
  String website;
  @NonNull
  String additionalInformation;
  @NonNull
  Address address;
}
