package com.nicmsaraiva.utils.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.Topic;

public class SnsService {
    private final AmazonSNS sns;

    public SnsService(AmazonSNS sns) {
        this.sns = sns;
    }

    public ListTopicsResult listTopics() {
        ListTopicsResult listTopics = sns.listTopics();
        System.out.println("Your Topics URLs:");
        for (Topic topic : listTopics.getTopics()) {
            System.out.println("URL: " + topic.getTopicArn());
        }
        return listTopics;
    }

    public void sendMessageTopic(String message, String topicName) {

    }

}
