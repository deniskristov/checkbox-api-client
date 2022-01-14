package ua.in.checkbox.api.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order
{
    public static final int DEFAULT_LIMIT = 25;

    private boolean desc;
    private int limit = DEFAULT_LIMIT;
    private int offset;

    @Builder
    public Order(boolean desc, int limit, int offset)
    {
        this.desc = desc;
        if (offset < 0)
            throw new IllegalStateException("Offset cannot be < 0");
        this.offset = offset;
        if (limit > 0)
        {
            this.limit = limit;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder query = new StringBuilder();
        if (desc)
        {
            query.append("desc=true");
        }
        if (limit != DEFAULT_LIMIT)
        {
            if (query.length() > 0)
            {
                query.append("&");
            }
            query
                .append("limit=")
                .append(limit);
        }
        if (offset > 0)
        {
            if (query.length() > 0)
            {
                query.append("&");
            }
            query
                .append("offset=")
                .append(offset);
        }
        return query.toString();
    }
}
