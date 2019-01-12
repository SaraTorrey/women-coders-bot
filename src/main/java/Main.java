import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

    public static void main( String[] args ) {

        System.out.println( "Begin Sending Tweets from Java!" );

        // Read API keys from environment variables (don't want to check these into Github! LOL!)
        String consumerKey = System.getenv("TWITTER_CONSUMER_KEY");
        String consumerSecret = System.getenv( "TWITTER_CONSUMER_SECRET" );
        String accessToken = System.getenv( "TWITTER_ACCESS_TOKEN" );
        String accessSecret = System.getenv( "TWITTER_ACCESS_SECRET" );

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled( true )
                .setOAuthConsumerKey( consumerKey )
                .setOAuthConsumerSecret( consumerSecret )
                .setOAuthAccessToken( accessToken )
                .setOAuthAccessTokenSecret( accessSecret );

        try {
            TwitterFactory factory = new TwitterFactory( cb.build() );
            Twitter twitter = factory.getInstance();

            System.out.println( twitter.getScreenName() );
            Status status = twitter.updateStatus( "Send my first tweet from a Java application! ☕️👩🏻‍💻" );
            System.out.println( "Successfully updated the status to [" + status.getText() + "]." );
        }
        catch ( TwitterException te ) {
            te.printStackTrace();
            System.exit( -1 );
        }

    }
}