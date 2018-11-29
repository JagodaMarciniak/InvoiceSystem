package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class Invoice {
  @NonNull
  int id;

  @NonNull
  InvoiceType type;

  @NonNull
  LocalDate issueDate;

  @NonNull
  LocalDate dueDate;

  @NonNull
  Company seller;

  @NonNull
  Company buyer;

  @NonNull
  List<InvoiceEntry> entries;

  @NonNull
  BigDecimal totalNetValue;

  @NonNull
  BigDecimal totalGrossValue;

  String comments;
}
