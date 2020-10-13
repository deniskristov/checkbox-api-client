package ua.in.checkbox.api.client.dto.cashier;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationModel
{
    private String id;
    private String title;
    private String edrpou;
    @JsonProperty("tax_number")
    private String taxNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;
}
