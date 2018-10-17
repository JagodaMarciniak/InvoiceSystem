package pl.coderstrust;

import java.math.BigDecimal;

import lombok.NonNull;
import lombok.Value;

@Value
public class InvoiceEntry {

  @NonNull
  String item;
  @NonNull
  Integer quantity;
  @NonNull
  UnitType unit;
  @NonNull
  BigDecimal price;
  @NonNull
  Vat vat;
  @NonNull
  BigDecimal netValue;
  @NonNull
  BigDecimal grossValue;

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "{"
        + "item='" + item + '\''
        + ", quantity='" + quantity
        + ", unit=" + unit
        + ", price=" + price
        + ", vat=" + vat
        + ", netValue=" + netValue
        + ", grossValue=" + grossValue
        + "}";
  }
}
