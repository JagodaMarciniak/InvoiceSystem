package pl.coderstrust.controller;

import com.itextpdf.text.pdf.PdfBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.validators.InvoiceValidator;
import pl.coderstrust.service.InvoicePdfService;
import pl.coderstrust.service.InvoiceService;

@Slf4j
@RestController
@Api(value = "Invoices", description = "Available operations for invoice application", tags = {"Invoices"})
@RequestMapping("/invoices")
@CrossOrigin
public class InvoiceController {

  private final InvoiceService invoiceService;

  private final InvoicePdfService invoicePdfService;

  @Autowired
  public InvoiceController(@NonNull InvoiceService invoiceService, @NonNull InvoicePdfService invoicePdfService) {
    this.invoiceService = invoiceService;
    this.invoicePdfService = invoicePdfService;
  }

  @GetMapping
  @ApiOperation(
      value = "Get all invoices",
      notes = "Get all invoices from database",
      response = Invoice.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Invoice.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> getAll() {
    try {
      log.info("Getting all invoices");
      log.debug(String.format("Getting all invoices"));

      return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
    } catch (Exception e) {
      log.error(String.format("Internal server error while getting invoices."));
      return new ResponseEntity<>(new ErrorMessage("Internal server error while getting invoices."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{invoiceId}")
  @ApiOperation(
      value = "Get invoice by id.",
      notes = "Get invoice from database using it's id.",
      response = Invoice.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Invoice.class),
      @ApiResponse(code = 404, message = "Invoice not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> getById(@ApiParam(value = "ID of Invoice that need to be found.", required = true) @PathVariable("invoiceId") String invoiceId) {
    try {
      log.info("Getting invoice");
      Optional<Invoice> optionalInvoice = invoiceService.getInvoice(invoiceId);

      if (optionalInvoice.isPresent()) {
        log.debug(String.format("Getting invoice with id: %s", invoiceId));
        return new ResponseEntity<>(optionalInvoice.get(), HttpStatus.OK);
      }

      log.debug(String.format("Invoice not found for passed id: %s", invoiceId));
      return new ResponseEntity<>(new ErrorMessage("Invoice not found for passed id."), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      log.error(String.format("Internal server error while getting invoice by id: %s", invoiceId));
      return new ResponseEntity<>(new ErrorMessage(String.format("Internal server error while getting invoice by id: %s", invoiceId)),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  @ApiOperation(
      value = "Add invoice.",
      notes = "Add invoice to database.",
      response = Invoice.class)
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Created", response = Invoice.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> add(@ApiParam(value = "Invoice need to be added to database.", required = true) @RequestBody Invoice invoice) {
    try {
      log.info("Adding new invoice");
      log.debug(String.format("Adding new invoice: %s", invoice));
      List<String> resultOfValidation = InvoiceValidator.validateInvoice(invoice, false);

      if (resultOfValidation.size() > 0) {
        log.debug(String.format("Invoice validation failed: %s", resultOfValidation));
        return new ResponseEntity<>(new ErrorMessage("Passed invoice is invalid.", resultOfValidation), HttpStatus.BAD_REQUEST);
      }

      Invoice addedInvoice = invoiceService.addInvoice(invoice);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.setLocation(URI.create(String.format("/invoices/%s", addedInvoice.getId())));

      log.debug(String.format("Invoice successfully added: %s", invoice));
      return new ResponseEntity<>(addedInvoice, responseHeaders, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(String.format("Internal server error while saving specified invoice. Invoice: %s", invoice));
      return new ResponseEntity<>(new ErrorMessage("Internal server error while saving specified invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{invoiceId}")
  @ApiOperation(
      value = "Update invoice.",
      notes = "Update existing invoice in database.",
      response = Invoice.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Invoice.class),
      @ApiResponse(code = 400, message = "Passed data is invalid.", response = ErrorMessage.class),
      @ApiResponse(code = 404, message = "Invoice not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> update(
      @ApiParam(value = "Id of invoice to be updated.", required = true) @PathVariable String invoiceId,
      @ApiParam(value = "Invoice to be updated.", required = true) @RequestBody Invoice invoice) {
    try {
      log.info("Updating new invoice");
      log.debug(String.format("Updating new invoice: %s", invoice));
      List<String> resultOfValidation = InvoiceValidator.validateInvoice(invoice, true);

      if (resultOfValidation.size() > 0) {
        log.debug(String.format("Invoice validation failed: %s", resultOfValidation));
        return new ResponseEntity<>(new ErrorMessage("Passed invoice is invalid.", resultOfValidation), HttpStatus.BAD_REQUEST);
      }

      if (!invoiceId.equals(invoice.getId())) {
        log.debug("Passed data is invalid. Please verify invoice id.");
        return new ResponseEntity<>(new ErrorMessage("Passed data is invalid. Please verify invoice id."), HttpStatus.BAD_REQUEST);
      }

      if (!invoiceService.invoiceExists(invoiceId)) {
        log.debug("Invoice not found.");
        return new ResponseEntity<>(new ErrorMessage("Invoice not found."), HttpStatus.NOT_FOUND);
      }

      log.debug(String.format("Invoice successfully updated: %s", invoice));
      invoiceService.updateInvoice(invoice);
      return new ResponseEntity<>(invoice, HttpStatus.OK);
    } catch (Exception e) {
      log.error(String.format("Internal server error while updating specified invoice. Invoice: %s", invoice));
      return new ResponseEntity<>(new ErrorMessage("Internal server error while updating specified invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{invoiceId}")
  @ApiOperation(
      value = "Delete invoice.",
      notes = "Delete invoice from database.",
      response = Invoice.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Invoice.class),
      @ApiResponse(code = 404, message = "Invoice not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> delete(@ApiParam(value = "Id of invoice to be deleted.", required = true) @PathVariable("invoiceId") String invoiceId) {
    try {
      log.info("Deleting invoice");
      Optional<Invoice> optionalInvoice = invoiceService.getInvoice(invoiceId);

      if (!optionalInvoice.isPresent()) {
        log.debug("Invoice not found.");
        return new ResponseEntity<>(new ErrorMessage("Invoice not found."), HttpStatus.NOT_FOUND);
      }

      log.debug(String.format("Deleting invoice with id: %s", invoiceId));
      invoiceService.deleteInvoice(invoiceId);
      return new ResponseEntity<>(optionalInvoice.get(), HttpStatus.OK);
    } catch (Exception e) {
      log.error(String.format("Internal server error while deleting specified invoice."));
      return new ResponseEntity<>(new ErrorMessage("Internal server error while deleting specified invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/pdf/{invoiceId}")
  @ApiOperation(
      value = "Get pdf.",
      notes = "Get pdf of selected invoice.",
      response = PdfBody.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = byte[].class),
      @ApiResponse(code = 404, message = "Invoice not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> getPdf(@PathVariable("invoiceId") String invoiceId) throws Exception {
    try {
      log.info("Generating invoice pdf");
      Optional<Invoice> optionalInvoice = invoiceService.getInvoice(invoiceId);

      if (!optionalInvoice.isPresent()) {
        log.debug("Invoice not found.");
        return new ResponseEntity<>(new ErrorMessage("Invoice not found."), HttpStatus.NOT_FOUND);
      }

      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.setContentType(MediaType.APPLICATION_PDF);

      log.debug(String.format("Generating pdf of invoice : %s", optionalInvoice.get()));
      return new ResponseEntity<>(invoicePdfService.createPdf(optionalInvoice.get()), responseHeaders, HttpStatus.OK);
    } catch (Exception e) {
      log.error(String.format("Internal server error while trying to get PDF of invoice."));
      return new ResponseEntity<>(new ErrorMessage("Internal server error while trying to get PDF of invoice."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
