package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Company {
  private @NonNull String name;
  private @NonNull AddressData addressData;
  private @NonNull ContactData contactData;
  private @NonNull FinancialData financialData;

  @Override
  public String toString() {
    return "Company{"
        + "name='" + name + '\''
        + ", "
        + addressData.toString()
        + contactData.toString()
        + financialData.toString()
        + '}';
  }
}
