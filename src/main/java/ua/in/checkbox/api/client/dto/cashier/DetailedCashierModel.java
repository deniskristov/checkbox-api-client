package ua.in.checkbox.api.client.dto.cashier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedCashierModel extends CashierModel
{
    private OrganizationModel organization;
}
