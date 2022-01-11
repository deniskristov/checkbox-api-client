package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Builder
@Getter
public class DiscountPayload
{
    private TYPE type;
    private MODE mode;
    private Integer value;
    @JsonProperty("tax_code")
    private Integer taxCode;
    @JsonProperty("tax_codes")
    private List<Integer> taxCodes;
    private String name;

    public enum TYPE
    {
        DISCOUNT,EXTRA_CHARGE
    }

    public enum MODE
    {
        PERCENT,VALUE
    }
}
