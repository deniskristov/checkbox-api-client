package ua.in.checkbox.api.client.dto.shift;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.in.checkbox.api.client.dto.cashier.CashierModel;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftWithCashierModel extends Shift
{
    private CashierModel cashier;
}
