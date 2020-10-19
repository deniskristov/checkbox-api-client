package ua.in.checkbox.api.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class HTTPValidationError
{
    private List<ValidationError> detail;
    private String message;

    @Data
    public static class ValidationError
    {
        private List<String> loc;
        private String msg;
        private String type;
    }
}
