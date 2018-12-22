package pl.coderstrust.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class InvoiceEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  String item;

  Long quantity;

  UnitType unit;

  BigDecimal price;

  Vat vatRate;

  BigDecimal netValue;

  BigDecimal grossValue;

  public InvoiceEntry() {
  }
}
