package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class FinancialDataPayer {
    private @NonNull String companyName;
    private @NonNull String taxIdentificationNumber;
    private @NonNull String bankAccountNumber;
}
