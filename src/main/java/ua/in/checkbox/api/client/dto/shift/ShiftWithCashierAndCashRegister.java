package ua.in.checkbox.api.client.dto.shift;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.BalanceModel;
import ua.in.checkbox.api.client.dto.CashRegisterModel;
import ua.in.checkbox.api.client.dto.CashierModel;
import ua.in.checkbox.api.client.dto.report.ReportModel;
import ua.in.checkbox.api.client.dto.tax.ShiftTaxModel;
import ua.in.checkbox.api.client.dto.tax.TaxModel;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftWithCashierAndCashRegister
{
    private String id;
    private Integer serial;
    private Shift.STATUS status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("opened_at")
    private Date openedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("closed_at")
    private Date closedAt;
    @JsonProperty("z_report")
    private ReportModel zReport;
    private BalanceModel balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;
    private List<ShiftTaxModel> taxes;
    @JsonProperty("cash_register")
    private CashRegisterModel cashRegister;
    private CashierModel cashier;
}
