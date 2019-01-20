package pl.coderstrust.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.coderstrust.configuration.ApplicationConfiguration;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoicePdfService;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

  private ObjectMapper mapper = new ApplicationConfiguration().getObjectMapper();
  private final String urlAddressTemplate = "/invoices/%s";
  private final String urlAddressTemplatePdf = "/invoices/pdf/%s";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InvoiceService invoiceService;

  @MockBean
  private InvoicePdfService invoicePdfService;

  @Test
  void shouldReturnAllInvoices() throws Exception {
    //given
    List<Invoice> expectedInvoiceList = Arrays.asList(InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice());
    when(invoiceService.getAllInvoices()).thenReturn(expectedInvoiceList);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, ""))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    List<Invoice> actualInvoices = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Invoice>>() {
    });

    //then
    assertEquals(HttpStatus.OK.value(), httpStatus);
    assertNotNull(actualInvoices);
    assertEquals(expectedInvoiceList, actualInvoices);
    verify(invoiceService).getAllInvoices();
  }

  @Test
  void shouldReturnInternalServerErrorDuringGettingAllInvoicesWhenSomethingWentWrongOnServer() throws Exception {
    //given
    when(invoiceService.getAllInvoices()).thenThrow(new ServiceOperationException());
    ResponseMessage expectedResponseMessage = new ResponseMessage("Internal server error while getting invoices.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, ""))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).getAllInvoices();
  }

  @Test
  void shouldReturnSpecificInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenReturn(Optional.of(expectedInvoice));

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    Invoice actualInvoice = mapper.readValue(result.getResponse().getContentAsString(), Invoice.class);

    //then
    assertEquals(HttpStatus.OK.value(), httpStatus);
    assertNotNull(actualInvoice);
    assertEquals(expectedInvoice, actualInvoice);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  void shouldReturnNotFoundStatusWhenInvoiceNotExisting() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenReturn(Optional.ofNullable(null));
    ResponseMessage expectedResponseMessage = new ResponseMessage("Invoice not found for passed id.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  void shouldReturnInternalServiceErrorDuringGettingSpecificInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenThrow(new ServiceOperationException());
    ResponseMessage expectedResponseMessage = new ResponseMessage(String.format("Internal server error while getting invoice by id: %s",
        expectedInvoice.getId()));

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  void shouldAddInvoice() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice = InvoiceGenerator.copyInvoice(invoiceToAdd);
    invoiceToAdd.setId(null);
    when(invoiceService.addInvoice(invoiceToAdd)).thenReturn(expectedInvoice);
    String invoiceAsJson = mapper.writeValueAsString(invoiceToAdd);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .post(String.format(urlAddressTemplate, ""))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    Invoice actualInvoice = mapper.readValue(result.getResponse().getContentAsString(), Invoice.class);

    //then
    assertEquals(HttpStatus.CREATED.value(), httpStatus);
    assertNotNull(actualInvoice);
    assertEquals(expectedInvoice, actualInvoice);
    String expectedLocationHeader = String.format("/invoices/%s", expectedInvoice.getId());
    String actualLocationHeader = result.getResponse().getHeader("location");
    assertEquals(expectedLocationHeader, actualLocationHeader);
    verify(invoiceService).addInvoice(invoiceToAdd);
  }

  @Test
  void shouldReturnValidationReportWhenAddingInvoiceWithId() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    Invoice expectedInvoice = InvoiceGenerator.copyInvoice(invoiceToAdd);
    when(invoiceService.addInvoice(invoiceToAdd)).thenReturn(expectedInvoice);
    String invoiceAsJson = mapper.writeValueAsString(invoiceToAdd);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .post(String.format(urlAddressTemplate, ""))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    "Id must be null"

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.CREATED.value(), httpStatus);
    assertNotNull(actualInvoice);
    assertEquals(expectedInvoice, actualInvoice);
    String expectedLocationHeader = String.format("/invoices/%s", expectedInvoice.getId());
    String actualLocationHeader = result.getResponse().getHeader("location");
    assertEquals(expectedLocationHeader, actualLocationHeader);
    verify(invoiceService).addInvoice(invoiceToAdd);
  }

  @Test
  void shouldReturnInternalServiceErrorDuringAddingInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    invoiceToAdd.setId(null);
    when(invoiceService.addInvoice(invoiceToAdd)).thenThrow(new ServiceOperationException());
    ResponseMessage expectedResponseMessage = new ResponseMessage("Internal server error while saving specified invoice.");

    String invoiceAsJson = mapper.writeValueAsString(invoiceToAdd);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .post(String.format(urlAddressTemplate, ""))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).addInvoice(invoiceToAdd);
  }

  @Test
  void shouldReturnBadRequestDuringAddingInvoiceWhenInvoiceContainsId() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    ResponseMessage expectedResponseMessage = new ResponseMessage("Passed invoice cannot contains id. It is generated by application.");

    String invoiceAsJson = mapper.writeValueAsString(invoiceToAdd);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .post(String.format(urlAddressTemplate, ""))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService, never()).addInvoice(invoiceToAdd);
  }

  @Test
  void shouldUpdateInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(expectedInvoice.getId())).thenReturn(true);

    String invoiceAsJson = mapper.writeValueAsString(expectedInvoice);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .put(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    Invoice actualInvoice = mapper.readValue(result.getResponse().getContentAsString(), Invoice.class);

    //then
    assertEquals(HttpStatus.OK.value(), httpStatus);
    assertNotNull(actualInvoice);
    assertEquals(expectedInvoice, actualInvoice);
    verify(invoiceService).updateInvoice(expectedInvoice);
  }

  @Test
  void shouldReturnBadRequestDuringUpdatingInvoiceWithWrongId() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    String wrongInvoiceId = String.valueOf(Integer.valueOf(expectedInvoice.getId()) + 1);
    String invoiceAsJson = mapper.writeValueAsString(expectedInvoice);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .put(String.format(urlAddressTemplate, wrongInvoiceId))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertNotEquals(expectedInvoice.getId(), wrongInvoiceId);
    ResponseMessage expectedResponseMessage = new ResponseMessage("Passed data is invalid. Please verify invoice id.");
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService, never()).updateInvoice(expectedInvoice);
  }

  @Test
  void shouldReturnNotFoundDuringUpdatingNonExistingInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(expectedInvoice.getId())).thenReturn(false);
    ResponseMessage expectedResponseMessage = new ResponseMessage("Invoice not found.");

    String invoiceAsJson = mapper.writeValueAsString(expectedInvoice);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .put(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).invoiceExists(expectedInvoice.getId());
  }

  @Test
  void shouldThrowInternalServerErrorDuringUpdatingWhenSomethingWentWrongWithServer() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(expectedInvoice.getId())).thenThrow(new ServiceOperationException());
    ResponseMessage expectedResponseMessage = new ResponseMessage("Internal server error while updating specified invoice.");

    String invoiceAsJson = mapper.writeValueAsString(expectedInvoice);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .put(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(invoiceAsJson))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).invoiceExists(expectedInvoice.getId());
  }

  @Test
  void shouldDeleteInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenReturn(Optional.of(expectedInvoice));
    doNothing().when(invoiceService).deleteInvoice(expectedInvoice.getId());

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .delete(String.format(urlAddressTemplate, expectedInvoice.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    Invoice actualInvoice = mapper.readValue(result.getResponse().getContentAsString(), Invoice.class);

    //then
    assertEquals(HttpStatus.OK.value(), httpStatus);
    assertNotNull(actualInvoice);
    assertEquals(expectedInvoice, actualInvoice);
    verify(invoiceService).deleteInvoice(expectedInvoice.getId());
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  void shouldReturnNotFoundDuringDeletingNonExistingInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenReturn(Optional.ofNullable(null));
    ResponseMessage expectedResponseMessage = new ResponseMessage("Invoice not found.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .delete(String.format(urlAddressTemplate, expectedInvoice.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualResponseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualResponseMessage);
    assertEquals(expectedResponseMessage, actualResponseMessage);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  void shouldThrowInternalServerErrorDuringDeletingWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenThrow(new ServiceOperationException());
    ResponseMessage expectedResponseMessage = new ResponseMessage("Internal server error while deleting specified invoice.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.delete(String.format(urlAddressTemplate, expectedInvoice.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualInvoiceResponse = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualInvoiceResponse);
    assertEquals(expectedResponseMessage, actualInvoiceResponse);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  void shouldReturnSpecificPdf() throws Exception {
    //given
    byte[] expectedArray = new byte[10];
    Invoice invoiceToPdf = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(invoiceToPdf.getId())).thenReturn(Optional.of(invoiceToPdf));
    when(invoicePdfService.createPdf(invoiceToPdf)).thenReturn(expectedArray);

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplatePdf, invoiceToPdf.getId()))
            .accept(MediaType.APPLICATION_PDF_VALUE))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    byte[] actualArray = result.getResponse().getContentAsByteArray();

    //then
    assertEquals(HttpStatus.OK.value(), httpStatus);
    assertNotNull(actualArray);
    assertArrayEquals(expectedArray, actualArray);
    verify(invoiceService).getInvoice(invoiceToPdf.getId());
    verify(invoicePdfService).createPdf(invoiceToPdf);
  }

  @Test
  void shouldThrowNotFoundExceptionWhenTryingToGetPdfWithInvalidInvoiceId() throws Exception {
    //given
    Invoice invoiceToPdf = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(invoiceToPdf.getId())).thenReturn(Optional.ofNullable(null));
    final ResponseMessage expectedResponseMessage = new ResponseMessage("Invoice not found.");

    //when
    invoicePdfService.createPdf(invoiceToPdf);
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get(String.format(urlAddressTemplatePdf, invoiceToPdf.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualInvoiceResponse = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualInvoiceResponse);
    assertEquals(expectedResponseMessage, actualInvoiceResponse);
    verify(invoiceService).getInvoice(invoiceToPdf.getId());
    verify(invoicePdfService).createPdf(invoiceToPdf);
  }

  @Test
  void shouldThrowInternalServerErrorWhenTryingToGetPdfAndSomethingGoesWrongOnServer() throws Exception {
    //given
    Invoice invoiceToPdf = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(invoiceToPdf.getId())).thenReturn(Optional.of(invoiceToPdf));
    when(invoicePdfService.createPdf(invoiceToPdf)).thenThrow(new ServiceOperationException());
    ResponseMessage expectedResponseMessage = new ResponseMessage("Internal server error while trying to get PDF of invoice.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get(String.format(urlAddressTemplatePdf, invoiceToPdf.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ResponseMessage actualInvoiceResponse = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualInvoiceResponse);
    assertEquals(expectedResponseMessage, actualInvoiceResponse);
    verify(invoiceService).getInvoice(invoiceToPdf.getId());
    verify(invoicePdfService).createPdf(invoiceToPdf);
  }
}
