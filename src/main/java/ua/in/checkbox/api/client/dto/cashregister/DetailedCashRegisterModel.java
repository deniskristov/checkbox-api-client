package ua.in.checkbox.api.client.dto.cashregister;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedCashRegisterModel
{
    private String id;
    @JsonProperty("fiscal_number")
    private String fiscalNumber;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("offline_mode")
    private boolean offlineMode;
    @JsonProperty("stay_offline")
    private boolean stayOffline;
    private String address;
    private boolean active;
}
