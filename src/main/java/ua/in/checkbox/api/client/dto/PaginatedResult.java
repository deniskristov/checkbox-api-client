package ua.in.checkbox.api.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResult<T>
{
    private List<T> results;
    private Meta meta;

    @Data
    public static class Meta
    {
        private Integer limit;
        private Integer offset;
    }
}
