package pl.coderstrust.generators;

import static pl.coderstrust.model.UnitType.HOUR;
import static pl.coderstrust.model.UnitType.PIECE;
import static pl.coderstrust.model.Vat.VAT_23;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.model.InvoiceEntry;

public final class InvoiceEntriesGenerator {
  public static List<InvoiceEntry> getSampleInvoiceEntries() {
    List<InvoiceEntry> entries = new ArrayList<>();
    entries.add(new InvoiceEntry("Flashlight BX24", 2L, PIECE,
        new BigDecimal(50), VAT_23, new BigDecimal(100), new BigDecimal(123)));
    entries.add(new InvoiceEntry("Swiss Army Knife", 1L, PIECE,
        new BigDecimal(200), VAT_23, new BigDecimal(200), new BigDecimal(246)));
    entries.add(new InvoiceEntry("Engine oil change", 1L, HOUR,
        new BigDecimal(100), VAT_23, new BigDecimal(100), new BigDecimal(123)));
    return entries;
  }
}
