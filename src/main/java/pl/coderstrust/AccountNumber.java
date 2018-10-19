package pl.coderstrust;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Value
@Getter(AccessLevel.NONE)
public class AccountNumber {
  private String ibanNumber;

  public AccountNumber(@NonNull String ibanNumber) {
    ibanNumber = ibanNumber.toUpperCase();
    if (!ibanNumber.matches("^([A-Z]{2}[0-9]{26})")) {
      throw new IllegalArgumentException("Incorrect iban number");
    }
    this.ibanNumber = ibanNumber;
  }

  public String getLocalNumber() {
    return ibanNumber.substring(2);
  }

  public String getIban() {
    return ibanNumber;
  }
}
