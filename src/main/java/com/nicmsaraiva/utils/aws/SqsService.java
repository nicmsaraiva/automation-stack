package com.nicmsaraiva.utils.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;

public class SqsService {
    private final AmazonSQS sqs;

    public SqsService(AmazonSQS sqs) {
        this.sqs = sqs;
    }

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

    public ListQueuesResult listQueues() {
        ListQueuesResult listQueuesResult = sqs.listQueues();
        System.out.println("Your SQS Queue URLs:");
        for (String url : listQueuesResult.getQueueUrls()) {
            System.out.println("URL: " + url);
        }
        return listQueuesResult;
    }

    public ListQueuesResult listQueuePrefix(String prefix){
        ListQueuesResult listQueuesResult = sqs.listQueues(new ListQueuesRequest(prefix));
        System.out.println("Queue URLs with prefix: " + prefix);
        for (String url : listQueuesResult.getQueueUrls()) {
            System.out.println("URL: " + url);
        }
        return listQueuesResult;
    }

    public void deleteQueueByUrl(String queueUrl) {
        try {
            sqs.deleteQueue(new DeleteQueueRequest(queueUrl));
            System.out.println("Queue deleted: " + queueUrl);
        } catch (AmazonSQSException exception) {
            System.err.println("Error deleting queue: " + exception.getMessage());
            throw exception;
        }
    }
}
