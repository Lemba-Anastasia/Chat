package chat;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Lemba on 16.09.2018.
 */
public class IdCounterTest extends TestCase {
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(IdCounter.getInstance());
    }

    public void testGetId() throws Exception {
        int i=0;
        Assert.assertEquals(1,++i);
    }

}