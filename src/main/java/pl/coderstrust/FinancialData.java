package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FinancialData {
  private @NonNull String taxId;
  private @NonNull String accountNumber;
}
