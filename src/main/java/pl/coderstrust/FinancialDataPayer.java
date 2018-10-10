package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class FinancialDataPayer {
    private @NonNull String companyName;
    private @NonNull String taxIdentificationNumber;
    private @NonNull String bankAccountNumber;
}
