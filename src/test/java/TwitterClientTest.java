import com.saratorrey.womencodersbot.TwitterClient;

import org.junit.Test;
import org.junit.Assert;

public class TwitterClientTest {

    @Test
    public void testCountNumbers() {

        Assert.assertEquals( 6, (int) TwitterClient.numberCount( "testStringWithNumber238123" ) );
        Assert.assertEquals( 8, (int) TwitterClient.numberCount( "tes34tStringWithNumber238123" ) );
    }
}