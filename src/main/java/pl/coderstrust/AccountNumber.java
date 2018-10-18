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
    this.ibanNumber = ibanNumber;
  }

  public String getLocalNumber() {
    return ibanNumber.substring(2);
  }

  public String getIban() {
    return ibanNumber;
  }
}
