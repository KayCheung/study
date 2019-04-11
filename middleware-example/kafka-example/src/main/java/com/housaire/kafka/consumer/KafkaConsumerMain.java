package com.housaire.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/9 14:21
 * @see
 * @since 1.0.0
 */
public class KafkaConsumerMain
{

    public static void main(String[] args)
    {
        String serversConfig = "localhost:39092";
        String groupId = "3";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serversConfig);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.partitionsFor("kafka_topic").forEach(partitionInfo -> {
            System.out.println("Partition ID:  " + partitionInfo.partition());
            System.out.println("Partition Leader:  " + partitionInfo.leader().toString());
            System.out.println("Partition Replicas:  " + partitionInfo.replicas().length);
        });

        TopicPartition topicPartition = new TopicPartition("kafka_topic", 2);
        consumer.assign(Arrays.asList(topicPartition));

//        consumer.subscribe(Pattern.compile("kafka_topic"));

        System.out.println("Group ID: " + groupId + "   Servers: " + serversConfig);

        while (true)
        {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            for (Iterator<ConsumerRecord<String, String>> recordIterator = records.iterator(); recordIterator.hasNext();)
            {
                ConsumerRecord<String, String> record = recordIterator.next();
                System.err.println(record.offset() + ". " + record.key() + " = " + record.value());
            }
        }
    }

}
