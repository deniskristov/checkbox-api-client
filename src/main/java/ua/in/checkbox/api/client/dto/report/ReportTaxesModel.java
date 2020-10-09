package ua.in.checkbox.api.client.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;

@Data
public class ReportTaxesModel
{
    private String id;
    private Integer code;
    private String label;
    private String symbol;
    // TODO number
    private String rate;
    private Integer sell_sum;
    private Integer return_sum;
    private Integer sales_turnover;
    private Integer returns_turnover;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
}
