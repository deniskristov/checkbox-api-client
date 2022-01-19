package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ua.in.checkbox.api.client.dto.DeliveryPayload;
import ua.in.checkbox.api.client.dto.good.GoodItemPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Builder
@Getter
public class ReceiptSellPayload
{
    @JsonProperty("cashier_name")
    private String cashierName;
    private String departament;
    private List<GoodItemPayload> goods;
    private DeliveryPayload delivery;
    private List<Payment> payments;
    private String header;
    private String footer;
    private String barcode;
    @Builder.Default
    private List<DiscountPayload> discounts = new ArrayList<>();
    @JsonProperty("is_pawnshop")
    private boolean pawnshop;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("technical_return")
    private boolean technicalReturn;
    @Builder.Default
    private Map<String,String> context = new HashMap();
    private boolean rounding;
    @JsonProperty("related_receipt_id")
    private String relatedReceiptId;
    @JsonProperty("previous_receipt_id")
    private String previousReceiptId;

    public static class ReceiptSellPayloadBuilder
    {
        public ReceiptSellPayloadBuilder forPawnshop()
        {
            this.pawnshop = true;
            return this;
        }
    }
}
