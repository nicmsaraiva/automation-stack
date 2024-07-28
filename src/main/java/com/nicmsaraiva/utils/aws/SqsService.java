package com.nicmsaraiva.utils.aws;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;

public class SqsService {
    AmazonSQS sqs = AmazonSQSClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
            .build();

    public void createSqsQueue(String queueName) {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName)
                .addAttributesEntry("DelaySeconds", "60")
                .addAttributesEntry("MessageRetentionPeriod", "86400");
        try {
            sqs.createQueue(createQueueRequest);
        } catch (AmazonSQSException exception) {
            if (!exception.getErrorCode().equals("QueueAlreadyExists")) {
                throw exception;
            }
        }
    }

    public void listQueues() {
        ListQueuesResult listQueuesResult = sqs.listQueues();
        System.out.println("Your SQS Queue URLs:");
        for (String url : listQueuesResult.getQueueUrls()) {
            System.out.println("URL: " + url);
        }
    }

    public void listQueuePrefix(String prefix){
        ListQueuesResult listQueuesResult = sqs.listQueues(new ListQueuesRequest(prefix));
        System.out.println("Queue URLs with prefix: " + prefix);
        for (String url : listQueuesResult.getQueueUrls()) {
            System.out.println("URL: " + url);
        }
    }
}
