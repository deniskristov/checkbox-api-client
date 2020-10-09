package ua.in.checkbox.api.client.dto.good;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Good
{
    private String code;
    private String barcode;
    private String name;
    private String header;
    private String footer;
    private String uktzed;
    // Копiйки
    private Integer price;
}
