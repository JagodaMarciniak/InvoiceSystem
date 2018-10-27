package pl.coderstrust.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class AccountNumber {
  private String ibanNumber;
  private String localNumber;

  public AccountNumber(@NonNull String ibanNumber) {
    if (!ibanNumber.matches("^([A-Z]{2}[0-9]{26})")) {
      throw new IllegalArgumentException("Incorrect iban number");
    }
    this.ibanNumber = ibanNumber;
    this.localNumber = ibanNumber.substring(2);
  }
}
