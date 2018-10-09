package pl.coderstrust;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceEntry {

  private String item;
  private String quantity;
  private UnitType unit;
  private BigDecimal price;
  private Vat vat;

  public InvoiceEntry(String item, String quantity, UnitType unit, BigDecimal price, Vat vat) {
    this.item = item;
    this.quantity = quantity;
    this.unit = unit;
    this.price = price;
    this.vat = vat;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public UnitType getUnit() {
    return unit;
  }

  public void setUnit(UnitType unit) {
    this.unit = unit;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Vat getVat() {
    return vat;
  }

  public void setVat(Vat vat) {
    this.vat = vat;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) object;
    return Objects.equals(item, that.item)
        && Objects.equals(quantity, that.quantity)
        && unit == that.unit
        && Objects.equals(price, that.price)
        && vat == that.vat;
  }

  @Override
  public int hashCode() {
    return Objects.hash(item, quantity, unit, price, vat);
  }

  @Override
  public String toString() {
    return "InvoiceEntry{"
        + "item='" + item + '\''
        + ", quantity='" + quantity + '\''
        + ", unit=" + unit
        + ", price=" + price
        + ", vat=" + vat
        + '}';
  }
}
