package pl.coderstrust;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public enum InvoiceType {

  STANDARD("Standard"),
  PRO_FORMA("Pro-forma"),
  DEBIT_MEMO("Debit memo");

  @NonNull
  @Getter
  private final String value;
}
