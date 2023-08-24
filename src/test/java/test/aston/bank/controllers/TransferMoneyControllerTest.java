package test.aston.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import test.aston.bank.dto.*;
import test.aston.bank.entities.Account;
import test.aston.bank.repositories.AccountRepository;
import test.aston.bank.services.DtoToEntity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferMoneyControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DtoToEntity dtoToEntity;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    // deposittest
    @Test
    public void givenDepositPayload_whenAdd_thenStatus200andAccountReturned() throws Exception {
        AccountDto michail = createTestAccount("Michail", "0000", 49.45);
        DepositPayload depositPayload = new DepositPayload();
        depositPayload.setAccount(michail);
        depositPayload.setAmount(1250.55);
        mockMvc.perform(
                        post("/transfer/deposit")
                                .content(objectMapper.writeValueAsString(depositPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").isNumber())
                .andExpect(jsonPath("$.result.name").value("Michail"))
                .andExpect(jsonPath("$.result.pin").value("0000"))
                .andExpect(jsonPath("$.result.score").value("1300.0"));
    }

    @Test
    public void givenDepositPayload_whenCheckAccount_thenStatus400andErrorNotExistReturned() throws Exception {
        DepositPayload depositPayload = new DepositPayload();
        depositPayload.setAccount(new AccountDto("Michail", "0000"));
        depositPayload.setAmount(1250.55);
        mockMvc.perform(
                        post("/transfer/deposit")
                                .content(objectMapper.writeValueAsString(depositPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("account"))
                .andExpect(jsonPath("$.errorMessage").value("account with name Michail and pin 0000 not exist"));
    }

    @Test
    public void givenDepositPayload_whenCheckAmount_thenStatus400andErrorNegativeAmountReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", null);
        DepositPayload depositPayload = new DepositPayload();
        depositPayload.setAccount(account);
        depositPayload.setAmount(-1250.55);
        mockMvc.perform(
                        post("/transfer/deposit")
                                .content(objectMapper.writeValueAsString(depositPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("amount"))
                .andExpect(jsonPath("$.errorMessage").value("amount must be > 0"));
    }

    // withdrawtest
    @Test
    public void givenWithdrawPayload_whenSubtract_thenStatus200andAccountReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", 100.00);
        WithdrawPayload withdrawPayload = new WithdrawPayload();
        withdrawPayload.setAccount(account);
        withdrawPayload.setAmount(50.55);
        mockMvc.perform(
                        post("/transfer/withdraw")
                                .content(objectMapper.writeValueAsString(withdrawPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").isNumber())
                .andExpect(jsonPath("$.result.name").value("Michail"))
                .andExpect(jsonPath("$.result.pin").value("0000"))
                .andExpect(jsonPath("$.result.score").value("49.45"));
    }

    @Test
    public void givenWithdrawPayload_whenCheckIsMoneyEnough_thenStatus400andErrorNotEnoughMoneyReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", 100.00);
        WithdrawPayload withdrawPayload = new WithdrawPayload();
        withdrawPayload.setAccount(account);
        withdrawPayload.setAmount(101.55);
        mockMvc.perform(
                        post("/transfer/withdraw")
                                .content(objectMapper.writeValueAsString(withdrawPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("amount"))
                .andExpect(jsonPath("$.errorMessage").value("amount must be > score"));
    }

    @Test
    public void givenWithdrawPayload_whenCheckAccount_thenStatus400andErrorNotExistReturned() throws Exception {
        WithdrawPayload withdrawPayload = new WithdrawPayload();
        withdrawPayload.setAccount(new AccountDto("Michail", "0000"));
        withdrawPayload.setAmount(1250.55);
        mockMvc.perform(
                        post("/transfer/deposit")
                                .content(objectMapper.writeValueAsString(withdrawPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("account"))
                .andExpect(jsonPath("$.errorMessage").value("account with name Michail and pin 0000 not exist"));
    }

    // transfertest
    @Test
    public void givenTransferPayload_whenTransfer_thenStatus200andAccountReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", 100.00);
        AccountDto accountRecipient = createTestAccount("Alex", "0001", 0.00);
        TransferPayload transferPayload = new TransferPayload();
        transferPayload.setAccount(account);
        transferPayload.setAmount(50.55);
        transferPayload.setRecipient("Alex");
        mockMvc.perform(
                        post("/transfer/transfer")
                                .content(objectMapper.writeValueAsString(transferPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").isNumber())
                .andExpect(jsonPath("$.result.name").value("Michail"))
                .andExpect(jsonPath("$.result.pin").value("0000"))
                .andExpect(jsonPath("$.result.score").value("49.45"));
    }

    @Test
    public void givenTransferPayload_whenCheckRecipient_thenStatus400andErrorRecipientNotExistReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", 100.00);
        TransferPayload transferPayload = new TransferPayload();
        transferPayload.setAccount(account);
        transferPayload.setAmount(50.55);
        transferPayload.setRecipient("Alex");
        mockMvc.perform(
                        post("/transfer/transfer")
                                .content(objectMapper.writeValueAsString(transferPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("recipient"))
                .andExpect(jsonPath("$.errorMessage").value("where are no recipients with name Alex"));
    }

    @Test
    public void givenTransferPayload_whenCheckRecipient_thenStatus400andErrorTooManyRecipientsReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", 100.00);
        AccountDto accountRecipient1 = createTestAccount("Alex", "0001", 1.00);
        AccountDto accountRecipient2 = createTestAccount("Alex", "0002", 2.00);
        TransferPayload transferPayload = new TransferPayload();
        transferPayload.setAccount(account);
        transferPayload.setAmount(50.55);
        transferPayload.setRecipient("Alex");
        mockMvc.perform(
                        post("/transfer/transfer")
                                .content(objectMapper.writeValueAsString(transferPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("recipient"))
                .andExpect(jsonPath("$.errorMessage").value("where are too many recipients with name Alex"));
    }

    @Test
    public void givenTransferPayload_whenCheckScore_thenStatus400andErrorNotEnoughMoneyReturned() throws Exception {
        AccountDto account = createTestAccount("Michail", "0000", 10.00);
        AccountDto accountRecipient1 = createTestAccount("Alex", "0001", 1.00);
        TransferPayload transferPayload = new TransferPayload();
        transferPayload.setAccount(account);
        transferPayload.setAmount(50.55);
        transferPayload.setRecipient("Alex");
        mockMvc.perform(
                        post("/transfer/transfer")
                                .content(objectMapper.writeValueAsString(transferPayload))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.type").value("amount"))
                .andExpect(jsonPath("$.errorMessage").value("amount must be > score"));
    }

    private AccountDto createTestAccount(String name, String pin, Double score) {
        Account account = repository.save(new Account(name, pin, score == null ? 0.0 : score));
        return dtoToEntity.entityAccountToPersonDto(account);
    }
}
