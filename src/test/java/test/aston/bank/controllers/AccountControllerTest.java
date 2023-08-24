package test.aston.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import test.aston.bank.dto.AccountDto;
import test.aston.bank.dto.AccountPayload;
import test.aston.bank.dto.PersonDto;
import test.aston.bank.dto.PersonPayload;
import test.aston.bank.repositories.AccountRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository repository;
    @Autowired
    private MockMvc mockMvc;

//    @AfterEach
//    public void resetDb() {
//        repository.deleteAll();
//    }

    @Test
    public void givenAccountDto_whenAdd_thenStatus200andAccountReturned() throws Exception {
        AccountDto account = new AccountDto("Michail", "0000");
        mockMvc.perform(
                        post("/account/create")
                                .content(objectMapper.writeValueAsString(new AccountPayload(account)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").isNumber())
                .andExpect(jsonPath("$.result.name").value("Michail"))
                .andExpect(jsonPath("$.result.pin").value("0000"));
    }

    @Test
    public void givenAccountDto_thenStatus400andErrorPinMoreThan4Returned() throws Exception {
        AccountDto account = new AccountDto("Michail", "10000");
        mockMvc.perform(
                        post("/account/create")
                                .content(objectMapper.writeValueAsString(new AccountPayload(account)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("pin"))
                .andExpect(jsonPath("$.errorMessage").value("length must be equal 4"));
    }

    @Test
    public void givenAccountDto_henStatus400andErrorPinNotDigitsReturned() throws Exception {
        AccountDto account = new AccountDto("Michail", "sasd");
        mockMvc.perform(
                        post("/account/create")
                                .content(objectMapper.writeValueAsString(new AccountPayload(account)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("pin"))
                .andExpect(jsonPath("$.errorMessage").value("must be digits"));
    }

    @Test
    public void givenAccountDto_thenStatus400andErrorEmptyNameReturned() throws Exception {
        AccountDto account = new AccountDto("", "0000");
        mockMvc.perform(
                        post("/account/create")
                                .content(objectMapper.writeValueAsString(new AccountPayload(account)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("name"))
                .andExpect(jsonPath("$.errorMessage").value("field.required"));
    }

    @Test
    public void givenAccountDto_thenStatus400andErrorEmptyPinReturned() throws Exception {
        AccountDto account = new AccountDto("Michail", "");
        mockMvc.perform(
                        post("/account/create")
                                .content(objectMapper.writeValueAsString(new AccountPayload(account)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("pin"))
                .andExpect(jsonPath("$.errorMessage").value("field.required"));
    }

    @Test
    public void givenAccountDto_thenStatus400andErrorNameMoreThan255Returned() throws Exception {
        AccountDto account = new AccountDto("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", "0000");
        mockMvc.perform(
                        post("/account/create")
                                .content(objectMapper.writeValueAsString(new AccountPayload(account)))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("name"))
                .andExpect(jsonPath("$.errorMessage").value("length must be < 255"));
    }

//    private Person createTestPerson(String name) {
//        Person emp = new Person(name);
//        return repository.save(emp);
//    }
}
