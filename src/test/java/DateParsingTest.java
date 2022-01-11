import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.in.checkbox.api.client.dto.good.GoodModel;
import ua.in.checkbox.api.client.utils.DateDeserializer;

import java.util.Date;

public class DateParsingTest
{
    private ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void initMapper()
    {
        SimpleModule dateModule = new SimpleModule();
        dateModule.addDeserializer(Date.class, new DateDeserializer());
        MAPPER.registerModule(dateModule);
    }

    @Test
    public void nullDateParsingTest() throws JsonProcessingException
    {
        String goodModelString = "{\"created_at\":\"\",\"updated_at\":\"\"}";
        GoodModel goodModel = MAPPER.readValue(goodModelString, GoodModel.class);
        Assert.assertEquals(null, goodModel.getCreatedAt());
        Assert.assertEquals(null, goodModel.getUpdatedAt());
    }

    @Test
    public void dateWithMsParsingTest() throws JsonProcessingException
    {
        String goodModelString = "{\"created_at\":\"2021-08-02T17:14:52.244695+00:00\",\"updated_at\":\"\"}";
        GoodModel goodModel = MAPPER.readValue(goodModelString, GoodModel.class);
        Assert.assertEquals("Mon Aug 02 20:14:52 EET 2021", goodModel.getCreatedAt().toString());
        Assert.assertEquals(null, goodModel.getUpdatedAt());
    }

    @Test
    public void dateWithMsNoTimeZoneParsingTest() throws JsonProcessingException
    {
        String goodModelString = "{\"created_at\":\"2022-01-11T19:50:59.414481\",\"updated_at\":\"\"}";
        GoodModel goodModel = MAPPER.readValue(goodModelString, GoodModel.class);
        Assert.assertEquals("Tue Jan 11 19:50:59 EET 2022", goodModel.getCreatedAt().toString());
        Assert.assertEquals(null, goodModel.getUpdatedAt());
    }

    @Test
    public void dateWithoutMsParsingTest() throws JsonProcessingException
    {
        String goodModelString = "{\"created_at\":\"2021-05-06T11:06:51+00:00\",\"updated_at\":\"\"}";
        GoodModel goodModel = MAPPER.readValue(goodModelString, GoodModel.class);
        Assert.assertEquals("Thu May 06 14:06:51 EET 2021", goodModel.getCreatedAt().toString());
        Assert.assertEquals(null, goodModel.getUpdatedAt());
    }
}
