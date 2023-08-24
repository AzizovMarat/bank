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
import test.aston.bank.repositories.AccountRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenAccountPayload_whenAdd_thenStatus200andAccountReturned() throws Exception {
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
}