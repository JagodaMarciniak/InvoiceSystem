package pl.coderstrust.controller;

import java.net.URI;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;

  @Autowired
  public InvoiceController(@NonNull InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    try {
      return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseMessage("Internal server error while getting invoices."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{invoiceId}")
  public ResponseEntity<?> get(@PathVariable("invoiceId") String invoiceId) {
    try {
      Optional<Invoice> optionalInvoice = invoiceService.getInvoice(invoiceId);
      if (optionalInvoice.isPresent()) {
        return new ResponseEntity<>(optionalInvoice.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ResponseMessage("Invoice not found for passed id."), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseMessage(String.format("Internal server error while getting invoice by id: %s", invoiceId)),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<?> add(@RequestBody Invoice invoice) {
    try {
      invoiceService.addInvoice(invoice);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.setLocation(URI.create(String.format("/invoices/%s", invoice.getId())));
      return new ResponseEntity<>(invoice, responseHeaders, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseMessage("Internal server error while saving specified invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{invoiceId}")
  public ResponseEntity<?> update(@PathVariable String invoiceId, @RequestBody Invoice invoice) {
    try {
      if (!invoiceId.equals(invoice.getId())) {
        return new ResponseEntity<>(new ResponseMessage("Passed data is invalid. Please verify invoice id."), HttpStatus.BAD_REQUEST);
      }
      if (!invoiceService.invoiceExists(invoiceId)) {
        return new ResponseEntity<>(new ResponseMessage("Invoice not found."), HttpStatus.NOT_FOUND);
      }
      invoiceService.updateInvoice(invoice);
      return new ResponseEntity<>(invoice, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseMessage("Internal server error while updating specified invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{invoiceId}")
  public ResponseEntity<?> delete(@PathVariable("invoiceId") String invoiceId) {
    try {
      Optional<Invoice> optionalInvoice = invoiceService.getInvoice(invoiceId);
      if (!optionalInvoice.isPresent()) {
        return new ResponseEntity<>(new ResponseMessage("Invoice not found."), HttpStatus.NOT_FOUND);
      }
      invoiceService.deleteInvoice(invoiceId);
      return new ResponseEntity<>(optionalInvoice.get(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseMessage("Internal server error while deleting specified invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
