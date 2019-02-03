import com.saratorrey.womencodersbot.TwitterClient;

import com.saratorrey.womencodersbot.WomenCodersBotApplication;
import com.saratorrey.womencodersbot.service.TwitterService;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WomenCodersBotApplication.class)
@ActiveProfiles(profiles = "test")
public class TwitterClientTest {

    @Resource
    TwitterService twitterService;


    @Test
    public void testCountNumbers() {

        Assert.assertEquals( 6, (int) twitterService.numberCount( "testStringWithNumber238123" ) );
        Assert.assertEquals( 8, (int) twitterService.numberCount( "tes34tStringWithNumber238123" ) );
    }
}