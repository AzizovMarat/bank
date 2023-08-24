package test.aston.bank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import test.aston.bank.dto.*;
import test.aston.bank.entities.Account;
import test.aston.bank.entities.Transaction;
import test.aston.bank.enums.TransactionType;
import test.aston.bank.exceptions.ViolationException;
import test.aston.bank.repositories.AccountRepository;
import test.aston.bank.services.AccountService;
import test.aston.bank.services.DtoToEntity;
import test.aston.bank.services.TransactionService;
import test.aston.bank.validation.ValidationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final AccountService accountService;
    private final DtoToEntity dtoToEntity;
    private final ValidationService validationService;
    private final TransactionService transactionService;

    @GetMapping("/all")
    public ResponseEntity<AbstractCommonResponse> all() {

        Set<Account> accounts = accountService.findAll();

        return ResponseEntity.badRequest().body(new CommonResponse<>(accounts));
    }

    @PostMapping("/person")
    public ResponseEntity<AbstractCommonResponse> person(@RequestBody AccountPayload payload) {

        AccountDto accountDto = payload.getAccount();
        if (accountDto != null) {
            try {
                if (validationService.isValid(payload.getAccount())) {
                    Account account = accountService.findByNameAndPin(payload.getAccount().getName(), payload.getAccount().getPin());
                    if (account == null) {
                        throw new ViolationException(new Violation("account", String.format("account with name %s and pin %s not exist", payload.getAccount().getName(), payload.getAccount().getPin())));
                    } else {
                        List<Transaction> accountTransactions = transactionService.findByAccountId(account.getId());
                        List<Transaction> recipientTransactions = transactionService.findByRecipientId(account.getId());
                        List<Transaction> transactions = new ArrayList<>(accountTransactions);
                        transactions.addAll(recipientTransactions);
                        return ResponseEntity.ok(new CommonResponse<>(transactions));
                    }
                }
            } catch (ViolationException e) {
                return ResponseEntity.badRequest().body(new ExceptionResponse(e.getViolation().getProperty(), e.getViolation().getMessage()));
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                return ResponseEntity.badRequest().body(new ExceptionResponse(e));
            }
        }
        return ResponseEntity.badRequest().body(new ExceptionResponse("request body is empty"));
    }
}
