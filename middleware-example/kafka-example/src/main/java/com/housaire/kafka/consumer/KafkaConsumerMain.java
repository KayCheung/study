package com.housaire.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;
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
        String serversConfig = "localhost:19092,localhost:29092,localhost:39092";
        String groupId = "a";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serversConfig);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        consumer.partitionsFor("kafka_multip_partition").forEach(partitionInfo -> {
            System.out.println("Partition ID:  " + partitionInfo.partition());
            System.out.println("Partition Leader:  " + partitionInfo.leader().toString());
            System.out.println("Partition Replicas:  " + partitionInfo.replicas().length);
            topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
        });
        topicPartitions.remove(1);
        topicPartitions.remove(0);
        consumer.assign(topicPartitions);

//        consumer.subscribe(Pattern.compile("kafka_multip_topic"));

        System.out.println("Group ID: " + groupId + "   Servers: " + serversConfig);

        consumer.assignment().forEach(topicPartition -> System.out.println(topicPartition.toString()));

        while (true)
        {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            for (Iterator<ConsumerRecord<String, String>> recordIterator = records.iterator(); recordIterator.hasNext();)
            {
                ConsumerRecord<String, String> record = recordIterator.next();
                System.err.println("Partition: " + record.partition() + " [ " + record.offset() + ". " + record.key() + " = " + record.value() + " ]");
            }
        }
    }

}
