package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.good.GoodItemModel;
import ua.in.checkbox.api.client.dto.shift.ShiftWithCashierAndCashRegister;

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
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    private String barcode;
    private String footer;
    @JsonProperty("is_created_offline")
    private boolean isCreatedOffline;
    @JsonProperty("is_sent_dps")
    private boolean sentDps;
    @JsonProperty("sent_dps_at")
    private Date sentDpsAt;
    private ShiftWithCashierAndCashRegister shift;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("tax_url")
    private String taxUrl;
    @JsonProperty("related_receipt_id")
    private String relatedReceiptId;
    @JsonProperty("control_number")
    private String controlNumber;

    public enum TYPE
    {
        SELL,SERVICE_IN,SERVICE_OUT,RETURN,SERVICE_CURRENCY,CURRENCY_EXCHANGE,PAWNSHOP
    }

    public enum STATUS
    {
        CREATED,DONE,ERROR
    }
}
