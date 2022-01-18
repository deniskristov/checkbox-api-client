package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment
{
    private TYPE type;
    // Копiйки
    private Integer value;
    // Картка,готiвка
    private String label;
    @JsonProperty("card_mask")
    private String cardMask;
    // Номер оплати
    private Integer code;
    @JsonProperty("pawnshop_is_return")
    private Boolean pawnshopIsReturn;
    @JsonProperty("payment_system")
    private String paymentSystem;
    @JsonProperty("receipt_no")
    private String receiptNo;

    public enum TYPE
    {
        CASHLESS,CASH,CARD
    }

    public static final String LABEL_CASH = "Готiвка";
    public static final String LABEL_CARD = "Картка";
}
