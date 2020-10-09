package ua.in.checkbox.api.client.dto.good;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class GoodItemPayload
{
    private Good good;
    @JsonProperty("good_id")
    private String goodId;
    // 1 шт = 1000
    private Integer quantity;
    @JsonProperty("is_return")
    private boolean isReturn;
}
