package ua.in.checkbox.api.client.dto.cashier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatureTestResult
{
    boolean online;
}
