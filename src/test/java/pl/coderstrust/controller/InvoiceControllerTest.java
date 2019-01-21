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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.coderstrust.configuration.ApplicationConfiguration;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoicePdfService;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class InvoiceControllerTest {

  private ObjectMapper mapper = new ApplicationConfiguration().getObjectMapper();
  private final String urlAddressTemplate = "/invoices/%s";
  private final String urlAddressTemplatePdf = "/invoices/pdf/%s";

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InvoiceService invoiceService;

  @MockBean
  private InvoicePdfService invoicePdfService;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Test
  @WithMockUser()
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
  @WithMockUser()
  void shouldReturnInternalServerErrorDuringGettingAllInvoicesWhenSomethingWentWrongOnServer() throws Exception {
    //given
    when(invoiceService.getAllInvoices()).thenThrow(new ServiceOperationException());
    ErrorMessage expectedErrorMessage = new ErrorMessage("Internal server error while getting invoices.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, ""))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).getAllInvoices();
  }

  @Test
  @WithMockUser()
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
  @WithMockUser()
  void shouldReturnNotFoundStatusWhenInvoiceNotExisting() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenReturn(Optional.ofNullable(null));
    ErrorMessage expectedErrorMessage = new ErrorMessage("Invoice not found for passed id.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  @WithMockUser()
  void shouldReturnInternalServiceErrorDuringGettingSpecificInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenThrow(new ServiceOperationException());
    ErrorMessage expectedErrorMessage = new ErrorMessage(String.format("Internal server error while getting invoice by id: %s",
        expectedInvoice.getId()));

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .get(String.format(urlAddressTemplate, expectedInvoice.getId()))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  @WithMockUser()
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
  @WithMockUser()
  void shouldReturnInternalServiceErrorDuringAddingInvoiceWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    invoiceToAdd.setId(null);
    when(invoiceService.addInvoice(invoiceToAdd)).thenThrow(new ServiceOperationException());
    ErrorMessage expectedErrorMessage = new ErrorMessage("Internal server error while saving specified invoice.");

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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).addInvoice(invoiceToAdd);
  }

  @Test
  @WithMockUser()
  void shouldReturnBadRequestDuringAddingInvoiceWhenInvoiceContainsId() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    ErrorMessage expectedErrorMessage = new ErrorMessage("Passed invoice is invalid.",
        Collections.singletonList("Id must be null"));

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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService, never()).addInvoice(invoiceToAdd);
  }

  @Test
  void shouldReturnBadRequestDuringAddingInvoiceWithInvalidData() throws Exception {
    //given
    Invoice invoiceToAdd = InvoiceGenerator.getRandomInvoice();
    invoiceToAdd.setId(null);
    invoiceToAdd.getBuyer().getContactDetails().setEmail("wrong email");
    invoiceToAdd.getSeller().getContactDetails().setPhoneNumber("+1abc543");
    List<String> expectedDetailsOfValidation = new ArrayList<>();
    expectedDetailsOfValidation.add("Email is not valid");
    expectedDetailsOfValidation.add("Phone number can only be numbers");

    ErrorMessage expectedErrorMessage = new ErrorMessage("Passed invoice is invalid.",
        expectedDetailsOfValidation);

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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService, never()).addInvoice(invoiceToAdd);
  }

  @Test
  @WithMockUser()
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
  @WithMockUser()
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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertNotEquals(expectedInvoice.getId(), wrongInvoiceId);
    ErrorMessage expectedErrorMessage = new ErrorMessage("Passed data is invalid. Please verify invoice id.");
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService, never()).updateInvoice(expectedInvoice);
  }

  @Test
  @WithMockUser()
  void shouldReturnNotFoundDuringUpdatingNonExistingInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(expectedInvoice.getId())).thenReturn(false);
    ErrorMessage expectedErrorMessage = new ErrorMessage("Invoice not found.");

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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).invoiceExists(expectedInvoice.getId());
  }

  @Test
  void shouldReturnBadRequestDuringUpdatingInvalidInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(expectedInvoice.getId())).thenReturn(true);
    expectedInvoice.getBuyer().getContactDetails().setEmail("wrong email");
    expectedInvoice.getSeller().getContactDetails().setPhoneNumber("+1abc543");
    List<String> expectedDetailsOfValidation = new ArrayList<>();
    expectedDetailsOfValidation.add("Email is not valid");
    expectedDetailsOfValidation.add("Phone number can only be numbers");

    ErrorMessage expectedErrorMessage = new ErrorMessage("Passed invoice is invalid.",
        expectedDetailsOfValidation);

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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService, never()).updateInvoice(expectedInvoice);
  }

  @Test
  @WithMockUser()
  void shouldThrowInternalServerErrorDuringUpdatingWhenSomethingWentWrongWithServer() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.invoiceExists(expectedInvoice.getId())).thenThrow(new ServiceOperationException());
    ErrorMessage expectedErrorMessage = new ErrorMessage("Internal server error while updating specified invoice.");

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
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).invoiceExists(expectedInvoice.getId());
  }

  @Test
  @WithMockUser()
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
  @WithMockUser()
  void shouldReturnNotFoundDuringDeletingNonExistingInvoice() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenReturn(Optional.ofNullable(null));
    ErrorMessage expectedErrorMessage = new ErrorMessage("Invoice not found.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders
            .delete(String.format(urlAddressTemplate, expectedInvoice.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualErrorMessage = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualErrorMessage);
    assertEquals(expectedErrorMessage, actualErrorMessage);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  @WithMockUser()
  void shouldThrowInternalServerErrorDuringDeletingWhenSomethingWentWrongOnServer() throws Exception {
    //given
    Invoice expectedInvoice = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(expectedInvoice.getId())).thenThrow(new ServiceOperationException());
    ErrorMessage expectedErrorMessage = new ErrorMessage("Internal server error while deleting specified invoice.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.delete(String.format(urlAddressTemplate, expectedInvoice.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualInvoiceResponse = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualInvoiceResponse);
    assertEquals(expectedErrorMessage, actualInvoiceResponse);
    verify(invoiceService).getInvoice(expectedInvoice.getId());
  }

  @Test
  @WithMockUser()
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
  @WithMockUser()
  void shouldThrowNotFoundExceptionWhenTryingToGetPdfWithInvalidInvoiceId() throws Exception {
    //given
    Invoice invoiceToPdf = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(invoiceToPdf.getId())).thenReturn(Optional.ofNullable(null));
    final ErrorMessage expectedErrorMessage = new ErrorMessage("Invoice not found.");

    //when
    invoicePdfService.createPdf(invoiceToPdf);
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get(String.format(urlAddressTemplatePdf, invoiceToPdf.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualInvoiceResponse = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
    assertNotNull(actualInvoiceResponse);
    assertEquals(expectedErrorMessage, actualInvoiceResponse);
    verify(invoiceService).getInvoice(invoiceToPdf.getId());
    verify(invoicePdfService).createPdf(invoiceToPdf);
  }

  @Test
  @WithMockUser()
  void shouldThrowInternalServerErrorWhenTryingToGetPdfAndSomethingGoesWrongOnServer() throws Exception {
    //given
    Invoice invoiceToPdf = InvoiceGenerator.getRandomInvoice();
    when(invoiceService.getInvoice(invoiceToPdf.getId())).thenReturn(Optional.of(invoiceToPdf));
    when(invoicePdfService.createPdf(invoiceToPdf)).thenThrow(new ServiceOperationException());
    ErrorMessage expectedErrorMessage = new ErrorMessage("Internal server error while trying to get PDF of invoice.");

    //when
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get(String.format(urlAddressTemplatePdf, invoiceToPdf.getId())))
        .andReturn();

    int httpStatus = result.getResponse().getStatus();
    ErrorMessage actualInvoiceResponse = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
    assertNotNull(actualInvoiceResponse);
    assertEquals(expectedErrorMessage, actualInvoiceResponse);
    verify(invoiceService).getInvoice(invoiceToPdf.getId());
    verify(invoicePdfService).createPdf(invoiceToPdf);
  }
}
