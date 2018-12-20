package pl.coderstrust.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InvoiceType {

  STANDARD("Standard"),
  PRO_FORMA("Pro-forma"),
  DEBIT_MEMO("Debit memo");

  @Getter
  private final String value;
}
