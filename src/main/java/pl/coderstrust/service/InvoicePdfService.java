package pl.coderstrust.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Address;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.ContactDetails;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

@Service
public class InvoicePdfService {

  public byte[] createPdf(@NonNull Invoice invoice) throws Exception {

    ByteArrayOutputStream array = new ByteArrayOutputStream();

    Document document = new Document();
    PdfWriter.getInstance(document, array);

    document.open();

    document.add(getHeader("Invoice details", false));
    document.add(getInvoiceTable(invoice));
    document.add(getHeader("Seller details", true));
    document.add(getCompanyTable(invoice.getSeller()));
    document.add(getHeader("Contact details", false));
    document.add(getContactDetailsTable(invoice.getSeller().getContactDetails()));
    document.add(getHeader("Address details", false));
    document.add(getAddressDetailsTable(invoice.getSeller().getContactDetails().getAddress()));
    document.add(getHeader("Buyer details", true));
    document.add(getCompanyTable(invoice.getBuyer()));
    document.add(getHeader("Contact details", false));
    document.add(getContactDetailsTable(invoice.getBuyer().getContactDetails()));
    document.add(getHeader("Address details", false));
    document.add(getAddressDetailsTable(invoice.getBuyer().getContactDetails().getAddress()));
    document.add(getHeader("Invoice entries", true));
    document.add(getEntriesTable(invoice.getEntries()));
    document.addTitle(String.format("Invoice_%s", invoice.getId()));

    document.close();
    return array.toByteArray();
  }

  private PdfPTable getHeader(String headerName, boolean spacing) {
    String[] header = new String[] {headerName};
    PdfPTable tableHeader = new PdfPTable(1);
    if (spacing) {
      tableHeader.setSpacingBefore(10f);
    }
    tableHeader.setWidthPercentage(110f);
    tableHeader.deleteBodyRows();
    addTableHeader(tableHeader, header);
    return tableHeader;
  }

  private PdfPTable getInvoiceTable(Invoice invoice) {
    String[] invoiceHeaders = new String[] {"Type", "Issue Date", "Due Date", "Buyer name",
        "Seller name", "Total net value", "Total gross value", "Comments"};
    PdfPTable invoiceTable = new PdfPTable(8);
    invoiceTable.setWidthPercentage(110f);
    addTableHeader(invoiceTable, invoiceHeaders);
    addInvoiceRows(invoiceTable, invoice);
    return invoiceTable;
  }

  private PdfPTable getCompanyTable(Company company) {
    String[] companyHeaders = new String[] {"Name", "Tax identification number", "Iban number", "Local number"};
    PdfPTable companyTable = new PdfPTable(4);
    companyTable.setWidthPercentage(110f);
    addTableHeader(companyTable, companyHeaders);
    addCompanyRows(companyTable, company);
    return companyTable;
  }

  private PdfPTable getContactDetailsTable(ContactDetails contactDetails) {
    String[] contactDetailsHeaders = new String[] {"Email", "Phone number", "Website"};
    PdfPTable contactDetailsTable = new PdfPTable(3);
    contactDetailsTable.setWidthPercentage(110f);
    addTableHeader(contactDetailsTable, contactDetailsHeaders);
    addContactDetailsRows(contactDetailsTable, contactDetails);
    return contactDetailsTable;
  }

  private PdfPTable getAddressDetailsTable(Address address) {
    String[] addressHeaders = new String[] {"Street", "Number", "Postal Code", "City", "Country"};
    PdfPTable addressDetailsTable = new PdfPTable(5);
    addressDetailsTable.setWidthPercentage(110f);
    addTableHeader(addressDetailsTable, addressHeaders);
    addAddressRows(addressDetailsTable, address);
    return addressDetailsTable;
  }

  private PdfPTable getEntriesTable(List<InvoiceEntry> entries) {
    String[] entriesHeaders = new String[] {"Item", "Quantity", "Unit type", "Price",
        "Vat rate", "Net value", "Total gross value"};
    PdfPTable entriesTable = new PdfPTable(7);
    entriesTable.setWidthPercentage(110f);
    addTableHeader(entriesTable, entriesHeaders);
    for (int i = 0; i < entries.size(); i++) {
      addEntriesRows(entriesTable, entries, i);
    }
    return entriesTable;
  }

  private void addTableHeader(PdfPTable table, String[] headers) {
    Stream.of(headers)
        .forEach(columnTitle -> {
          PdfPCell header = new PdfPCell();
          header.setBackgroundColor(BaseColor.LIGHT_GRAY);
          header.setBorderWidth(2);
          header.setPhrase(new Phrase(columnTitle));
          table.addCell(header);
        });
  }

  private void addInvoiceRows(PdfPTable table, Invoice invoice) {
    table.addCell(invoice.getType().toString());
    table.addCell(invoice.getIssueDate().toString());
    table.addCell(invoice.getDueDate().toString());
    table.addCell(invoice.getBuyer().getName());
    table.addCell(invoice.getSeller().getName());
    table.addCell(invoice.getTotalNetValue().toString());
    table.addCell(invoice.getTotalGrossValue().toString());
    table.addCell(invoice.getComments());
  }

  private void addCompanyRows(PdfPTable table, Company company) {
    table.addCell(company.getName());
    table.addCell(company.getTaxIdentificationNumber());
    table.addCell(company.getAccountNumber().getIbanNumber());
    table.addCell(company.getAccountNumber().getLocalNumber());
  }

  private void addContactDetailsRows(PdfPTable table, ContactDetails contact) {
    table.addCell(contact.getEmail());
    table.addCell(contact.getPhoneNumber());
    table.addCell(contact.getWebsite());
  }

  private void addAddressRows(PdfPTable table, Address address) {
    table.addCell(address.getStreet());
    table.addCell(address.getNumber());
    table.addCell(address.getPostalCode());
    table.addCell(address.getCity());
    table.addCell(address.getCountry());
  }

  private void addEntriesRows(PdfPTable table, List<InvoiceEntry> entries, int index) {
    table.addCell(entries.get(index).getItem());
    table.addCell(entries.get(index).getQuantity().toString());
    table.addCell(entries.get(index).getUnit().toString());
    table.addCell(entries.get(index).getPrice().toString());
    table.addCell(Float.toString(entries.get(index).getVatRate().getValue()));
    table.addCell(entries.get(index).getNetValue().toString());
    table.addCell(entries.get(index).getGrossValue().toString());
  }
}
