import org.junit.Assert;
import org.junit.Test;
import ua.in.checkbox.api.client.utils.CheckboxApiCallException;

public class ExceptionTest
{
    @Test
    public void testReason()
    {
        CheckboxApiCallException unknown = CheckboxApiCallException.builder().build();
        Assert.assertTrue(unknown.isUnknownReason());

        CheckboxApiCallException known = CheckboxApiCallException.builder().httpCode(500).build();
        Assert.assertFalse(known.isUnknownReason());
    }
}
