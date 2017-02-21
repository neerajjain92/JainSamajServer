package com.jainsamaj.util;


import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jaine03 on 15/02/17.
 */
public class SimpleNotificationService {

    private static AmazonSNSClient snsClient;
    private static Logger logger = LoggerFactory.getLogger(SimpleNotificationService.class);

    static {
        //create a new SNS client and set endpoint
        snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());
        snsClient.setRegion(Region.getRegion(Regions.DEFAULT_REGION));
    }

    /**
     * This method will create a topic specific to the newly created user
     * We can publish otp/login or other information to this Channel.
     * @param pmfKey : Unique Topic Name
     * @return Topic ARN which will be used in subscribing to this particular topic
     */
    public static String createTopic(final String pmfKey) {

        //create a new SNS topic
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(pmfKey);
        CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);

        //print TopicArn
        logger.debug("Create Topic Result {}",createTopicResult);
        //get request id for CreateTopicRequest from SNS metadata
        logger.debug("CreateTopicRequest - {} ", snsClient.getCachedResponseMetadata(createTopicRequest));

        return createTopicResult.getTopicArn();
    }

    /**
     * This method will subscribe the User to the particular Topic, User has to confirm subscription in order
     * to receive further communication via this topic
     * @param topicARN : Unique Identifier for Topic created before
     * @return Subscription Request Id
     */
    public static String subscribeToTopic(final String topicARN, final String emailId) {

        //Subscribe to SNS topic
        SubscribeRequest subscribeRequest = new SubscribeRequest(topicARN, "email", emailId);
        snsClient.subscribe(subscribeRequest);

        //Get Request Id for Subscription
        logger.debug("SubscribeRequest - " , snsClient.getCachedResponseMetadata(subscribeRequest));
        logger.debug("Check your email and confirm subscription.");

        return snsClient.getCachedResponseMetadata(subscribeRequest).getRequestId();
    }

    public static String publishToTopic(final String topicARN, final String message) {

        // Publish to SNS topic
        PublishRequest publishRequest = new PublishRequest(topicARN, message);
        PublishResult publishResult = snsClient.publish(publishRequest);
        //print MessageId of message published to SNS topic
        logger.debug("MessageId - {} ", publishResult.getMessageId());
        return publishResult.getMessageId();
    }
}
