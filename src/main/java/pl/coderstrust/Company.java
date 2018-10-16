package pl.coderstrust;

import lombok.*;

@Value
@AllArgsConstructor
public class Company {
  @NonNull
  String name;
  @NonNull
  String taxIdentificationNumber;
  @NonNull
  AccountNumber accountNumber;
  @NonNull
  ContactDetails contactDetails;
}
