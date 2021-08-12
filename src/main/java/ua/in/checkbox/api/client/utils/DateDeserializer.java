package ua.in.checkbox.api.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateDeserializer extends StdDeserializer<Date>
{
    private static final SimpleDateFormat withMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");
    private static final SimpleDateFormat withoutMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    public DateDeserializer()
    {
        this(null);
    }

    public DateDeserializer(Class<?> vc)
    {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        String dateString = p.getText();
        if (dateString.isEmpty())
        {
            return null;
        }
        try
        {
            return withMillis.parse(dateString);
        }
        catch (ParseException pe)
        {
            try
            {
                return withoutMillis.parse(dateString);
            }
            catch (ParseException pe1)
            {
                String error = String.format("Unable to parse date '%s'", dateString);
                log.error(error, pe1);
                throw new RuntimeException(error, pe1);
            }
        }
        catch (Exception e)
        {
            String error = String.format("Unable to parse date '%s'", dateString);
            log.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
}
