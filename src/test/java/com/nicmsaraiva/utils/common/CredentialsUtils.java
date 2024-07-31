package com.nicmsaraiva.utils.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;

public class CredentialsUtils {
    private static final Dotenv dotenv = Dotenv.load();

    public static AmazonSQS createSqsClient() {
        String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
        String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
        String region = dotenv.get("AWS_REGION");
        String endpointUrl = dotenv.get("AWS_ENDPOINT_URL");

        assert accessKey != null;
        assert secretKey != null;
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public static String getUrl(String account) {
        return dotenv.get("AWS_ENDPOINT_URL") + "/" + account;
    }
}
