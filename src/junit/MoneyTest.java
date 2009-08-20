package junit;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MoneyTest extends TestCase {
	
    private Money f12CHF;
    private Money f14CHF;   

    protected void setUp() {
        f12CHF= new Money(12, "CHF");
        f14CHF= new Money(14, "CHF");
    }

    public void testEquals() {
        Assert.assertTrue(!f12CHF.equals(null));
        Assert.assertEquals(f12CHF, f12CHF);
        Assert.assertEquals(f12CHF, new Money(12, "CHF"));
        Assert.assertTrue(!f12CHF.equals(f14CHF));
    }

    public void testSimpleAdd() {
        Money expected= new Money(26, "CHF");
        Money result= f12CHF.add(f14CHF);
        Assert.assertTrue(expected.equals(result));
    }

}
