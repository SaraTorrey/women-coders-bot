package com.saratorrey.womencodersbot.service.impl;

import com.google.common.collect.Lists;
import com.saratorrey.womencodersbot.service.TwitterService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;

import javax.annotation.PostConstruct;

@Component
public class TwitterServiceImpl implements TwitterService {

    // Don't ever check your credentials into GitHub!
    private static final String TWITTER_CONSUMER_KEY = System.getenv( "TWITTER_CONSUMER_KEY" );
    private static final String TWITTER_CONSUMER_SECRET = System.getenv( "TWITTER_CONSUMER_SECRET" );
    private static final String TWITTER_ACCESS_TOKEN = System.getenv( "TWITTER_ACCESS_TOKEN" );
    private static final String TWITTER_ACCESS_SECRET = System.getenv( "TWITTER_ACCESS_SECRET" );

    // Comma-separated list of accounts to skip/ignore. Helps with spam filtering.
    private static final String SKIP_ACCOUNTS = System.getenv( "TWITTER_SKIP_ACCOUNTS" );
    private static final String SKIP_PHRASES = System.getenv( "TWITTER_SKIP_PHRASES" );

    private static final int FAKE_ACCOUNT_NUMBER_THRESHOLD = 6;

    // Filter query indicating which hashtags to retweet
    private static final FilterQuery FILTER_QUERY = new FilterQuery( "#MomsCanCode",
                                                                     "#MomsWhoCode",
                                                                     "#WomenWhoCode",
                                                                     "#WomenCanCode",
                                                                     "#WomenCoders",
                                                                     "#GirlsWhoCode",
                                                                     "#GirlsCanCode",
                                                                     "#MomsInTech");

    @Autowired
    Environment environment;

    @Override
    // Don't actually run the bot when running in test mode.

    @PostConstruct // Runs the bot when the server starts up
    public void runBot() {

        // Skip with test profile
        if ( Arrays.stream( environment.getActiveProfiles() ).anyMatch( v -> v.equals( "test" ) ) ) {
            System.out.println( "Bot will not be started, as profile is currently running as test." );
            return;
        }

        // Read API keys from environment variables (don't want to check these into Github! LOL!)
        ConfigurationBuilder configurationBuilder = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                                                 TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        TwitterFactory tf = new TwitterFactory( configurationBuilder.build() );
        final Twitter twitter = tf.getInstance();

        configurationBuilder = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                            TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        TwitterStream twitterStream = new TwitterStreamFactory( configurationBuilder.build() ).getInstance();
        StatusListener listener = new StatusListener() {
            public void onStatus( Status status ) {

                // Check if account is OK to retweet. Try and avoid obvious fake and spam accounts.
                if ( isAccountOk( status ) ) {
                    System.out.println( String.format( "Retweeting: [%s]", status.getText() ) );
                    try {
                        twitter.retweetStatus( status.getId() );
                    }
                    catch ( Exception e ) {
                        System.out.println( e.getMessage() );
                    }
                }
                else {
                    System.out.println( String.format( "Skipping: [%s]", status.getText() ) );
                }
            }

            public void onDeletionNotice( StatusDeletionNotice statusDeletionNotice ) {

            }

            public void onTrackLimitationNotice( int numberOfLimitedStatuses ) {

            }

            public void onScrubGeo( long l, long l1 ) {

            }

            public void onStallWarning( StallWarning stallWarning ) {

            }

            public void onException( Exception ex ) {

                ex.printStackTrace();
            }
        };

        twitterStream.addListener( listener );
        twitterStream.filter( FILTER_QUERY );
    }

    private boolean isAccountOk( Status status ) {

        // Check if this is an account that should be skipped (helps with spam filtering)
        boolean isAccountToSkip = Arrays.stream( StringUtils.split( SKIP_ACCOUNTS, "," ) )
                .anyMatch( s -> status.getUser().getScreenName().toLowerCase().contains( s.trim().toLowerCase() ) );

        // Check if this is for phrases that should be skipped (helps with spam filtering)
        boolean containsPhraseToSkip = Arrays.stream( StringUtils.split( SKIP_PHRASES, "," ) )
                .anyMatch( s -> status.getText().toLowerCase().contains( s.trim().toLowerCase() ) );


        // Skip accounts with too many numbers in the name. Fake accounts often have lots of numbers in the name.
        boolean tooManyNumberInAccountName =
                numberCount( status.getUser().getScreenName() ) >= FAKE_ACCOUNT_NUMBER_THRESHOLD;
        return !isAccountToSkip &&
               !containsPhraseToSkip &&
               !status.isRetweet() &&
               !status.isRetweetedByMe() && // Avoid infinite loop
               !tooManyNumberInAccountName;
    }

    private static ConfigurationBuilder buildConfig( String consumerKey,
                                                     String consumerSecret,
                                                     String accessToken,
                                                     String accessSecret ) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled( true )
                .setOAuthConsumerKey( consumerKey )
                .setOAuthConsumerSecret( consumerSecret )
                .setOAuthAccessToken( accessToken )
                .setOAuthAccessTokenSecret( accessSecret );
        return cb;
    }

    /**
     * Counts the number of characters in the give String.
     */
    @Override
    public Integer numberCount( String string ) {

        int count = 0;
        for ( Character character : Lists.charactersOf( string ) ) {
            if ( Character.isDigit( character ) ) {
                count++;

            }
        }
        return count;
    }

    /**
     * This method is currently not being used.
     * @param tweetBody
     */
    private static void sendTweet( String tweetBody ) {

        // Read API keys from environment variables (don't want to check these into Github! LOL!)
        ConfigurationBuilder configurationBuilder = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                               TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        try {
            TwitterFactory factory = new TwitterFactory( configurationBuilder.build() );
            Twitter twitter = factory.getInstance();

            System.out.println( twitter.getScreenName() );
            Status status = twitter.updateStatus( tweetBody );
            System.out.println( "Successfully updated the status to [" + status.getText() + "]." );
        }
        catch ( TwitterException te ) {
            te.printStackTrace();
            System.exit( -1 );
        }
    }
}
