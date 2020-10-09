package ua.in.checkbox.api.client.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;
import java.util.List;

@Data
public class ReportModel
{
    private String id;
    private Integer serial;
    @JsonProperty("is_z_report")
    private boolean zReport;
    private List<ReportPaymentsModel> payments;
    private List<ReportTaxesModel> taxes;
    private Integer sell_receipts_count;
    private Integer return_receipts_count;
    private Integer transfers_count;
    private Integer transfers_sum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;
}
