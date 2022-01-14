package ua.in.checkbox.api.client.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ua.in.checkbox.api.client.dto.Order;
import ua.in.checkbox.api.client.dto.shift.ShiftFilter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Builder
@Getter
public class ReceiptFilter {
    private String fiscalCode;
    private String serial;
    private Order order;
    private String barcode;
    private String dateFrom;
    private String dateTo;
    private boolean selfReceipts;
    @Builder.Default
    private List<String> shiftIds = new ArrayList<>();
    @Builder.Default
    private List<String> cashRegisterIds = new ArrayList<>();

    public static ReceiptFilter empty()
    {
        return ReceiptFilter.builder().build();
    }

    @Override
    public String toString()
    {
        StringJoiner query = new StringJoiner("&");
        if (order != null)
            query.add(order.toString());

        if(fiscalCode!=null)
            query.add("fiscal_code="+ fiscalCode);
        if(serial!=null)
            query.add("serial="+ serial);
        if(selfReceipts)
            query.add("self_receipts=true");
        if(dateFrom!=null)
            query.add("from_date="+ dateFrom);
        if(dateTo!=null)
            query.add("to_date="+ dateTo);
        shiftIds.forEach(si->query.add("shift_id="+si));
        cashRegisterIds.forEach(cri->query.add("cash_register_id="+cri));

        return query.length()>0?"?"+query:"";
    }
}
