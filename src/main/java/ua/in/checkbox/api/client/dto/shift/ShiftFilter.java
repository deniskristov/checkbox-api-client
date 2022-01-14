package ua.in.checkbox.api.client.dto.shift;

import lombok.Builder;
import lombok.Singular;
import ua.in.checkbox.api.client.dto.Order;

import java.util.List;

@Builder
public class ShiftFilter
{
    @Singular
    private List<Shift.STATUS> statuses;
    private Order order;

    public static ShiftFilter empty()
    {
        return ShiftFilter.builder().build();
    }

    @Override
    public String toString()
    {
        StringBuilder query = new StringBuilder();
        statuses.forEach(status ->
        {
            if (query.length() > 0)
                query.append("&");
            query
                .append("statuses=")
                .append(status);
        });
        if (order != null)
        {
            String orderStr = order.toString();
            if (!orderStr.isEmpty() && query.length() > 0)
            {
                query.append("&");
            }
            query.append(orderStr);
        }
        if (query.length() > 0)
        {
            query.insert(0,"?");
        }
        return query.toString();
    }
}
