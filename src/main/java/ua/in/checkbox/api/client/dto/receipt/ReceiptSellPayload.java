package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ua.in.checkbox.api.client.dto.DeliveryPayload;
import ua.in.checkbox.api.client.dto.good.GoodItemPayload;

import java.util.ArrayList;
import java.util.List;

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
}
