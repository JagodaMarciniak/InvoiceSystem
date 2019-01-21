package pl.coderstrust.security;

import java.util.logging.Filter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.coderstrust.controller.InvoiceController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvoiceController.class)
public class AuthenticationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Filter springSecurityFilterChain;

  private WebApplicationContext context;

  private final String urlAddressTemplate = "/invoices/%s";

  @Test
  @WithMockUser(username = "user", password = "pass")
  void test(){

  }
}
