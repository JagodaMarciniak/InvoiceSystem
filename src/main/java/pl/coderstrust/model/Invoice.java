package pl.coderstrust.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoice {
  int id;

  InvoiceType type;

  LocalDate issueDate;

  LocalDate dueDate;

  Company seller;

  Company buyer;

  List<InvoiceEntry> entries;

  BigDecimal totalNetValue;

  BigDecimal totalGrossValue;

  String comments;
}
