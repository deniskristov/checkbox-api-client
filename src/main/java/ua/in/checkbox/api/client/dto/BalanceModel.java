package ua.in.checkbox.api.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.utils.AppConstants;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;
}
