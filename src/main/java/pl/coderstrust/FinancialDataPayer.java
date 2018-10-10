package pl.coderstrust;

public class FinancialDataPayer {

    private String name;
    private String taxIdentificationNumber;
    private String bankAccountNumber;

    public FinancialDataPayer(String name, String taxIdentificationNumber, String bankAccountNumber){
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.bankAccountNumber = bankAccountNumber;
    }
}
