package com.saratorrey.womencodersbot;

import org.apache.commons.lang3.StringUtils;
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

public class TwitterClient {

    // Don't ever check your credentials into GitHub!
    public static final String TWITTER_CONSUMER_KEY = System.getenv( "TWITTER_CONSUMER_KEY" );
    public static final String TWITTER_CONSUMER_SECRET = System.getenv( "TWITTER_CONSUMER_SECRET" );
    public static final String TWITTER_ACCESS_TOKEN = System.getenv( "TWITTER_ACCESS_TOKEN" );
    public static final String TWITTER_ACCESS_SECRET = System.getenv( "TWITTER_ACCESS_SECRET" );

    // Comma-separated list of accounts to skip/ignore. Helps with spam filtering.
    public static final String SKIP_ACCOUNTS = System.getenv( "TWITTER_SKIP_ACCOUNTS" );


    public static void main( String[] args ) {

        searchTwitter();
    }

    private static void sendTweet( String tweetContent ) {

        // Read API keys from environment variables (don't want to check these into Github! LOL!)
        ConfigurationBuilder cb = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                               TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        try {
            TwitterFactory factory = new TwitterFactory( cb.build() );
            Twitter twitter = factory.getInstance();

            System.out.println( twitter.getScreenName() );
            Status status = twitter.updateStatus( tweetContent );
            System.out.println( "Successfully updated the status to [" + status.getText() + "]." );
        }
        catch ( TwitterException te ) {
            te.printStackTrace();
            System.exit( -1 );
        }
    }

    public static void searchTwitter() {


        // Read API keys from environment variables (don't want to check these into Github! LOL!)

        ConfigurationBuilder cb = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                               TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        TwitterFactory tf = new TwitterFactory( cb.build() );
        final Twitter twitter = tf.getInstance();

        cb = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                          TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        TwitterStream twitterStream = new TwitterStreamFactory( cb.build() ).getInstance();

        StatusListener listener = new StatusListener() {
            public void onStatus( Status status ) {

                // Check if this is an account that should be skipped (helps with spam filtering)
                boolean isAccountToSkip = Arrays.stream( StringUtils.split( SKIP_ACCOUNTS, "," ) )
                        .anyMatch( s -> status.getUser().getScreenName().toLowerCase().contains( s.toLowerCase() ) );

                if ( !isAccountToSkip &&
                     !status.isRetweet() && !status.isRetweetedByMe() ) {
                    System.out.println( String.format( "Retweeting: [%s]", status.getText() ) );
                    try {
                        twitter.retweetStatus( status.getId() );
                        twitter.createFriendship( status.getUser().getScreenName() );
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

        twitterStream.filter( new FilterQuery( "#MomsCanCode",
                                               "#MomsWhoCode",
                                               "#WomenWhoCode",
                                               "#WomenCanCode",
                                               "#WomenCoders",
                                               "#GirlsWhoCode",
                                               "#GirlsCanCode",
                                               "#GirlCode",
                                               "GirlDevelopIt",
                                               "#rladies",
                                               "#VueVixens",
                                               "#WOCinTechChat") );


//        TwitterFactory tf = new TwitterFactory( cb.build() );
//        Twitter twitter = tf.getInstance();
//        try {
//            Query query = new Query( "#WomenWhoCode" );
//            query.since( "2018-12-25" );
//            query.until( "2019-01-01" );
//            QueryResult result;
//            result = twitter.search( query );
//            List<Status> tweets = result.getTweets();
//            for ( Status tweet : tweets ) {
//                System.out.println( "@" + tweet.getUser().getScreenName() + " - " + tweet.getText() );
//
//                if ( !tweet.getUser().getScreenName().toLowerCase().contains( "womencodersbot" ) &&
//                     !tweet.getUser().getScreenName().toLowerCase().contains( "saratorrey" ) &&
//                     !tweet.getUser().getScreenName().toLowerCase().contains( "plumbing" ) &&
//                     !tweet.isRetweet() && !tweet.isRetweetedByMe() ) {
//                    System.out.println( tweet.getText() );
//                    try {
//                        twitter.retweetStatus( tweet.getId() );
//                    }
//                    catch ( Exception e ) {
//                        System.out.println( e.getMessage() );
//                    }
//                }
//            }
//
//            System.exit( 0 );
//        }
//        catch ( TwitterException te ) {
//            te.printStackTrace();
//            System.out.println( "Failed to search tweets: " + te.getMessage() );
//            System.exit( -1 );
//        }
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
}