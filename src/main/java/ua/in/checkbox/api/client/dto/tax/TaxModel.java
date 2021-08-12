package ua.in.checkbox.api.client.dto.tax;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxModel
{
    private String id;
    private Integer code;
    private String label;
    private String symbol;
    private Integer rate;
    @JsonProperty("extra_rate")
    private Integer extraRate;
    private boolean included;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
