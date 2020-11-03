package ua.in.checkbox.api.client.utils;

import lombok.Builder;
import lombok.Getter;
import ua.in.checkbox.api.client.dto.ErrorDetails;
import ua.in.checkbox.api.client.dto.HTTPValidationError;

@Getter
public class CheckboxApiCallException extends RuntimeException
{
    private Integer httpCode;
    private ErrorDetails error;
    private HTTPValidationError validationError;

    @Builder
    public CheckboxApiCallException(Integer httpCode, ErrorDetails error, HTTPValidationError validationError)
    {
        super(toStringImpl(error, validationError));
        this.httpCode = httpCode;
        this.error = error;
        this.validationError = validationError;
    }

    public boolean isUnknownReason()
    {
        return httpCode == null;
    }

    @Override
    public String toString()
    {
        return toStringImpl(error, validationError);
    }

    private static String toStringImpl(ErrorDetails error, HTTPValidationError validationError)
    {
        if (error != null)
        {
            return error.toString();
        }
        else if (validationError != null)
        {
            return validationError.toString();
        }
        else
        {
            return "unknown error";
        }
    }
}
