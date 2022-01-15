import org.junit.Assert;
import org.junit.Test;
import ua.in.checkbox.api.client.dto.Order;
import ua.in.checkbox.api.client.dto.receipt.Payment;
import ua.in.checkbox.api.client.dto.receipt.ReceiptFilter;
import ua.in.checkbox.api.client.dto.receipt.ReceiptSellPayload;
import ua.in.checkbox.api.client.dto.shift.Shift;
import ua.in.checkbox.api.client.dto.shift.ShiftFilter;

import java.util.Arrays;

public class DtoDefaultValuesTest
{
    @Test
    public void receiptSellPayloadTest()
    {
        ReceiptSellPayload receiptSellPayload = ReceiptSellPayload.builder().build();
        Assert.assertTrue(receiptSellPayload.getDiscounts().size() == 0);
        Assert.assertTrue(receiptSellPayload.getContext().size() == 0);
        Assert.assertFalse(receiptSellPayload.isPawnshop());
        Assert.assertFalse(receiptSellPayload.isRounding());
        Assert.assertFalse(receiptSellPayload.isTechnicalReturn());
        receiptSellPayload.getContext().put("key1", "value1");
        receiptSellPayload.getContext().put("key2", "value2");

        ReceiptSellPayload receiptSellPayloadForPawnshop = ReceiptSellPayload.builder().forPawnshop().build();
        Assert.assertTrue(receiptSellPayloadForPawnshop.isPawnshop());
    }

    @Test
    public void paymentSellPayloadTest()
    {
        Payment payment = Payment.builder().build();
        Assert.assertNull(payment.getPawnshopIsReturn());
    }

    @Test
    public void orderTest()
    {
        Order order = Order.builder().build();
        Assert.assertEquals(Order.DEFAULT_LIMIT, order.getLimit());
        Assert.assertEquals(0, order.getOffset());
    }

    @Test
    public void shiftFilterTest()
    {
        Assert.assertEquals("", ShiftFilter.empty().toString());
        Assert.assertEquals("?statuses=CLOSED&statuses=OPENED", ShiftFilter.builder()
            .status(Shift.STATUS.CLOSED)
            .status(Shift.STATUS.OPENED)
            .build()
            .toString()
        );
        Assert.assertEquals("?desc=true&limit=20&offset=2", ShiftFilter.builder()
            .order(Order.builder()
                .desc(true).limit(20).offset(2).build())
            .build()
            .toString()
        );
        Assert.assertEquals("?statuses=CLOSED&statuses=OPENED&desc=true&limit=20&offset=2", ShiftFilter.builder()
            .status(Shift.STATUS.CLOSED)
            .status(Shift.STATUS.OPENED)
            .order(Order.builder()
                .desc(true).limit(20).offset(2).build())
            .build()
            .toString()
        );
    }

    @Test
    public void receiptFilterTest()
    {
        Assert.assertEquals("", ReceiptFilter.empty().toString());
        Assert.assertEquals("?fiscal_code=12344&serial=101&self_receipts=true", ReceiptFilter.builder()
                .fiscalCode("12344")
                .serial("101")
                .selfReceipts(true)
                .build()
                .toString()
        );
        Assert.assertEquals("?desc=true&limit=20&offset=2", ReceiptFilter.builder()
                .order(Order.builder()
                        .desc(true).limit(20).offset(2).build())
                .build()
                .toString()
        );
        Assert.assertEquals("?desc=true&limit=20&offset=2&fiscal_code=12344&serial=101&self_receipts=true", ReceiptFilter.builder()
                .fiscalCode("12344")
                .serial("101")
                .selfReceipts(true)
                .order(Order.builder()
                        .desc(true).limit(20).offset(2).build())
                .build()
                .toString()
        );
        Assert.assertEquals("?desc=true&limit=20&offset=2&fiscal_code=12344&serial=101&self_receipts=true" +
                "&shift_id=A134480b-V123-4T96-g50c-18cab15bfcb3&shift_id=5b6b480b-R634-4d16-850c-48cab15bfcb3" +
                "&cash_register_id=A134480b-V123-4T96-g50c-18cab15bfcb3&cash_register_id=5b6b480b-R634-4d16-850c-48cab15bfcb3", ReceiptFilter.builder()
                .fiscalCode("12344")
                .serial("101")
                .selfReceipts(true)
                .order(Order.builder()
                        .desc(true).limit(20).offset(2).build())
                .shiftIds(Arrays.asList("A134480b-V123-4T96-g50c-18cab15bfcb3","5b6b480b-R634-4d16-850c-48cab15bfcb3"))
                .cashRegisterIds(Arrays.asList("A134480b-V123-4T96-g50c-18cab15bfcb3","5b6b480b-R634-4d16-850c-48cab15bfcb3"))
                .build()
                .toString()
        );

        // needs to add date tests
    }
}
