package pl.coderstrust;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public enum Vat {

  VAT_23(BigDecimal.valueOf(0.23)),
  VAT_8(BigDecimal.valueOf(0.08)),
  VAT_5(BigDecimal.valueOf(0.05)),
  VAT_0(BigDecimal.valueOf(0.00));

  @NonNull
  @Getter
  private final BigDecimal value;
}
