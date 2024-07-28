package com.nicmsaraiva.unit.aws;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.nicmsaraiva.utils.aws.SqsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SqsServiceTest {
    private static SqsService sqsService;
    private static AmazonSQS sqs;

    @BeforeAll
    public static void setup() {
        sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();
        sqsService = new SqsService(sqs);
    }

    @Test
    public void testCreateSqsQueue() {
        String queueName = "test-queue";
        sqsService.createSqsQueue(queueName);
        ListQueuesResult listQueuesResult = sqs.listQueues();
        Assertions.assertTrue(listQueuesResult.getQueueUrls().stream()
                .anyMatch(url -> url.endsWith(queueName)));
    }
}
