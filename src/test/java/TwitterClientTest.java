import com.saratorrey.womencodersbot.TwitterClient;

import com.saratorrey.womencodersbot.service.TwitterService;
import org.junit.Test;
import org.junit.Assert;

import javax.annotation.Resource;

public class TwitterClientTest {

    @Resource
    TwitterService twitterService;


    @Test
    public void testCountNumbers() {

        Assert.assertEquals( 6, (int) twitterService.numberCount( "testStringWithNumber238123" ) );
        Assert.assertEquals( 8, (int) twitterService.numberCount( "tes34tStringWithNumber238123" ) );
    }
}