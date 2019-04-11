package com.housaire.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/4/9 14:01
 * @see
 * @since 1.0.0
 */
public class KafkaProducerMain
{

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092,localhost:39092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "0");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> record = new ProducerRecord<>("kafka_topic", 2, "kafkaMsg","Hello Kafka!  -  " + Calendar.getInstance().getTime().toLocaleString());

        RecordMetadata recordMetadata = producer.send(record).get();

        System.out.println("Partition: " + recordMetadata.partition() + "   Offset: " + recordMetadata.offset());

        producer.close();
    }

}
