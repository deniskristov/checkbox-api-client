package ua.in.checkbox.api.client.dto.tax;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShiftTaxModel extends TaxModel
{
    private Integer sales;
    private Integer returns;
    @JsonProperty("sales_turnover")
    private Integer salesTurnover;
    @JsonProperty("returns_turnover")
    private Integer returnsTurnover;
}
