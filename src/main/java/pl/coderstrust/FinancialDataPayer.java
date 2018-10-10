package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class FinancialDataPayer {
    private String companyName;
    private String taxIdentificationNumber;
    private String bankAccountNumber;
}
