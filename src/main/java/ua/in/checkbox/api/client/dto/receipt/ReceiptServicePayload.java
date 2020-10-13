package ua.in.checkbox.api.client.dto.receipt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReceiptServicePayload
{
    private Payment payment;
}
