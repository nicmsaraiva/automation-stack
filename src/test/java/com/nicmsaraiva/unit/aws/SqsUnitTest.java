package com.nicmsaraiva.unit.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.nicmsaraiva.utils.aws.SqsService;
import com.nicmsaraiva.utils.common.CredentialsUtils;
import org.junit.jupiter.api.Test;

public class SqsUnitTest {

    @Test
    public void testCreateSqsQueue() {
        AmazonSQS mockSqs = mock(AmazonSQS.class);
        SqsService sqsService = new SqsService(mockSqs);
        String expectedQueueUrl = CredentialsUtils.getUrl("000000000000") + "test-queue";
        CreateQueueResult createQueueResult = new CreateQueueResult().withQueueUrl(expectedQueueUrl);

        when(mockSqs.createQueue(any(CreateQueueRequest.class))).thenReturn(createQueueResult);

        String actualQueueUrl = sqsService.createSqsQueue("test-queue");
        assertEquals(expectedQueueUrl, actualQueueUrl);
    }
}
