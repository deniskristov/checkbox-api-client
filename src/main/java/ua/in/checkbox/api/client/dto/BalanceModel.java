package ua.in.checkbox.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceModel
{
    private Integer initial;
    private Integer balance;
    @JsonProperty("cash_sales")
    private Integer cashSales;
    @JsonProperty("card_sales")
    private Integer cardSales;
    @JsonProperty("cash_returns")
    private Integer cashReturns;
    @JsonProperty("card_returns")
    private Integer cardReturns;
    @JsonProperty("service_in")
    private Integer serviceIn;
    @JsonProperty("service_out")
    private Integer serviceOut;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
