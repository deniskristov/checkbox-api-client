package ua.in.checkbox.api.client.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
