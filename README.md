# automation-stack

A comprehensive stack of tests for various services, including AWS Services, using Java.

### Overview

The `automation-stack` project provides a set of tests to ensure the correct functionality of different services. This includes testing AWS Services functionalities locally using LocalStack. The tests are written in Java and use JUnit for assertions and Mockito for mocking.

### Prerequisites

- Docker
- Docker Compose
- Java (JDK 11 or later)
- Maven

### Setup

- To run the tests, you'll need to start the LocalStack environment using Docker Compose. This will spin up the necessary services locally.


```sh
docker-compose up
```

- Run the tests

```sh
mvn test
```

### Test Examples
- The SqsService class provides methods to interact with AWS SQS. Here's an example of a test to create an SQS queue:

```java
@Test
public void testCreateSqsQueue() {
   String queueName = "test-create-queue";
   sqsService.createSqsQueue(queueName);

   ListQueuesResult listQueuesResult = sqs.listQueues();
   assertTrue(listQueuesResult.getQueueUrls().stream()
           .anyMatch(url -> url.endsWith(queueName)));
}
```

### Handle Exceptions
- You can also test how the application handles exceptions:

```java
@Test
public void testCreateQueueAlreadyExistsException() {
   AmazonSQS mockSqs = mock(AmazonSQS.class);
   SqsService mockSqsService = new SqsService(mockSqs);
   String queueName = "existing-queue";

   AmazonSQSException exception = new AmazonSQSException("Queue already exists");
   exception.setErrorCode("QueueAlreadyExists");
   doThrow(exception).when(mockSqs).createQueue(any(CreateQueueRequest.class));

   assertDoesNotThrow(() -> mockSqsService.createSqsQueue(queueName));
}

```

### Clone the Repository

```sh
git clone https://github.com/nicmsaraiva/automation-stack.git
cd automation-stack
