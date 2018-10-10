package pl.coderstrust;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Company {
    private AddressDataPayer addressDataPayer;
    private ContactDataPayer contactDataPayer;
    private FinancialDataPayer financialDataPayer;

    @Override
    public String toString() {
        return "Company{" +
                addressDataPayer.toString() +
                contactDataPayer.toString() +
                financialDataPayer.toString() +
                '}';
    }
}