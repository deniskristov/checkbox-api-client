package ua.in.checkbox.api.client.dto.shift;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.in.checkbox.api.client.dto.CashRegisterModel;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftWithCashRegisterModel extends Shift
{
    @JsonProperty("cash_register")
    private CashRegisterModel cashRegister;
}
