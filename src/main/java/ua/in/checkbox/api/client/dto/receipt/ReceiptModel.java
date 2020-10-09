package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.good.GoodItemModel;
import ua.in.checkbox.api.client.dto.good.GoodItemPayload;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptModel
{
    private String id;
    private TYPE type;
    private Integer serial;
    private STATUS status;
    @JsonProperty("total_sum")
    private Integer totalSum;
    @JsonProperty("total_payment")
    private Integer totalPayment;
    @JsonProperty("total_rest")
    private Integer totalRest;
    @JsonProperty("fiscal_code")
    private String fiscalCode;
    @JsonProperty("fiscal_date")
    private Date fiscalDate;
    private List<GoodItemModel> goods;
    private List<Payment> payments;
    @JsonProperty("delivered_at")
    private Date deliveredAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;
    private String barcode;
    private String footer;
    @JsonProperty("is_created_offline")
    private boolean isCreatedOffline;
    @JsonProperty("is_sent_dps")
    private boolean sentDps;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_PATTERN)
    @JsonProperty("sent_dps_at")
    private Date sentDpsAt;

    public enum TYPE
    {
        SELL
    }

    public enum STATUS
    {
        CREATED,DONE
    }
}
