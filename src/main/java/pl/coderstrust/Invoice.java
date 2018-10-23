package pl.coderstrust;

import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class Invoice {
  @NonNull
  String id;

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

  String comments;
}
