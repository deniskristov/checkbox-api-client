package ua.in.checkbox.api.client.dto.shift;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.BalanceModel;
import ua.in.checkbox.api.client.dto.CashRegisterModel;
import ua.in.checkbox.api.client.dto.CashierModel;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shift
{
    private String id;
    private STATUS status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("opened_at")
    private Date openedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("closed_at")
    private Date closedAt;
    @JsonProperty("cash_register")
    private CashRegisterModel cashRegister;
    private BalanceModel balance;
    private CashierModel cashier;

    public enum STATUS
    {
        CREATED, OPENING, OPENED, CLOSING, CLOSED
    }
}
