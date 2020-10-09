package ua.in.checkbox.api.client.dto.good;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.dto.tax.TaxModel;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;
import java.util.List;

@Data
public class GoodModel
{
    private String id;
    private String code;
    private String barcode;
    private String name;
    private String uktzed;
    private Integer price;
    private List<TaxModel> taxes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;
}
