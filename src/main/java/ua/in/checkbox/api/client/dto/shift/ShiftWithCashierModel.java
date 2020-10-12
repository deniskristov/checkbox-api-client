package ua.in.checkbox.api.client.dto.shift;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ua.in.checkbox.api.client.dto.CashierModel;

@Data
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftWithCashierModel extends Shift
{
    private CashierModel cashier;
}
