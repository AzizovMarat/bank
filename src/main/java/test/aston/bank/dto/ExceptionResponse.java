package test.aston.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse extends AbstractCommonResponse {
    @JsonProperty("type")
    private String type;
    @JsonProperty("errorMessage")
    private String errorMessage;

    public ExceptionResponse(Exception ex) {
        setSuccess(false);
        type = ex.getClass().getName();
        errorMessage = ex.getMessage();
    }

    public ExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
