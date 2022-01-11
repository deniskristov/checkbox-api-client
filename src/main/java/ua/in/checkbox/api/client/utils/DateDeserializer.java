package ua.in.checkbox.api.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j
public class DateDeserializer extends StdDeserializer<Date>
{
    private static final DateTimeFormatter FORMATTER_WITH_MS
        = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
    private static final DateTimeFormatter FORMATTER_WITH_MS_NO_TIMEZONE
        = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

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
            ZonedDateTime dateTime = ZonedDateTime.parse(dateString, FORMATTER_WITH_MS);
            return java.util.Date
                .from(dateTime.toInstant());
        }
        catch (DateTimeParseException e)
        {
            try
            {
                ZonedDateTime dateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                return java.util.Date
                    .from(dateTime.toInstant());
            }
            catch (Exception e1)
            {
                try
                {
                    LocalDateTime dateTime = LocalDateTime.parse(dateString, FORMATTER_WITH_MS_NO_TIMEZONE);
                    return java.util.Date
                        .from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
                }
                catch (Exception e2)
                {
                    String error = String.format("Unable to parse date '%s'", dateString);
                    log.error(error, e1);
                    throw new RuntimeException(error, e1);
                }
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
