package test.aston.bank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.aston.bank.dto.*;
import test.aston.bank.entities.Account;
import test.aston.bank.exceptions.ViolationException;
import test.aston.bank.services.AccountService;
import test.aston.bank.services.DtoToEntity;
import test.aston.bank.validation.ValidationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final DtoToEntity dtoToEntity;
    private final ValidationService validationService;

//    @PostMapping("/account")
//    public ResponseEntity<AbstractCommonResponse> createAccount(@RequestBody PersonDto accountDto) {
//
//        try {
//            Account account = dtoToEntity.dtoAccountToEntityAccount(accountDto);
//            account = accountService.update(account);
//            return ResponseEntity.ok(new CommonResponse<>(account));
//        } catch (Exception e) {
//            System.out.println(e.getLocalizedMessage());
//            return new ResponseEntity<>(new ExceptionResponse(e), HttpStatus.BAD_REQUEST);
//        }
//
//    }

    @PostMapping("/create")
    public ResponseEntity<AbstractCommonResponse> createAccount(@RequestBody AccountPayload accountPayload) {
        AccountDto accountDto = accountPayload.getAccount();
        if (accountDto != null) {
            try {
                if (validationService.isValid(accountDto)) {
                    Account account = dtoToEntity.dtoAccountToEntityAccount(accountDto);
                    account = accountService.update(account);
                    return ResponseEntity.ok(new CommonResponse<>(account));
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
