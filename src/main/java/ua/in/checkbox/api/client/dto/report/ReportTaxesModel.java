package ua.in.checkbox.api.client.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonProperty("created_at")
    private Date createdAt;
    private Integer value;
    @JsonProperty("extra_value")
    private Integer extraValue;
}
