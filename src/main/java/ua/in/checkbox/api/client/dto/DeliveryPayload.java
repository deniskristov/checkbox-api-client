package ua.in.checkbox.api.client.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class DeliveryPayload
{
    private String email;
}
