import org.junit.Assert;
import org.junit.Test;
import ua.in.checkbox.api.client.dto.receipt.Payment;
import ua.in.checkbox.api.client.dto.receipt.ReceiptSellPayload;

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
}
