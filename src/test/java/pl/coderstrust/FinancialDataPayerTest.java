package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FinancialDataPayerTest {

    @ParameterizedTest
    @ValueSource(strings = {"Sample Wide, Sample, Sample S.p z o.o"})
    public void testForValidCompanyName(String givenCompanyName){
        //given
        FinancialDataPayer financialDataPayer = new FinancialDataPayer(givenCompanyName, "Example", "Example");

        //when
        String result = financialDataPayer.getCompanyName();

        //then
        assertSame(givenCompanyName, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"725-18-01-126, 8451769793, 851-26-24-854"})
    public void testForValidTaxIdentificationNumber(String givenTaxIdentificationNumber){
        //given
        FinancialDataPayer financialDataPayer = new FinancialDataPayer("Example", givenTaxIdentificationNumber, "Example");

        //when
        String result = financialDataPayer.getTaxIdentificationNumber();

        //then
        assertSame(givenTaxIdentificationNumber, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2121 1009 0000 0002 3569 8741", "212110090000000235698741"})
    public void testForValidBankAccountNumber(String givenBankAccountNumber){
        //given
        FinancialDataPayer financialDataPayer = new FinancialDataPayer("Example", "Example", givenBankAccountNumber);

        //when
        String result = financialDataPayer.getBankAccountNumber();

        //then
        assertSame(givenBankAccountNumber, result);
    }

    @Test
    public void testForExceptionWhenCompanyNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            FinancialDataPayer financialDataPayer = new FinancialDataPayer(null, "Example", "Example");
        });
    }

    @Test
    public void testForExceptionWhenTaxIdentificationNumberIsNull(){
        assertThrows(NullPointerException.class, () -> {
            FinancialDataPayer financialDataPayer = new FinancialDataPayer("Example", null, "Example");
        });
    }

    @Test
    public void testForExceptionWhenBankAccountNumberIsNull(){
        assertThrows(NullPointerException.class, () -> {
            FinancialDataPayer financialDataPayer = new FinancialDataPayer("Example", "Example", null);
        });
    }
}