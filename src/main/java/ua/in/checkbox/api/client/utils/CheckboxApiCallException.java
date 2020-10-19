package ua.in.checkbox.api.client.utils;

import lombok.Builder;
import lombok.Getter;
import ua.in.checkbox.api.client.dto.ErrorDetails;
import ua.in.checkbox.api.client.dto.HTTPValidationError;

@Builder
@Getter
public class CheckboxApiCallException extends RuntimeException
{
    private static final int UNKNOWN = -1;

    @Builder.Default
    private int httpCode = UNKNOWN;
    private ErrorDetails error;
    private HTTPValidationError validationError;

    public boolean isUnknownReason()
    {
        return httpCode == UNKNOWN;
    }
}
