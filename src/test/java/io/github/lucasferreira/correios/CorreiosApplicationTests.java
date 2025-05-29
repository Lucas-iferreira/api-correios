package io.github.lucasferreira.correios;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.lucasferreira.correios.model.Address;
import io.github.lucasferreira.correios.service.CorreiosService;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.springtest.MockServerTest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@MockServerTest({"correios.base.url=http://localhost:${mockServerPort}/ceps.csv"})
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"setup.on.startup=false"})
class CorreiosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private MockServerClient mockServer;

    @Autowired
    private CorreiosService service;

    @Test
    @Order(1)
    public void testGetZipcodeWhenReady() throws Exception {
        mockMvc.perform(get("/zipcode/03358150")).andExpect(status().isServiceUnavailable());
    }

    @Test
    @Order(3)
    public void testSetup() throws Exception {
        String bodyString = "SP,Sao Paulo,Vila Formosa,3358150,Rua Ituri,,,,,,,,,,";

        mockServer.when(request()
                        .withPath("/ceps.csv")
                        .withMethod("GET"))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(bodyString));

        service.setUp();
    }

    @Test
    @Order(2)
    public void testSetupNotOk() throws Exception {

        mockServer.when(request()
                        .withPath("/ceps.csv")
                        .withMethod("GET"))
                .respond(response()
                        .withStatusCode(500)
                        .withBody("ERROR"));

        assertThrows(Exception.class, () -> service.setUp());

    }

    @Test
    @Order(4)
    public void testGetZipcodethatDoesntExist() throws Exception {
        mockMvc.perform(get("/zipcode/99999999")).andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    public void TestGetZipcodeOk() throws Exception {
        MvcResult result = mockMvc.perform(get("/zipcode/03358150")).andExpect(status().isOk()).andReturn();
        String resultString = result.getResponse().getContentAsString();

        String addressToCompareStr = new ObjectMapper().writeValueAsString(
                Address.builder()
                        .city("Sao Paulo")
                        .street("Rua Ituri")
                        .district("Vila Formosa")
                        .state("SP")
                        .zipcode("03358150")
                        .build());

        JSONAssert.assertEquals(addressToCompareStr, resultString, false);
    }
}
