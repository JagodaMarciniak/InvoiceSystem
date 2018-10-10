package pl.coderstrust;

public class FinancialDataPayer {

    private String companyName;
    private String taxIdentificationNumber;
    private String bankAccountNumber;

    public FinancialDataPayer(String companyName, String taxIdentificationNumber, String bankAccountNumber){
        this.companyName = companyName;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.bankAccountNumber = bankAccountNumber;
    }

    @Override
    public String toString() {
        return "FinancialDataPayer{" +
                "companyName='" + companyName + '\'' +
                ", taxIdentificationNumber='" + taxIdentificationNumber + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                '}';
    }
}
