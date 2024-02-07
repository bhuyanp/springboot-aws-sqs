package com.example.springbootawssqs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SQSService {
    private final AwsCredentials awsCredentials;
    private final String MSG_ATTR_APPLICATION = "Application";

    @Value("${aws.sqs.queue.standard.url}")
    private String standardQueueURL;
    @Value("${aws.sqs.queue.fifo.url}")
    private String fifoQueueURL;


    private SqsClient getSQSClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }



    public String postMessage(String message, MESSAGE_QUEUE_TYPE messageQueueType) {
        String queueURL = messageQueueType==MESSAGE_QUEUE_TYPE.FIFO?fifoQueueURL:standardQueueURL;

        MessageAttributeValue messageAttributeValue = MessageAttributeValue.builder()
                .stringValue("Demo Application")
                .dataType("String")
                .build();
        Map<String, MessageAttributeValue> messageAttributes = Map.of(MSG_ATTR_APPLICATION, messageAttributeValue);

        SendMessageRequest sendMessageRequest = switch (messageQueueType){
            case FIFO -> SendMessageRequest.builder()
                    .queueUrl(queueURL)
                    .messageBody(message)
                    .messageGroupId("test-group-1")//required for FIFO
                    //required for FIFO is If content based deduplication is not enabled
                    .messageDeduplicationId(UUID.randomUUID().toString()).messageAttributes(messageAttributes)
                    .build();
            case STANDARD -> SendMessageRequest.builder()
                    .queueUrl(queueURL)
                    .messageBody(message)
                    .delaySeconds(5)
                    .messageAttributes(messageAttributes)
                    .build();
        };

        SendMessageResponse sendMessageResponse = getSQSClient().sendMessage(sendMessageRequest);
        return sendMessageResponse.messageId();
    }


    public List<Message> consumeMessages(MESSAGE_QUEUE_TYPE messageQueueType) {
        String queueURL = messageQueueType==MESSAGE_QUEUE_TYPE.FIFO?fifoQueueURL:standardQueueURL;

        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueURL)
                .waitTimeSeconds(10)
                .maxNumberOfMessages(10)
                .messageAttributeNames(MSG_ATTR_APPLICATION)
                .build();
        List<Message> messages = getSQSClient().receiveMessage(receiveMessageRequest)
                .messages();
        messages.forEach(message -> deleteMessage(message, queueURL));
        return messages;
    }



    public void deleteMessage(Message message, String queueUrl) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();
        getSQSClient().deleteMessage(deleteMessageRequest);
    }


}

enum MESSAGE_QUEUE_TYPE {
    STANDARD, FIFO
}