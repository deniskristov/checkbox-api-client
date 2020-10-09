package ua.in.checkbox.api.client.utils;

import lombok.Builder;
import lombok.Getter;
import ua.in.checkbox.api.client.dto.ErrorDetails;

@Builder
@Getter
public class CheckboxApiCallException extends RuntimeException
{
    private static final int UNKNOWN = -1;

    @Builder.Default
    private int httpCode = UNKNOWN;
    private ErrorDetails error;

    public boolean isUnknownReason()
    {
        return httpCode == UNKNOWN;
    }
}
