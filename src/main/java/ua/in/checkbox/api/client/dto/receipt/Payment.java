package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    public enum TYPE
    {
        CASHLESS,CASH
    }

    public static final String LABEL_CASH = "Готiвка";
    public static final String LABEL_CARD = "Картка";
}
