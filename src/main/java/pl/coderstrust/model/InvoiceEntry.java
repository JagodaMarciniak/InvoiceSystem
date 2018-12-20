package pl.coderstrust.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceEntry {
  String item;

  Long quantity;

  UnitType unit;

  BigDecimal price;

  Vat vatRate;

  BigDecimal netValue;

  BigDecimal grossValue;
}
