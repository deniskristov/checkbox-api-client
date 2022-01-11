import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import ua.in.checkbox.api.client.dto.receipt.ReceiptSellPayload;

public class DtoSerializationTest
{
    private ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void receiptSellPayloadTest() throws JsonProcessingException
    {
        String sellPayload = "{\"departament\":null,\"goods\":null,\"delivery\":null,\"payments\":null,\"header\":null,\"footer\":null,\"barcode\":null,\"discounts\":[],\"context\":{\"property2\":\"value2\",\"property1\":\"value1\"},\"rounding\":false,\"cashier_name\":null,\"is_pawnshop\":false,\"order_id\":null,\"technical_return\":false}";
        ReceiptSellPayload receiptSellPayload = ReceiptSellPayload.builder().build();
        receiptSellPayload.getContext().put("property1", "value1");
        receiptSellPayload.getContext().put("property2", "value2");
        String serializationResult = MAPPER.writeValueAsString(receiptSellPayload);
        Assert.assertEquals(sellPayload, serializationResult);
    }
}
