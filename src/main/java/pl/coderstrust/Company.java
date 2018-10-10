package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class Company {
    private @NonNull AddressDataPayer addressDataPayer;
    private @NonNull ContactDataPayer contactDataPayer;
    private @NonNull FinancialDataPayer financialDataPayer;

    @Override
    public String toString() {
        return "Company{" +
                addressDataPayer.toString() +
                contactDataPayer.toString() +
                financialDataPayer.toString() +
                '}';
    }
}