package ua.in.checkbox.api.client.dto.good;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodItemModel
{
    private Good good;
    @JsonProperty("good_id")
    private String goodId;
    private Integer sum;
    private Integer quantity;
    @JsonProperty("is_return")
    private boolean isReturn;

}
