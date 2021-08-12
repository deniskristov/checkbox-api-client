package ua.in.checkbox.api.client.dto.shift;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.BalanceModel;
import ua.in.checkbox.api.client.dto.report.ReportModel;
import ua.in.checkbox.api.client.dto.tax.ShiftTaxModel;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shift
{
    private String id;
    private Integer serial;
    private STATUS status;
    @JsonProperty("z_report")
    private ReportModel zReport;
    @JsonProperty("opened_at")
    private Date openedAt;
    @JsonProperty("closed_at")
    private Date closedAt;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    private List<ShiftTaxModel> taxes;
    private BalanceModel balance;

    public enum STATUS
    {
        CREATED, OPENING, OPENED, CLOSING, CLOSED
    }
}
