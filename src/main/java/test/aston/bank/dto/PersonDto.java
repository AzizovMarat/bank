package test.aston.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends AccountDto {
    private Double score;

    public PersonDto(String name, String pin) {
        super(name, pin);
        this.score = 0.0;
    }

    public PersonDto(String name, String pin, Double score) {
        super(name, pin);
        this.score = score;
    }
}
