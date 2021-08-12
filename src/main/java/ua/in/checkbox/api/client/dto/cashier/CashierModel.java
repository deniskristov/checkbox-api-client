package ua.in.checkbox.api.client.dto.cashier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;

    public enum SIGNATURE_TYPE
    {
        AGENT,CLOUD_SIGNATURE
    }
}
