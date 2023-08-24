package test.aston.bank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.aston.bank.dto.*;
import test.aston.bank.entities.Account;
import test.aston.bank.entities.Transaction;
import test.aston.bank.enums.TransactionType;
import test.aston.bank.exceptions.ViolationException;
import test.aston.bank.services.AccountService;
import test.aston.bank.services.DtoToEntity;
import test.aston.bank.services.TransactionService;
import test.aston.bank.validation.ValidationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferMoneyController {

    private final AccountService accountService;
    private final DtoToEntity dtoToEntity;
    private final ValidationService validationService;
    private final TransactionService transactionService;

    @Transactional
    @PostMapping("/deposit")
    public ResponseEntity<AbstractCommonResponse> deposit(@RequestBody DepositPayload payload) {

        try {
            Account account = null;
            if (validationService.isValid(payload.getAccount())) {
                account = accountService.findByNameAndPin(payload.getAccount().getName(), payload.getAccount().getPin());
            }
            if (account == null) {
                throw new ViolationException(new Violation("account", String.format("account with name %s and pin %s not exist", payload.getAccount().getName(), payload.getAccount().getPin())));
            } else {
                if (validationService.isValid(payload.getAmount())) {
                    account.setScore(account.getScore() + payload.getAmount());
                    accountService.update(account);
                    Transaction transaction = new Transaction(account.getId(), payload.getAmount(), TransactionType.DEPOSIT);
                    transactionService.save(transaction);
                    return ResponseEntity.ok(new CommonResponse<>(account));
                }
            }
            return ResponseEntity.ok(new CommonResponse<>(account));
        } catch (ViolationException e) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(e.getViolation().getProperty(), e.getViolation().getMessage()));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse(e));
        }
//        return ResponseEntity.badRequest().body(new CommonResponse<>());
    }

    @Transactional
    @PostMapping("/withdraw")
    public ResponseEntity<AbstractCommonResponse> withdraw(@RequestBody WithdrawPayload payload) {

        try {
            Account account = null;
            if (validationService.isValid(payload.getAccount())) {
                account = accountService.findByNameAndPin(payload.getAccount().getName(), payload.getAccount().getPin());
            }
            if (account == null) {
                throw new ViolationException(new Violation("account", String.format("account with name %s and pin %s not exist", payload.getAccount().getName(), payload.getAccount().getPin())));
            } else {
                if (validationService.isValid(payload.getAmount())) {
                    if (validationService.isMoneyEnough(payload.getAmount(), account)) {
                        account.setScore(account.getScore() - payload.getAmount());
                        accountService.update(account);
                        Transaction transaction = new Transaction(account.getId(), payload.getAmount(), TransactionType.WITHDRAW);
                        transactionService.save(transaction);
                        return ResponseEntity.ok(new CommonResponse<>(account));
                    }
                }
            }
            return ResponseEntity.ok(new CommonResponse<>(account));
        } catch (ViolationException e) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(e.getViolation().getProperty(), e.getViolation().getMessage()));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse(e));
        }
//        return ResponseEntity.badRequest().body(new CommonResponse<>());
    }

    @Transactional
    @PostMapping("/transfer")
    public ResponseEntity<AbstractCommonResponse> transfer(@RequestBody TransferPayload payload) {

        try {
            Account account = null;
            Account accountRecipient = null;
            if (validationService.isValid(payload.getAccount())) {
                account = accountService.findByNameAndPin(payload.getAccount().getName(), payload.getAccount().getPin());
            }
            List<Account> recipientsAccountEntities = accountService.findByName(payload.getRecipient());
            if (recipientsAccountEntities.size() > 1)
                throw new ViolationException(new Violation("recipient", String.format("where are too many recipients with name %s", payload.getRecipient())));
            if (recipientsAccountEntities.size() == 0)
                throw new ViolationException(new Violation("recipient", String.format("where are no recipients with name %s", payload.getRecipient())));

            accountRecipient = recipientsAccountEntities.get(0);
            if (account == null) {
                throw new ViolationException(new Violation("account", String.format("account with name %s and pin %s not exist", payload.getAccount().getName(), payload.getAccount().getPin())));
            } else {
                if (validationService.isValid(payload.getAmount())) {
                    if (validationService.isMoneyEnough(payload.getAmount(), account)) {
                        account.setScore(account.getScore() - payload.getAmount());
                        accountService.update(account);
                        accountRecipient.setScore(accountRecipient.getScore() + payload.getAmount());
                        accountService.update(accountRecipient);
                        Transaction transaction = new Transaction(account.getId(), accountRecipient.getId(), payload.getAmount(), TransactionType.TRANSFER);
                        transactionService.save(transaction);
                        return ResponseEntity.ok(new CommonResponse<>(account));
                    }
                }
            }
            return ResponseEntity.ok(new CommonResponse<>(account));
        } catch (ViolationException e) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(e.getViolation().getProperty(), e.getViolation().getMessage()));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse(e));
        }
//        return ResponseEntity.badRequest().body(new CommonResponse<>());
    }
}
