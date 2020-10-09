package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    public enum TYPE
    {
        CARD,CASH
    }

    public static final String LABEL_CASH = "Готiвка";
    public static final String LABEL_CARD = "Картка";
}
