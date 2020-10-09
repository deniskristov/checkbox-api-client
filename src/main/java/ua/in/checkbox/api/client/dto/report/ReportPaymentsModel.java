package ua.in.checkbox.api.client.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.receipt.Payment;

@Data
public class ReportPaymentsModel
{
    private String id;
    private Payment.TYPE type;
    private String label;
    @JsonProperty("sell_sum")
    private Integer sellSum;
    @JsonProperty("return_sum")
    private Integer returnSum;
    @JsonProperty("service_in")
    private Integer serviceIn;
    @JsonProperty("service_out")
    private Integer serviceOut;
}
