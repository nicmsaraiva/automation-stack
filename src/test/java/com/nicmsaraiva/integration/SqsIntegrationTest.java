package com.nicmsaraiva.integration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.nicmsaraiva.utils.aws.SqsService;
import com.nicmsaraiva.utils.common.CredentialsUtils;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SqsIntegrationTest {
    private static String URL;
    private static SqsService sqsService;
    private static AmazonSQS sqs;
    private List<String> queuesToDelete;

    @BeforeAll
    public static void setup() {
        sqs = CredentialsUtils.createSqsClient();
        URL = CredentialsUtils.getUrl("000000000000");
        sqsService = new SqsService(sqs);
    }

    @BeforeEach
    public void setupTest() {
        queuesToDelete = new ArrayList<>();
    }

    @AfterEach
    public void cleanUp() {
        for (String queueUrl : queuesToDelete) {
            sqsService.deleteQueueByUrl(queueUrl);
        }
    }

    private void createQueue(String queueName) {
        sqsService.createSqsQueue(queueName);
        queuesToDelete.add(URL + queueName);
    }

    @Test
    public void testCreateSqsQueue() {
        String queueName = "test-create-queue";
        createQueue(queueName);

        ListQueuesResult listQueuesResult = sqs.listQueues();
        assertTrue(listQueuesResult.getQueueUrls().stream()
                .anyMatch(url -> url.endsWith(queueName)));
    }

    @Test
    public void testListQueues() {
        String queueName1 = "test-queue-1";
        String queueName2 = "test-queue-2";
        createQueue(queueName1);
        createQueue(queueName2);

        ListQueuesResult listQueuesResult = sqsService.listQueues();
        assertEquals(2, listQueuesResult.getQueueUrls().size());
    }

    @Test
    public void testListQueuesPrefix() {
        String prefix = "test-prefix-";
        String queueName1 = prefix + "queue-1";
        String queueName2 = prefix + "queue-2";
        createQueue(queueName1);
        createQueue(queueName2);

        ListQueuesResult listQueuesResult = sqsService.listQueuePrefix(prefix);
        assertEquals(2, listQueuesResult.getQueueUrls().size(), "List queue result with prefix " + prefix + " is not 2");
    }

    @Test
    public void testListQueuesPrefixIncorrect() {
        String prefix = "test-prefix-";
        String queueName1 = prefix + "queue-1";
        String queueName2 = prefix + "queue-2";
        createQueue(queueName1);
        createQueue(queueName2);

        ListQueuesResult listQueuesResult = sqsService.listQueuePrefix("incorrect-prefix");
        assertEquals(0, listQueuesResult.getQueueUrls().size(), "List queue result with incorrect prefix is not 0");
    }

    @Test
    public void testSendMessageToQueue() throws InterruptedException {
        String queueName = "test-queue-send-message";
        String sqsMessage = "Test SQS Message";
        createQueue(queueName);
        sqsService.sendMessageToQueue(sqsMessage, URL + queueName);

        // Need delay to send message in queue
        Thread.sleep(6000);

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(URL + queueName);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        assertEquals(1, messages.size(), "Expected one message in the queue");
        assertEquals(sqsMessage, messages.get(0).getBody(), "Message body should match the sent message");
    }
}
