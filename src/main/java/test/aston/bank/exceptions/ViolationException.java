package test.aston.bank.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import test.aston.bank.dto.Violation;

@Getter
@Setter
@RequiredArgsConstructor
public class ViolationException extends Exception {

//    private List<Violation> violations;

    private final Violation violation;
}
