package ua.in.checkbox.api.client.dto.good;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.tax.TaxModel;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodModel
{
    private String id;
    private String code;
    private String barcode;
    private String name;
    private String uktzed;
    private Integer price;
    private List<TaxModel> taxes;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
