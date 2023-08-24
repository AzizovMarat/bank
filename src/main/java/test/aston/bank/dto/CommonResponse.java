package test.aston.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonResponse<R> extends AbstractCommonResponse {

    @JsonProperty("result")
    private R result;

    public CommonResponse(R result) {
        setSuccess(true);
        this.result = result;
    }
}
