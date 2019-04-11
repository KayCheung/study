package com.housaire;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, TimeoutException, InterruptedException
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // (String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        channel.exchangeDeclare("housaire_exchange_fanout", BuiltinExchangeType.FANOUT, false, true, null);
        channel.queueDeclare("housaire_queue_test", false, false, false, null);
        channel.queueBind("housaire_queue_test", "housaire_exchange_fanout", "");
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener()
        {
            @Override
            public void handleAck(long deliveryTag, boolean b) throws IOException
            {
                System.err.println(deliveryTag + " is ack");
            }

            @Override
            public void handleNack(long deliveryTag, boolean b) throws IOException
            {
                System.err.println(deliveryTag + " is nack");
            }
        });

        channel.txSelect();

        channel.basicPublish("housaire_exchange_fanout", "", false, false, null, "hello first one".getBytes());
        channel.basicQos(1);

        channel.txCommit();

        System.out.println("消息发送完成...");
        Thread.sleep(10000L);

        channel.basicConsume("housaire_queue_test", false, Thread.currentThread().getName(), false, false, null, new Consumer() {

            @Override
            public void handleConsumeOk(String s)
            {
                System.out.println("handleConsumeOk -> " + s);
            }

            @Override
            public void handleCancelOk(String s)
            {
                System.out.println("handleCancelOk -> " + s);
            }

            @Override
            public void handleCancel(String s) throws IOException
            {
                System.out.println("handleCancel -> " + s);
            }

            @Override
            public void handleShutdownSignal(String s, ShutdownSignalException e)
            {
                System.out.println("handleShutdownSignal -> " + s);
                e.printStackTrace();
            }

            @Override
            public void handleRecoverOk(String s)
            {
                System.out.println("handleRecoverOk -> " + s);
            }

            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException
            {
                System.out.println("handleRecoverOk -> " + s);
                System.err.println("Message: " + new String(bytes));
                channel.basicAck(envelope.getDeliveryTag(), false); // multiple : true 表示将小于当前DeliveryTag的消息一并ack，false 表示只将当前DeliveryTag消息进行ack
            }
        });
    }
}
