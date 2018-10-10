package pl.coderstrust;

public class Company {

    private AddressDataPayer addressDataPayer;
    private ContactDataPayer contactDataPayer;
    private FinancialDataPayer financialDataPayer;

    public Company(AddressDataPayer addressDataPayer, ContactDataPayer contactDataPayer, FinancialDataPayer financialDataPayer){
        this.addressDataPayer = addressDataPayer;
        this.contactDataPayer = contactDataPayer;
        this.financialDataPayer = financialDataPayer;
    }
}