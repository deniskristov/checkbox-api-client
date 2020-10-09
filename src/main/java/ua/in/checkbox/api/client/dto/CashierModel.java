package ua.in.checkbox.api.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.in.checkbox.api.client.utils.AppConstants;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashierModel
{
    private String id;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("key_id")
    private String keyId;
    @JsonProperty("signature_type")
    private SIGNATURE_TYPE signatureType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_NO_MILLIS_PATTERN)
    @JsonProperty("updated_at")
    private Date updatedAt;

    public enum SIGNATURE_TYPE
    {
        AGENT
    }
}
