package pl.coderstrust.generators;

import pl.coderstrust.model.AccountNumber;

public class AccountNumberGenerator {

  private static String ibanNumber = "PL83620519463926400000847295";
  private static String localNumber = ibanNumber.substring(2);

  public static AccountNumber getSampleAccountNumber() {
    return new AccountNumber(ibanNumber, localNumber);
  }
}
